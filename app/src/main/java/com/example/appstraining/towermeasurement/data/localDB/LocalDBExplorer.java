package com.example.appstraining.towermeasurement.data.localDB;

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
import com.example.appstraining.towermeasurement.util.DegreeNumericConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    Cursor cursor;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat dateFormatMeas = new SimpleDateFormat("yyyy-MM-dd");

    private final String BUILDINGS_TAB = "buildings";
    private final String SECTIONS_TAB = "sections";
    private final String MEASUREMENTS_TAB = "measures";
    private final String RESULTS_TAB = "results";

    public LocalDBExplorer(Context context) {
        mContext = context;
        dbHelper = new DBHelper(mContext, "localDB", null, 1);
        localDB = dbHelper.getWritableDatabase();
    }

    public void update(Building building) {
        if (!localDB.isOpen()) localDB = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Starting update building in SQLite id = " + building.getId() +
                "Number of Sections = " + building.getNumberOfSections());
        long b_id = -1, s_id = -1, m_id = -1;                        // excluded for update action
        int updCountB = 0, updCountS = 0, updCountM = 0, updCountR = 0;
        updCountB = localDB.update("buildings", getBuildingCV(building, MODE_UPDATE),
                "b_id = ?", new String[] { String.valueOf(building.getId())});

        Log.d(LOG_TAG, "update building = " + updCountB);

        for (int i = 0; i < building.getNumberOfSections(); i++) {
            Section section = building.getSection(i + 1);
            String[] params = {String.valueOf(section.getId()), String.valueOf(section.getBuilding_id())};
            updCountS = localDB.update("sections", getSectionCV(section, b_id, MODE_UPDATE),
                    "(s_id = ? and s_b_id = ?)", params);
            Log.d(LOG_TAG, "update section = " + updCountS);
        }
        int shift = 0;
        // changed side < building.getConfig() + 1
        for (int side = 1; side < 3; side++) { // changed fo 2 sides
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

                updCountM = localDB.update("measures", getMeasurementCV(measurement, b_id, s_id, MODE_UPDATE),
                        "(m_id = ? and m_s_id = ? and m_b_id = ?)", mParams);
                Log.d(LOG_TAG, "updated measurement = " + updCountM);

                Log.d(LOG_TAG, "Update result ... r_id = " + result.getId() +
                        "; r_s_id = " + result.getSectionID() +
                        "; r_b_id = " + result.getBuildingID()
                );
                updCountR = localDB.update("results", getResultCV(result, b_id, s_id, m_id, MODE_UPDATE),
                        "r_id = ? and r_m_id = ?", rParams);
                Log.d(LOG_TAG, "updated result = " + updCountR);
            }
            shift = shift + building.getNumberOfSections() + 1;
        }
        localDB.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Building get(Long id, boolean isSingleGet) {
        Log.d(LOG_TAG, "Load building from local DB started id = " + id);
        if (!localDB.isOpen()) localDB = dbHelper.getReadableDatabase();
        boolean isBuildingDataAdded = false;
        Set<Integer> addedSectionIds = new HashSet<>();
        int currentSectionId = 0;
        Building localDBBuilding = new Building();

        ArrayList<Section> reportSections = new ArrayList<>();
        ArrayList<Measurement> reportMeasurements = new ArrayList<>();
        ArrayList<Result> reportResults = new ArrayList<>();
        Log.d(LOG_TAG, "Is database open  : " + localDB.isOpen());
        String query = "select * from measures " +
                "inner join results on m_id = r_id " +
                "inner join sections on m_s_id = s_id " +
                "inner join buildings on m_b_id = b_id " +
                "where m_b_id = ?;";
        cursor = localDB.rawQuery(query,new String[] {String.valueOf(id)});
        /*cursor = localDB.rawQuery(query, new String[] {"2"});*/

        Log.d(LOG_TAG, "Cursor count " + cursor.getCount());
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                do{

                    if(!isBuildingDataAdded) {
                        try {
                            localDBBuilding.setId(id);
                            localDBBuilding.setName(cursor.getString(cursor.getColumnIndex("b_name")));
                            localDBBuilding.setAddress(cursor.getString(cursor.getColumnIndex("b_address")));
                            localDBBuilding.setType(BuildingType.valueOf(cursor.getString(cursor.getColumnIndex("b_type"))));
                            localDBBuilding.setConfig(cursor.getInt(cursor.getColumnIndex("b_config")));
                            localDBBuilding.setNumberOfSections(cursor.getInt(cursor.getColumnIndex("b_numberofsecs")));
                            localDBBuilding.setHeight(cursor.getInt(cursor.getColumnIndex("b_height")));
                            localDBBuilding.setStartLevel(cursor.getInt(cursor.getColumnIndex("b_startlevel")));
                            localDBBuilding.setUserName(cursor.getString(cursor.getColumnIndex("b_username")));
                            localDBBuilding.setCreationDate(new java.sql.Date(
                                    dateFormat.parse(cursor.getString(cursor.getColumnIndex("b_creationdate")))
                                            .getTime())
                            );
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        isBuildingDataAdded = true;
                    }

                    currentSectionId = cursor.getInt(cursor.getColumnIndex("s_id"));
                    if (!addedSectionIds.contains(currentSectionId)) {
                        Section currentSection = new Section();
                        currentSection.setId(currentSectionId);
                        currentSection.setNumber(cursor.getInt(cursor.getColumnIndex("s_number")));
                        currentSection.setWidthBottom(cursor.getInt(cursor.getColumnIndex("s_widthbottom")));
                        currentSection.setWidthTop(cursor.getInt(cursor.getColumnIndex("s_widthtop")));
                        currentSection.setHeight(cursor.getInt(cursor.getColumnIndex("s_height")));
                        currentSection.setName(cursor.getString(cursor.getColumnIndex("s_name")));
                        currentSection.setLevel(cursor.getInt(cursor.getColumnIndex("s_level")));
                        currentSection.setBuilding_id(cursor.getLong(cursor.getColumnIndex("s_b_id")));

                        localDBBuilding.addSection(currentSection);
                        addedSectionIds.add(currentSectionId);
                    }

                    Measurement currentMeasurement = new Measurement();
                    try {
                        currentMeasurement.setId(cursor.getInt(cursor.getColumnIndex("m_id")));
                        currentMeasurement.setNumber(cursor.getInt(cursor.getColumnIndex("m_number")));
                        currentMeasurement.setCircle(CircleTheo.valueOf(
                                cursor.getString(cursor.getColumnIndex("m_circle"))));
                        currentMeasurement.setLeftAngle(cursor.getDouble(cursor.getColumnIndex("m_leftangle")));
                        currentMeasurement.setRightAngle(cursor.getDouble(cursor.getColumnIndex("m_rightangle")));
                        currentMeasurement.setTheoHeight(cursor.getInt(cursor.getColumnIndex("m_theoheight")));
                        currentMeasurement.setDistance(cursor.getInt(cursor.getColumnIndex("m_distance")));
                        currentMeasurement.setSectionNumber(cursor.getInt(cursor.getColumnIndex("m_sectionnumber")));
                        currentMeasurement.setSide(cursor.getInt(cursor.getColumnIndex("m_side")));
                        currentMeasurement.setBaseOrTop(BaseOrTop.valueOf(
                                cursor.getString(cursor.getColumnIndex("m_baseortop"))));
                        currentMeasurement.setDate(new java.sql.Date(
                                dateFormatMeas.parse(cursor.getString(cursor.getColumnIndex("m_date")))
                                        .getTime()));
                        currentMeasurement.setContractor(cursor.getString(cursor.getColumnIndex("m_contractor")));
                        currentMeasurement.setAzimuth(cursor.getInt(cursor.getColumnIndex("m_sideazimuth")));
                        currentMeasurement.setSectionId(cursor.getInt(cursor.getColumnIndex("m_s_id")));
                        currentMeasurement.setBuildingId(cursor.getInt(cursor.getColumnIndex("m_b_id")));

                        localDBBuilding.addMeasurement(currentMeasurement);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Result currentResult = new Result();
                    currentResult.setId(cursor.getInt(cursor.getColumnIndex("r_id")));
                    currentResult.setAverageKL(cursor.getDouble(cursor.getColumnIndex("averageKL")));
                    currentResult.setAverageKR(cursor.getDouble(cursor.getColumnIndex("averageKR")));
                    currentResult.setAverageKLKR(cursor.getDouble(cursor.getColumnIndex("averageKLKR")));
                    currentResult.setShiftDegree(cursor.getDouble(cursor.getColumnIndex("shift_deg")));
                    currentResult.setShiftMm(cursor.getInt(cursor.getColumnIndex("shift_mm")));
                    currentResult.setTanAlfa(cursor.getDouble(cursor.getColumnIndex("tan_alfa")));
                    currentResult.setDistanceToSec(cursor.getInt(cursor.getColumnIndex("dist_to_sec")));
                    currentResult.setDistanceDelta(cursor.getInt(cursor.getColumnIndex("delta_dis")));
                    currentResult.setBetaAverageLeft(cursor.getDouble(cursor.getColumnIndex("beta_average_left")));
                    currentResult.setBetaAverageRight(cursor.getDouble(cursor.getColumnIndex("beta_average_right")));

                    currentResult.setBetaI(cursor.getDouble(cursor.getColumnIndex("beta_i")));
                    currentResult.setBetaDelta(cursor.getInt(cursor.getColumnIndex("beta_delta")));
                    currentResult.setSectionID(cursor.getInt(cursor.getColumnIndex("r_s_id")));
                    currentResult.setBuildingID(cursor.getInt(cursor.getColumnIndex("r_b_id")));
                    currentResult.setMeasureID(cursor.getInt(cursor.getColumnIndex("r_m_id")));

                    localDBBuilding.addResultData(currentResult);
                } while (cursor.moveToNext());

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
        if (isSingleGet) localDB.close();
        return localDBBuilding;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Long, Building> getBuildingMap(String... searchParameters) {
        Log.d(LOG_TAG, "getBuildingMap started");

        Map<Long, Building> localDBBuildingMap = new HashMap<>();
        long building_id = 0;
        String query = "select * from buildings " +
                "where (b_name like ? and b_address like ?)";
        Cursor mapCursor = localDB.rawQuery(query,
                new String[] {"%"+ searchParameters[0] +"%", "%" + searchParameters[1] + "%"});
        if (mapCursor != null) {
            if (mapCursor.moveToFirst()) {
                mapCursor.moveToFirst();
                do {
                    building_id = mapCursor.getInt(mapCursor.getColumnIndex("b_id"));
                    localDBBuildingMap.put(building_id, get(building_id, false));
                    Log.d(LOG_TAG, "building_id = " + building_id);
                } while (mapCursor.moveToNext());
            }
        }
        Log.d(LOG_TAG, "map size = " + localDBBuildingMap.size());
        localDB.close();
        return localDBBuildingMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long save(Building building) {

        if (!localDB.isOpen()) localDB = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Start to insert building ot Local Database");
        Log.d(LOG_TAG, "Building id = " + building.getId());
        //building.getSections().sort(Comparator.comparing(Section::getId));
        long b_id = 0, s_ids[] = new long[building.getNumberOfSections()], m_ids[] = new long[building.getMeasurements().size()];
        //long lastId = localDB.rawQuery("SELECT MAX(b_id) FROM buildings", null).getInt(0);
        Log.d(LOG_TAG, "building is new:" + building.isNew());
        if (!building.isNew()) update(building);

        Log.d(LOG_TAG, "localDB is open: " + localDB.isOpen());
        cursor = localDB.query("buildings", new String[]{"b_id"}, "b_id = ?",
                new String[] {String.valueOf(building.getId())}, null, null, null);
        Log.d(LOG_TAG, "cursor getCount() = " + cursor.getCount() + " ...insert");
        if(cursor.getCount() == 0) {
            b_id = localDB.insert("buildings", null, getBuildingCV(building, MODE_INSERT));
            /*for (int i = 0; i < building.getNumberOfSections(); i++) {
                Section section = building.getSection(i + 1);
                s_ids[i] = localDB.insert("sections", null, getSectionCV(section, b_id, MODE_INSERT));
            }*/

            long finalB_id = b_id;
            building.getSections().forEach(section -> {
                s_ids[section.getNumber() - 1] = localDB.insert("sections", null, getSectionCV(section, finalB_id, MODE_INSERT));
            });

            building.getMeasurements().forEach(measurement -> {
                m_ids[measurement.getNumber() - 1] = localDB.insert(MEASUREMENTS_TAB, null,
                        getMeasurementCV(measurement, finalB_id, s_ids[measurement.getSectionNumber() - 1], MODE_INSERT));
                localDB.insert(RESULTS_TAB, null, getResultCV(
                        building.getResults().get(measurement.getNumber() - 1),
                        finalB_id,
                        s_ids[measurement.getSectionNumber() - 1],
                        m_ids[measurement.getNumber() - 1],
                        MODE_INSERT));
            });

            /*int shift = 0; // сдвиг
            for (int side = 1; side < building.getConfig() + 1; side++) {
                for (int sec = 1; sec < building.getNumberOfSections() + 2; sec++) { // include top measure
                    Measurement measurement = building.getMeasurements().get(shift + sec - 1);
                    Result result = building.getResults().get(shift + sec - 1);

                    m_ids[shift + sec - 1] = localDB.insert("measures", null, getMeasurementCV(measurement, b_id, s_ids[sec - 1], MODE_INSERT));
                    localDB.insert("results", null, getResultCV(result, b_id, s_id, m_id, MODE_INSERT));
                }
                shift += building.getNumberOfSections() + 1;
            }*/

        Log.d(LOG_TAG, "building successfully saved id_b = " + b_id);
        } else {
            update(building);
        }
        localDB.close();
        return b_id;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public long[] save(List<Building> buildings){
        long[] savedIds = new long[buildings.size()];
        Log.d(LOG_TAG, "buildings size = " + buildings.size());
        Log.d(LOG_TAG, "Building from buildings " + buildings.get(0));
        for(int i = 0; i < buildings.size(); i++) {
            if(buildings.get(i) != null) {
                Log.d(LOG_TAG, "buildings id, name to save " + buildings.get(i).getId() + ", " + buildings.get(i).getName());
                savedIds[i] = save(buildings.get(i));
            }
        }
        return savedIds;
    }

    public void update(List<Building> buildings){
        for(int i = 0; i < buildings.size(); i++) {
            update(buildings.get(i));
        }
    }

    public void delete(Building building) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int delete(Long[] ids) {
        int count;
        String idsArgs = Arrays.toString(ids);
        idsArgs = idsArgs.substring(1,idsArgs.length() - 1);
        Log.d(LOG_TAG, "idsArgs: " + idsArgs);
        if (!localDB.isOpen()) localDB = dbHelper.getWritableDatabase();
        count = localDB.delete(BUILDINGS_TAB, String.format("b_id in (%s)", idsArgs), null);
        localDB.delete(SECTIONS_TAB, String.format("s_b_id in (%s)", idsArgs), null);
        localDB.delete(MEASUREMENTS_TAB, String.format("m_b_id in (%s)", idsArgs), null);
        localDB.delete(RESULTS_TAB, String.format("r_b_id in (%s)", idsArgs), null);
        localDB.close();
        return count;
    }

    public void closeDB() {
        dbHelper.close();
    }

    private ContentValues getBuildingCV(Building building, int mode) {
        ContentValues cv = new ContentValues();
        switch (mode) {
            case MODE_INSERT:
                //cv.put("b_id", building.getId());
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

    private ContentValues getSectionCV(Section section, long b_id, int mode) {
        ContentValues cv = new ContentValues();
        //cv.clear();
        switch (mode) {
            case MODE_INSERT:
                cv.put("s_b_id", b_id);
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

    private ContentValues getMeasurementCV(Measurement measurement, long b_id, long s_id, int mode) {
        //cv.clear();
        Log.d(LOG_TAG, "Prepare to store in local DB. Measurement left angle = " + measurement.getLeftAngle() + "\n"
        + "Measurement right angle = " + measurement.getRightAngle());
        ContentValues cv = new ContentValues();
        switch (mode) {
            case MODE_INSERT:
                cv.put("m_b_id", b_id);
                cv.put("m_s_id", s_id);

            case MODE_UPDATE:
                cv.put("m_circle", measurement.getCircle().toString());
                cv.put("m_leftangle", measurement.getLeftAngle());
                cv.put("m_rightangle", measurement.getRightAngle());
                cv.put("m_theoheight", measurement.getTheoHeight());
                cv.put("m_distance", measurement.getDistance());
                cv.put("m_date", measurement.getDate().toString());
                cv.put("m_contractor", measurement.getContractor());
                cv.put("m_sideazimuth", measurement.getAzimuth());
                cv.put("m_number", measurement.getNumber());
                cv.put("m_side", measurement.getSide());
                cv.put("m_sectionnumber", measurement.getSectionNumber());
                cv.put("m_baseortop", measurement.getBaseOrTop().toString());
        }
        return cv;
    }

    private ContentValues getResultCV(Result result, long b_id, long s_id, long m_id, int mode) {
        ContentValues cv = new ContentValues();
        switch (mode) {
            case MODE_INSERT:
                cv.put("r_s_id", s_id);
                cv.put("r_b_id", b_id);
                cv.put("r_m_id", m_id);
            case MODE_UPDATE:
                cv.put("averageKL", result.getAverageKL());
                cv.put("averageKR", result.getAverageKR());
                cv.put("averageKLKR", result.getAverageKLKR());
                cv.put("shift_deg", result.getShiftDegree());
                cv.put("shift_mm", result.getShiftMm());
                cv.put("tan_alfa", result.getTanAlfa());
                cv.put("dist_to_sec", result.getDistanceToSec());
                cv.put("delta_dis", result.getDistanceToSec());
                cv.put("beta_average_left", result.getBetaAverageLeft());
                cv.put("beta_average_right", result.getBetaAverageRight());
                cv.put("beta_i", result.getBetaI());
                cv.put("beta_delta", result.getBetaDelta());
        }
        return cv;
    }
}
