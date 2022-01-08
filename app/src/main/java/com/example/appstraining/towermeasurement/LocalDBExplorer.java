package com.example.appstraining.towermeasurement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.BuildingType;
import com.example.appstraining.towermeasurement.model.CircleTheo;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.model.Section;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalDBExplorer {
    Context mContext;
    private final String LOG_TAG = "LocalDBExplorer";
    private final int MODE_INSERT = 0;
    private final int MODE_UPDATE = 1;

    DBHelper dbHelper;
    SQLiteDatabase localDB;
    ContentValues cv = new ContentValues();
    Cursor c;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");


    public LocalDBExplorer(Context context) {
        mContext = context;
        dbHelper = new DBHelper(mContext, "localDB", null, 1);
        localDB = dbHelper.getWritableDatabase();
    }

    public void update(Building building) {
        Log.d(LOG_TAG, "Starting update building in SQLite id = " + building.getId() +
                "Number of Sections = " + building.getNumberOfSections());
        int updCountB = 0, updCountS = 0, updCountM = 0, updCountR = 0;
        updCountB = localDB.update("buildings", getBuildingCV(building, MODE_UPDATE),
                "b_id = ?", new String[] { String.valueOf(building.getId())});
        Log.d(LOG_TAG, "update building = " + updCountB);

        for (int i = 0; i < building.getNumberOfSections(); i++) {
            Section section = building.getSection(i + 1);
            String[] params = {String.valueOf(section.getId()), String.valueOf(section.getBuilding_id())};
            updCountS = localDB.update("sections", getSectionCV(section, MODE_UPDATE),
                    "(s_id = ? and s_b_id = ?)", params);
            Log.d(LOG_TAG, "update section = " + updCountS);
        }
        int shift = 0;
        // changed side < building.getConfig() + 1
        for (int side = 1; side < 4; side++) {
            for (int sec = 1; sec < building.getNumberOfSections() + 2; sec++) { // include top measure
                Measurement measurement = building.getMeasurements().get(shift + sec - 1);

                Result result = building.getResults().get(shift + sec - 1);
                String[] mParams = {String.valueOf(measurement.getId()),
                        String.valueOf(measurement.getSectionId()),
                        String.valueOf(measurement.getBuildingId())};
                String[] rParams = {String.valueOf(result.getId()),
                        String.valueOf(result.getMeasureID())};
                Log.d(LOG_TAG, "Update measurement ... m_id = " + measurement.getId() +
                        "; m_s_id = " + measurement.getSectionId() +
                        "; m_b_id = " + measurement.getBuildingId()
                );

                updCountM = localDB.update("measures", getMeasurementCV(measurement, MODE_UPDATE),
                        "(m_id = ? and m_s_id = ? and m_b_id = ?)", mParams);
                Log.d(LOG_TAG, "updated measurement = " + updCountM);

                Log.d(LOG_TAG, "Update result ... r_id = " + result.getId() +
                        "; r_s_id = " + result.getSectionID() +
                        "; r_b_id = " + result.getBuildingID()
                );
                updCountR = localDB.update("results", getResultCV(result, MODE_UPDATE),
                        "r_id = ? and r_m_id = ?", rParams);
                Log.d(LOG_TAG, "updated result = " + updCountR);
            }
            shift = shift + building.getNumberOfSections() + 1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Building getBuilding(int id) {
        Log.d(LOG_TAG, "Load building from local DB started id = " + id);
        boolean isBuildingDataAdded = false;
        Set<Integer> addedSectionIds = new HashSet<>();
        int currentSectionId = 0;
        Building localDBBuilding = new Building();

        ArrayList<Section> reportSections = new ArrayList<>();
        ArrayList<Measurement> reportMeasurements = new ArrayList<>();
        ArrayList<Result> reportResults = new ArrayList<>();

        String query = "select * from measures " +
                "inner join results on m_id = r_id " +
                "inner join sections on m_s_id = s_id " +
                "inner join buildings on m_b_id = b_id " +
                "where m_b_id = ?;";
        c = localDB.rawQuery(query, new String[] {String.valueOf(id)});
        //c.moveToFirst();
        //Log.d(LOG_TAG, "Cursor.getString " + c.getInt(c.getColumnIndex("m_id")));
        Log.d(LOG_TAG, "Cursor count " + c.getCount());
        if (c != null) {
            if (c.moveToFirst()) {
                c.moveToFirst();
                do{

                    if(!isBuildingDataAdded) {
                        try {
                            localDBBuilding.setId(id);
                            localDBBuilding.setName(c.getString(c.getColumnIndex("b_name")));
                            localDBBuilding.setAddress(c.getString(c.getColumnIndex("b_address")));
                            localDBBuilding.setType(BuildingType.valueOf(c.getString(c.getColumnIndex("b_type"))));
                            localDBBuilding.setConfig(c.getInt(c.getColumnIndex("b_config")));
                            localDBBuilding.setNumberOfSections(c.getInt(c.getColumnIndex("b_numberofsecs")));
                            localDBBuilding.setHeight(c.getInt(c.getColumnIndex("b_height")));
                            localDBBuilding.setStartLevel(c.getInt(c.getColumnIndex("b_startlevel")));
                            localDBBuilding.setUserName(c.getString(c.getColumnIndex("b_username")));
                            localDBBuilding.setCreationDate(new java.sql.Date(
                                    dateFormat.parse(c.getString(c.getColumnIndex("b_creationdate")))
                                            .getTime())
                            );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        isBuildingDataAdded = true;
                    }

                    currentSectionId = c.getInt(c.getColumnIndex("s_id"));
                    if (!addedSectionIds.contains(currentSectionId)) {
                        Section currentSection = new Section();
                        currentSection.setId(currentSectionId);
                        currentSection.setNumber(c.getInt(c.getColumnIndex("s_number")));
                        currentSection.setWidthBottom(c.getInt(c.getColumnIndex("s_widthbottom")));
                        currentSection.setWidthTop(c.getInt(c.getColumnIndex("s_widthtop")));
                        currentSection.setHeight(c.getInt(c.getColumnIndex("s_height")));
                        currentSection.setName(c.getString(c.getColumnIndex("s_name")));
                        currentSection.setLevel(c.getInt(c.getColumnIndex("s_level")));
                        currentSection.setBuilding_id(c.getInt(c.getColumnIndex("s_b_id")));

                        localDBBuilding.addSection(currentSection);
                        addedSectionIds.add(currentSectionId);
                    }

                    Measurement currentMeasurement = new Measurement();
                    try {
                        currentMeasurement.setId(c.getInt(c.getColumnIndex("m_id")));
                        currentMeasurement.setNumber(c.getInt(c.getColumnIndex("m_number")));
                        currentMeasurement.setCircle(CircleTheo.valueOf(
                                c.getString(c.getColumnIndex("m_circle"))));
                        currentMeasurement.setLeftAngle(c.getDouble(c.getColumnIndex("m_leftangle")));
                        currentMeasurement.setRightAngle(c.getDouble(c.getColumnIndex("m_rightangle")));
                        currentMeasurement.setTheoHeight(c.getInt(c.getColumnIndex("m_theoheight")));
                        currentMeasurement.setDistance(c.getInt(c.getColumnIndex("m_distance")));
                        currentMeasurement.setSectionNumber(c.getInt(c.getColumnIndex("m_sectionnumber")));
                        currentMeasurement.setSide(c.getInt(c.getColumnIndex("m_side")));
                        currentMeasurement.setBaseOrTop(BaseOrTop.valueOf(
                                c.getString(c.getColumnIndex("m_baseortop"))));
                        currentMeasurement.setDate(new java.sql.Date(
                                dateFormat.parse(c.getString(c.getColumnIndex("m_date")))
                                        .getTime()));
                        currentMeasurement.setContractor(c.getString(c.getColumnIndex("m_contractor")));
                        currentMeasurement.setAzimuth(c.getInt(c.getColumnIndex("m_sideazimuth")));
                        currentMeasurement.setSectionId(c.getInt(c.getColumnIndex("m_s_id")));
                        currentMeasurement.setBuildingId(c.getInt(c.getColumnIndex("m_b_id")));

                        localDBBuilding.addMeasurement(currentMeasurement);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Result currentResult = new Result();
                    currentResult.setId(c.getInt(c.getColumnIndex("r_id")));
                    currentResult.setAverageKL(c.getDouble(c.getColumnIndex("averageKL")));
                    currentResult.setAverageKR(c.getDouble(c.getColumnIndex("averageKR")));
                    currentResult.setAverageKLKR(c.getDouble(c.getColumnIndex("averageKLKR")));
                    currentResult.setShiftDegree(c.getDouble(c.getColumnIndex("shift_deg")));
                    currentResult.setShiftMm(c.getInt(c.getColumnIndex("shift_mm")));
                    currentResult.setTanAlfa(c.getDouble(c.getColumnIndex("tan_alfa")));
                    currentResult.setDistanceToSec(c.getInt(c.getColumnIndex("dist_to_sec")));
                    currentResult.setDistanceDelta(c.getInt(c.getColumnIndex("delta_dis")));
                    currentResult.setBetaAverage(c.getDouble(c.getColumnIndex("beta_average")));
                    currentResult.setBetaI(c.getDouble(c.getColumnIndex("beta_i")));
                    currentResult.setBetaDelta(c.getDouble(c.getColumnIndex("beta_delta")));
                    currentResult.setSectionID(c.getInt(c.getColumnIndex("r_s_id")));
                    currentResult.setBuildingID(c.getInt(c.getColumnIndex("r_b_id")));
                    currentResult.setMeasureID(c.getInt(c.getColumnIndex("r_m_id")));

                    localDBBuilding.addResultData(currentResult);
                } while (c.moveToNext());
                Log.d(LOG_TAG, "Building loaded from inner storage. id = " + localDBBuilding.getId()
                        + " config = " + localDBBuilding.getConfig());
            }
        } else {
            Log.d(LOG_TAG, "Cursor is null");
        }

        //return null;
        //reportBuilding.getSections().sort(Comparator.comparing(Section::getId));
        //reportBuilding.getMeasurements().sort(Comparator.comparing(Measurement::getId));
        //reportBuilding.getResults().sort(Comparator.comparing(Result::getId));
        return localDBBuilding;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Integer, Building> getBuildingMap(String name, String address) {
        Log.d(LOG_TAG, "getBuildingMap started");
        Map<Integer, Building> localDBBuildingMap = new HashMap<>();
        int building_id = 0;
        String query = "select * from buildings " +
                "where (b_name like ? and b_address like ?)";
        Cursor mapCursor = localDB.rawQuery(query,
                new String[] {"%"+ name +"%", "%" + address + "%"});
        if (mapCursor != null) {
            if (mapCursor.moveToFirst()) {
                mapCursor.moveToFirst();
                do {
                    building_id = mapCursor.getInt(mapCursor.getColumnIndex("b_id"));
                    localDBBuildingMap.put(building_id, getBuilding(building_id));
                    Log.d(LOG_TAG, "building_id = " + building_id);
                } while (mapCursor.moveToNext());
            }
        }
        Log.d(LOG_TAG, "map size = " + localDBBuildingMap.size());
        return localDBBuildingMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(Building building) {
        Log.d(LOG_TAG, "Start to insert building ot Local Database");
        //building.getSections().sort(Comparator.comparing(Section::getId));
        long id_b = 0, id_s = 0, id_m = 0;
        c = localDB.query("buildings", new String[]{"b_id"}, "b_id = ?",
                new String[] {String.valueOf(building.getId())}, null, null, null);
        Log.d(LOG_TAG, "cursor getCount() = " + c.getCount() + " ...insert");
        if(c.getCount() == 0) {
            id_b = localDB.insert("buildings", null, getBuildingCV(building, MODE_INSERT));
            for (int i = 0; i < building.getNumberOfSections(); i++) {
                Section section = building.getSection(i + 1);
                id_s += localDB.insert("sections", null, getSectionCV(section, MODE_INSERT));
            }
            int shift = 0;
            for (int side = 1; side < building.getConfig() + 1; side++) {
                for (int sec = 1; sec < building.getNumberOfSections() + 2; sec++) { // include top measure
                    Measurement measurement = building.getMeasurements().get(shift + sec - 1);
                    Result result = building.getResults().get(shift + sec - 1);

                    id_m += localDB.insert("measures", null, getMeasurementCV(measurement, MODE_INSERT));
                    localDB.insert("results", null, getResultCV(result, MODE_INSERT));
                }
                shift += building.getNumberOfSections() + 1;
            }
        Log.d(LOG_TAG, "id_b = " + id_b + "; id_s = " + id_s + "; id_m = " + id_m);
        } else {
            update(building);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insert(List<Building> buildings){
        for(int i = 0; i < buildings.size(); i++) {
            insert(buildings.get(i));
        }
    }

    public void update(List<Building> buildings){
        for(int i = 0; i < buildings.size(); i++) {
            update(buildings.get(i));
        }
    }

    public void delete(Building building) {

    }

    public void delete(List<Building> buildings) {

    }

    public void closeDB() {
        dbHelper.close();
    }

    private ContentValues getBuildingCV(Building building, int mode) {
        ContentValues cv = new ContentValues();
        switch (mode) {
            case MODE_INSERT:
                cv.put("b_id", building.getId());
            case MODE_UPDATE:
                cv.put("b_name", building.getName());
                cv.put("b_address", building.getAddress());
                cv.put("b_type", building.getType().toString());
                cv.put("b_config", building.getConfig());
                cv.put("b_numberofsecs", building.getNumberOfSections());
                cv.put("b_height", building.getHeight());
                cv.put("b_startlevel", building.getStartLevel());
                cv.put("b_username", building.getUserName());
                cv.put("b_creationdate", dateFormat.format(building.getCreationDate()));
        }

        return cv;
    }

    private ContentValues getSectionCV(Section section, int mode) {
        ContentValues cv = new ContentValues();
        //cv.clear();
        switch (mode) {
            case MODE_INSERT:
                cv.put("s_id", section.getId());
                cv.put("s_b_id", section.getBuilding_id());
            case MODE_UPDATE:
                cv.put("s_number", section.getNumber());
                cv.put("s_widthbottom", section.getWidthBottom());
                cv.put("s_widthtop", section.getWidthTop());
                cv.put("s_height", section.getHeight());
                cv.put("s_level", section.getLevel());
                cv.put("s_name", section.getName());
        }
        return cv;
    }

    private ContentValues getMeasurementCV(Measurement measurement, int mode) {
        //cv.clear();
        Log.d(LOG_TAG, "Prepare to store in local DB. Measurement left angle = " + measurement.getLeftAngle() + "\n"
        + "Measurement right angle = " + measurement.getRightAngle());
        ContentValues cv = new ContentValues();
        switch (mode) {
            case MODE_INSERT:
                cv.put("m_id", measurement.getId());
                cv.put("m_b_id", measurement.getBuildingId());
                cv.put("m_s_id", measurement.getSectionId());

            case MODE_UPDATE:
                cv.put("m_circle", measurement.getCircle().toString());
                cv.put("m_leftangle", measurement.getLeftAngle());
                cv.put("m_rightangle", measurement.getRightAngle());
                cv.put("m_theoheight", measurement.getTheoHeight());
                cv.put("m_distance", measurement.getDistance());
                cv.put("m_date", dateFormat.format(measurement.getDate()));
                cv.put("m_contractor", measurement.getContractor());
                cv.put("m_sideazimuth", measurement.getAzimuth());
                cv.put("m_number", measurement.getNumber());
                cv.put("m_side", measurement.getSide());
                cv.put("m_sectionnumber", measurement.getSectionNumber());
                cv.put("m_baseortop", measurement.getBaseOrTop().toString());
        }
        return cv;
    }

    private ContentValues getResultCV(Result result, int mode) {
        ContentValues cv = new ContentValues();
        switch (mode) {
            case MODE_INSERT:
                cv.put("r_id", result.getId());
                cv.put("r_s_id", result.getSectionID());
                cv.put("r_b_id", result.getBuildingID());
                cv.put("r_m_id", result.getMeasureID());
            case MODE_UPDATE:
                cv.put("averageKL", result.getAverageKL());
                cv.put("averageKR", result.getAverageKR());
                cv.put("averageKLKR", result.getAverageKLKR());
                cv.put("shift_deg", result.getShiftDegree());
                cv.put("shift_mm", result.getShiftMm());
                cv.put("tan_alfa", result.getTanAlfa());
                cv.put("dist_to_sec", result.getDistanceToSec());
                cv.put("delta_dis", result.getDistanceToSec());
                cv.put("beta_average", result.getBetaAverage());
                cv.put("beta_i", result.getBetaI());
                cv.put("beta_delta", result.getBetaDelta());
        }
        return cv;
    }
}
