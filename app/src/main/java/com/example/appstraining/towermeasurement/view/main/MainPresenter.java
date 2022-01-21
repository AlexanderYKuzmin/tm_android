package com.example.appstraining.towermeasurement.view.main;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.appstraining.towermeasurement.model.openGL.DrawPreparation;
import com.example.appstraining.towermeasurement.data.localDB.LocalDBExplorer;
import com.example.appstraining.towermeasurement.data.network.RuVdsServer;
import com.example.appstraining.towermeasurement.util.AppPropertyHandler;
import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.BuildingType;
import com.example.appstraining.towermeasurement.model.CircleTheo;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.RequestCode;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.model.Section;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;

import okhttp3.OkHttpClient;

public class MainPresenter implements LifecycleObserver {
    private final String LOG_TAG = "mainPresenter.class";
    private final int TOWER_EDGES = 101;
    private final int TOWER_FLATS = 102;
    public static final int RESULT_SECTION_VALUES_OK = 1;

    MainViewInterface mMainActivity;
    Context context;
    Properties properties;

    public boolean isBuildingMapPrepared = false;
    public boolean isBuildingPrepared = false;
    public static boolean isMeasuresChanged = false;

    private final LocalDBExplorer dbExplorer;
    DrawPreparation drawPreparation;

    private Building building;
    private String[] searchParameters;
    Map<Integer, Building> buildingMap;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    // fragments handle


    public MainPresenter(MainViewInterface mainActivity, Context context){
        mMainActivity = mainActivity;
        this.context = context;
        dbExplorer = new LocalDBExplorer(context);
        drawPreparation = new DrawPreparation(this);
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    //@OnLifecycleEvent(Lifecycle.Event.ON_START)
    /*@RequiresApi(api = Build.VERSION_CODES.N)
    public void setBuildingOnStart(int id) {
        getFromLocalDB(id);
    }*/

    public Building getBuilding(){
        return building;
    }

    public void getSectionListAdapter() {
        //Intent intent = new Intent(context, SectionsViewPager.class);
       // mMainActivity.start

    }

    public void setSearchParameters(String... parameters) { // from search dialog fragment
        searchParameters = parameters;
    }

    public void setBuildingMap(Map<Integer, Building> buildingMap) {
        this.buildingMap = buildingMap;
    }

    public void createBuilding(String... params) {                  // for tab "new" menu
        int id = 0;
        String name = params[0];
        String address = params[1];
        //BuildingType type = BuildingType.valueOf(params[2]);
        BuildingType type = BuildingType.getBuildingType(params[2]);
        int config = Integer.parseInt(params[3]);
        int numberOfSections = Integer.parseInt(params[4]);
        int height = Integer.parseInt(params[5]);
        int startLevel = 0;
        String userName = "noName";
        java.sql.Date creationDate = new Date(new java.util.Date().getTime());

        String contractor = "noName";
        java.sql.Date measureDate = new Date(new java.util.Date().getTime());

        building = new Building(id, name, address, type, config, numberOfSections,
                height, startLevel, userName, creationDate); // parameters from activity
        Log.d(LOG_TAG, "Building created. Name : " + building.getName()
                + " Address : " + building.getAddress()
                + " Creation date : " + dateFormat.format(building.getCreationDate()));
        // set sections
        int sectionId = 0;
        int sectionNumber;
        int widthBottom = 0, widthTop = 0;
        int sectionHeight  = 0;
        int level = 0;

        // ******** Add sections ************
        for(int i = 0; i < numberOfSections; i++) {
            sectionNumber = i + 1;
            building.addSection(new Section(0, sectionNumber, widthBottom, widthTop,
                    sectionHeight, level, name, id));
            System.out.println("******" + building.getSection(i+1).toString());
        }
        Log.d(LOG_TAG, "Sections created. Example Section #1 " + building.getSection(1).getNumber());

        // ******** Add measurements **********
        int measureNumber = 0;
        for(int side = 1; side < 4; side++) {
            for(int sectionNum = 1; sectionNum < building.getNumberOfSections() + 1; sectionNum++) {
                measureNumber++;
                building.addMeasurement(new Measurement(0, measureNumber, side, CircleTheo.KL,
                        0.0, 0.0, 0, 0,
                        sectionNum, BaseOrTop.BASE, 0, 0,
                        measureDate, contractor, 0));
                Log.d(LOG_TAG, "measurement added: id = "
                        + building.getMeasurement(1, 1, BaseOrTop.BASE));

                if(sectionNum == building.getNumberOfSections()) {
                    measureNumber++;
                    building.addMeasurement(new Measurement(0, measureNumber, side, CircleTheo.KL,
                            0.0, 0.0, 0, 0,
                            sectionNum, BaseOrTop.TOP, 0, 0,
                            measureDate, contractor, 0));
                }
            }
        }

        // ******** Add results **********
        //for(int side = 1; side < building.getConfig() + 1; side++) {
        for(int side = 1; side < 4; side++) {
            for (int sectionNum = 1; sectionNum < building.getNumberOfSections() + 2; sectionNum++) {           // according with top of section
                building.addResultData(new Result(0, 0.0, 0.0, 0.0, 0.0,
                0, 0.0, 0, 0, 0.0, 0.0, 0.0,
                0, 0, 0));
                Log.d(LOG_TAG, "result added :" );
            }
        }
        Log.d(LOG_TAG, "Building created. building_id = " + building.getId() + "\n"
                + "measurements size = " + building.getMeasurements().size() + "\n"
                + "results size = " + building.getResults().size()
        );
    }

    public void getBuildingFromFile(){ // get rid of it

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public synchronized void loadBuilding(int building_id){
        Log.d(LOG_TAG, "Load Building started! Building ID = " + building_id);
        OkHttpClient okHttpClient = new OkHttpClient();
        RuVdsServer.GET(this, okHttpClient, RequestCode.GET,
                building_id, null, null);
        while (!isBuildingPrepared) {
            Log.d(LOG_TAG, "Freezing...");
            try {

                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(building == null) {
            Log.d(LOG_TAG, "Loading failed");
        } else {
            Log.d(LOG_TAG, "Building is loaded. ID = " + building.getId()
                    + "; Object name = " + building.getName()
                    + "; Section[5] level = " + building.getSection(3).getLevel()
                    + "; Section[6] level = " + building.getSection(4).getLevel()
            );
            isBuildingPrepared = false; // building loaded
        }
        building.getSections().sort(Comparator.comparing(Section::getId));
        building.getMeasurements().sort(Comparator.comparing(Measurement::getId));
        building.getResults().sort(Comparator.comparing(Result::getId));
    }

    //*** Mount building on MainActivity ***
    public void mountBuilding(MainActivityMode activityMode) {
        //this.building = building;
        //mMainActivity.updateSectionList(getSections());
        //mMainActivity.removeAnimatedModel();
        mMainActivity.updateView(activityMode);

        /*mMainActivity.updateView(context.getString(R.string.title_view_selected)
                , String.valueOf(building.getId())
                , building.getName(), building.getAddress()
                , String.valueOf(building.getType()), String.valueOf(building.getConfig())
                , String.valueOf(building.getHeight()), String.valueOf(building.getNumberOfSections())
        );*/
        Bundle bundle = new Bundle();
        bundle.putFloatArray("towerflats", drawPreparation.getDrawingSequence(TOWER_FLATS));
        bundle.putFloatArray("toweredges", drawPreparation.getDrawingSequence(TOWER_EDGES));
        bundle.putInt("config", building.getConfig());
        bundle.putInt("levels", building.getNumberOfSections() + 1);
        //mMainActivity.showAnimatedModel(bundle);

    }

    public synchronized Map<Integer, Building> getBuildingMap(String objectName, String address) {
        Log.d(LOG_TAG, "Getting MAP started!");
        OkHttpClient okHttpClient = new OkHttpClient();
        RuVdsServer.GET(this, okHttpClient, RequestCode.SEARCH,
                0, objectName, address);
        while (!isBuildingMapPrepared) {
            Log.d(LOG_TAG, "Freezing...");
            try{
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //notify();
        Log.d(LOG_TAG, "Unfreezed. BuildingMap is: " + buildingMap
                + "\n"
                + "Building_id = 4: " + buildingMap.get(4).getId() + " : "
                + buildingMap.get(4).getAddress() + " : "
                + buildingMap.get(4).getName()
        );
        if(buildingMap != null) {
            isBuildingMapPrepared = false;
            return buildingMap;
        }
        return null;
    }

    public ArrayList<Section> getSections() {
        return building.getSections();
    }

    public int[] getLevels() {
        int[] levels = new int[getSections().size() + 1];
        levels[0] = 0;
        for (int i = 1; i < getSections().size(); i++) {
            levels[i] = levels[i - 1] + getSections().get(i).getHeight();
        }
        levels[levels.length - 1] = getBuilding().getHeight();
        return levels;
    }

    public ArrayList<Measurement> getMeasurements() {
        return building.getMeasurements();
    }

    public ArrayList<Result> getResults() {
        return building.getResults();
    }

    public void updateSection(int secNum, String widthBottom, String widthTop, String height){
        int wb = Integer.parseInt(widthBottom);
        int wt = Integer.parseInt(widthTop);
        int h = Integer.parseInt(height);
        building.getSection(secNum).setWidthBottom(wb);
        building.getSection(secNum).setWidthTop(wt);
        building.getSection(secNum).setHeight(h);
        Log.d(LOG_TAG, "Section #" + secNum + " is updated!");
        Log.d(LOG_TAG, "Height = " + building.getSection(secNum).getHeight());

        mMainActivity.updateSectionList(getSections());
    }

    public void setMeasurements(ArrayList<Measurement> measurements) {
        building.setMeasurements(measurements);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setResults() {
        double startAverageKL = 0, startAverageKR = 0;
        double startAverageKLKR = 0;
        double averageKL = 0, averageKR = 0;
        double averageKLKR = 0;
        double shiftDegree;
        int shiftMm;
        double tanAlfa;
        int distanceToPoint;
        int distanceDelta;
        double betaAverage, betaI, betaDelta;

        ArrayList<Measurement> locUsingMeasurements = getMeasurements();
        locUsingMeasurements.sort(Comparator.comparing(Measurement::getId));
        ArrayList<Result> locUsingResults = getResults();
        locUsingResults.sort(Comparator.comparing(Result::getMeasureID));

        if(isMeasuresChanged) {
            // mathematics will be here!!
            for(int i = 0; i < locUsingMeasurements.size(); i++) {
                int sectionNumber = locUsingMeasurements.get(i).getSectionNumber();


                if(sectionNumber > 1) {
                    //distanceToPoint = getMeasures().get(i).getDistance();
                    averageKL =
                            (getMeasurements().get(i).getLeftAngle() + getMeasurements().get(i).getRightAngle()) / 2;
                    averageKR = averageKL;
                    averageKLKR = averageKL;
                    distanceToPoint = getDistanceToPoint(locUsingMeasurements.get(i));
                    shiftDegree = averageKLKR - startAverageKLKR;
                    tanAlfa = Math.tan(Math.PI*shiftDegree/180);
                    shiftMm = (int) (tanAlfa * distanceToPoint);
                    //betaAverageLeft = locUsingMeasurements.get(i).getLeftAngle(); // average beta between KL & KR
                    //betaI =

                } else {
                    averageKL =
                            (getMeasurements().get(i).getLeftAngle() + getMeasurements().get(i).getRightAngle()) / 2;
                    startAverageKL = averageKL;
                    startAverageKR = startAverageKL;
                    averageKLKR = averageKL;
                    startAverageKLKR = averageKLKR;
                    distanceToPoint = getDistanceToPoint(locUsingMeasurements.get(i));
                    shiftDegree = 0;
                    tanAlfa = 0;
                    shiftMm = 0;
                }
                locUsingResults.get(i).updateResult(averageKL, averageKR, averageKLKR,
                        shiftDegree, shiftMm, tanAlfa, distanceToPoint, 0);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getLastInputObject() {
        Log.d(LOG_TAG, "get last input object");
        int id = 2;
        try {
            String idStr = AppPropertyHandler.getProperty("id", context);
            Log.d(LOG_TAG, "id string = " + idStr);
            if(idStr != null) id = Integer.parseInt(idStr);
            Log.d(LOG_TAG, "id = " + id);
        } finally {
            if(id > 0) {
                getFromLocalDB(id);
                mountBuilding(MainActivityMode.LAST_LOADED_OBJECT);
            }
        }
    }

    public String registerObject() {
        OkHttpClient okHttpClient = new OkHttpClient();
        return  RuVdsServer.POST(okHttpClient, building, RequestCode.REGISTER);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void setLastInputObject() {                                            // save last edit object id to property
        //new FileLoader(context).saveBuildingToFile(building);
        Log.d(LOG_TAG, "Method quit started set id = " + building.getId());

        String idStr = building.getId() > 0 ? String.valueOf(building.getId()) : "1";

        try {
            AppPropertyHandler.setProperty("id", idStr, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int checkSectionValues(int secNum) {
        Section section = getSections().get(secNum - 1);
        if ( section.getWidthBottom() > 0 && section.getWidthTop() > 0 && section.getHeight() > 0) {
            return RESULT_SECTION_VALUES_OK;
        }
        return -1;
    }

    private int getDistanceToPoint(Measurement measurement){
        Section section = building.getSection(measurement.getSectionNumber());
        //Section prevSection;
        int theoDistance = measurement.getDistance();
        int theoHeight = measurement.getTheoHeight();
        //int buildingStartLevel = building.getStartLevel(); // gonna playing with theoheight according building startlevel
        //double r1; // previous section inner circle radius
        double r2; // current section inner circle radius
        double k = Math.sqrt(3) / 6; //
        //int a1; // previous section widthBottom
        //int a2; // current section widthBottom

        int distanceToPoint;

        //if(measurement.getSectionNumber() > 1) {
        if(measurement.getBaseOrTop() == BaseOrTop.BASE) {
            //prevSection = building.getSection(measurement.getSectionNumber() - 1);

            //r1 = prevSection.getWidthBottom() * k;
            r2 = section.getWidthBottom() * k; // this actually is distanceDelta;
            //distanceDelta = (int) (r1 - r2);
            distanceToPoint =(int) Math.sqrt(
                    Math.pow(((section.getNumber() - 1)*section.getHeight() - theoHeight), 2) +
                    Math.pow((theoDistance - r2), 2)
            );
        } else {
            r2 = section.getWidthTop() * k;
            distanceToPoint = (int) Math.sqrt(
                    Math.pow(((section.getNumber())*section.getHeight() - theoHeight), 2) +
                            Math.pow((theoDistance - r2), 2)
            );
        }
        return distanceToPoint;
    }


/*
    private float[] getVertices(){
        int numberOfSections = building.getNumberOfSections();
        int verticesInLevel = building.getConfig();
        int numberOfVertices = (3 * verticesInLevel * numberOfSections) + (3*verticesInLevel);
        float[] towerVertices = new float[numberOfVertices];
        float k = (float) (1 / Math.sqrt(3));
        float a, r, R;
        float h = -1.9f;

        Log.d(LOG_TAG, "Number of vertices = " + numberOfVertices);
       // Log.d(LOG_TAG, " getting coordinates for each level");
        for(int i = 0; i < numberOfSections * 9; i = i + 9) { // get coordinates for each level
            //Log.d(LOG_TAG, "level");
            a = (float)building.getSection(i / 9 + 1).getWidthBottom() / 10000;
            R = a * k;
            r = R / 2;
            //h0 = h;
            h = h + (float)building.getSection(i / 9 + 1).getHeight() / 25000;

            towerVertices[i] = -a / 2; // X1
            towerVertices[i + 1] = h; // Y1
            towerVertices[i + 2] = -r; // Z1

            towerVertices[i + 3] = a / 2; // X2
            towerVertices[i + 4] = h; // Y2
            towerVertices[i + 5] = -r; // Z2

            towerVertices[i + 6] = 0; // X3
            towerVertices[i + 7] = h; // Y3
            towerVertices[i + 8] = R; // Z3

            if(i == numberOfSections * 8) {
                a = (float)building.getSection(i / 9 + 1).getWidthTop() / 10000;
                R = a * k;
                r = R / 2;
                h = h + (float)building.getSection(i / 9 + 1).getHeight() / 25000;
                towerVertices[i + 9] = -a / 2; // X1
                towerVertices[i + 10] = h; // Y1
                towerVertices[i + 11] = -r; // Z1

                towerVertices[i + 12] = a / 2; // X2
                towerVertices[i + 13] = h; // Y2
                towerVertices[i + 14] = -r; // Z2

                towerVertices[i + 15] = 0; // X3
                towerVertices[i + 16] = h; // Y3
                towerVertices[i + 17] = R; // Z3
            }
        }
        for(float x : towerVertices) {
            //Log.d(LOG_TAG, " coordinates = " + x);
        }

        return towerVertices;
    }
*/
/*
    private float[] getDrawingSequence(int towerPart, float[] vertices) {
        float[] coordinatesSequence;
        int config = building.getConfig();
        switch (towerPart) {
            case TOWER_FLATS:
                int shift = 0;
                coordinatesSequence = new float[(vertices.length + (building.getNumberOfSections() + 1) * 3)];
                for (int i = 0; i < vertices.length; i = i + 9) {
                   // Log.d(LOG_TAG, "Flat " + i / 9);
                    coordinatesSequence[i + shift] = vertices [i];          // X
                    //Log.d(LOG_TAG, "Left side x = " + coordinatesSequence[i + shift]);
                    coordinatesSequence[i + 1 + shift] = vertices[i + 1];   // Y
                    //Log.d(LOG_TAG, "Left side y = " + coordinatesSequence[i + 1 + shift]);
                    coordinatesSequence[i + 2 + shift] = vertices[i + 2];   // Z
                    //Log.d(LOG_TAG, "Left side x = " + coordinatesSequence[i + shift]
                      //      + " y = " + coordinatesSequence[i + 1 + shift]
                        //    + " z = " + coordinatesSequence[i + 2 + shift]);

                    coordinatesSequence[i + 3 + shift] = vertices [i + 3];
                    coordinatesSequence[i + 4 + shift] = vertices[i + 4];
                    coordinatesSequence[i + 5 + shift] = vertices[i + 5];

                    coordinatesSequence[i + 6 + shift] = vertices [i + 6];
                    coordinatesSequence[i + 7 + shift] = vertices[i + 7];
                    coordinatesSequence[i + 8 + shift] = vertices[i + 8];

                    coordinatesSequence[i + 9 + shift] = vertices [i];
                    coordinatesSequence[i + 10 + shift] = vertices[i + 1];
                    coordinatesSequence[i + 11 + shift] = vertices[i + 2];
                    shift += 3;
                }
                return coordinatesSequence;
            case  TOWER_EDGES:
                int shift2 = 0;
                coordinatesSequence = new float[vertices.length];
                for (int j = 0; j < config; j++) {
                    shift = j * (building.getNumberOfSections() + 1) * 3;
                    shift2 = j * 3;
                    //Log.d(LOG_TAG, "Edge :" + j);
                    for (int i = 0, k = 0; i < coordinatesSequence.length / config; i = i + 3, k += 9) {

                        coordinatesSequence[i + shift] = vertices [k + shift2];
                        coordinatesSequence[i + 1 + shift] = vertices [k + 1 + shift2];
                        coordinatesSequence[i + 2 + shift] = vertices [k + 2 + shift2];
                        if(j == 0) {
                        //    Log.d(LOG_TAG, "leftside edge x = " + coordinatesSequence[i + shift]
                         //           + " y = " + coordinatesSequence[i + 1 + shift]
                          //          + " z = " + coordinatesSequence[i + 2 + shift]);
                        }
                    }
                }
                return coordinatesSequence;
        }
        return null;
    }
*/
    public void saveToLocalDB() {
        //LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        dbExplorer.update(building);
        dbExplorer.closeDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveToLocalDB(Integer... ids) {
        //LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        ArrayList<Building> chosenBuildings = new ArrayList<>();
        for(int i = 0; i < ids.length; i++) {
            chosenBuildings.add(buildingMap.get(ids[i]));
            Log.d(LOG_TAG, "Building added ot LocalDatabase id = " + ids[i]);
            //if(i == (ids.length - 1)) building = chosenBuildings.get(i);
        }
        dbExplorer.save(chosenBuildings);
        dbExplorer.closeDB();

        /*String queryB = "insert or replace into buildings " +
                "(b_id, b_name, b_address, b_type, b_config, " +
                "b_numberofsecs, b_height, b_startlev, b_username, b_creationdate)" +
                "values("
                + building.getId() + ", "
                + "'" + building.getName() + "', "
                + "'" + building.getAddress() + "', "
                + "'" + building.getType().toString() + "', "
                + building.getConfig() + ", "
                + building.getNumberOfSections() + ", "
                + building.getHeight() + ", "
                + building.getStartLevel() + ", "
                + "'" + building.getUserName() + ", "
                + "'" + dateFormat.format(building.getCreationDate()) + "')";
        localDB.execSQL(queryB);

        for(int i = 0; i < building.getNumberOfSections(); i++) {
            Section section = building.getSection(i + 1);

            String queryS = "insert or replace into sections (s_id, s_number, s_widthbottom, s_widthtop," +
                    "s_height, s_height, s_level, s_name, s_b_id)" +
                    "values("
                    + section.getId() + ", "
                    + section.getNumber() + ", "
                    + section.getWidthBottom() + ", "
                    + section.getWidthTop() + ", "
                    + section.getHeight() + ", "
                    + section.getLevel() + ", "
                    + "'" + section.getName() + "', "

                    + building.getId() + ")";
            localDB.execSQL(queryS);
        }

        *//*
                + "m_id integer primary key,"
                + "m_number integer,"
                + "m_circle text,"
                + "m_leftangle real,"
                + "m_rightangle real,"
                + "m_theoheight integer,"
                + "m_distance integer,"
                + "m_side integer,"
                + "m_baseortop text,"
                + "m_date date,"
                + "m_contractor text,"
                + "m_sideazimuth integer,"
                + "m_s_id integer,"
                + "m_b_id integer,"
         *//*
        for(int side = 1;  side < building.getConfig() + 1; side ++) {
            for(int sec = 1; sec < building.getNumberOfSections() + 1; sec++) {

                Measurement measurement = building.getMeasurements().get(side * sec);
                int sectionNumber = i % building.getNumberOfSections();
                if (sectionNumber == 0)
                    String queryM = "insert or replace into measures (s_id, s_number, s_widthbottom, s_widthtop," +
                            "s_height, s_height, s_level, s_name, s_b_id)" +
                            "values("
                            + measurement.getId() + ", "
                            + measurement.getNumber() + ", "
                            + "'" + measurement.getCircle() + "', "
                            + measurement.getLeftAngle() + ", "
                            + measurement.getRightAngle() + ", "
                            + measurement.getTheoHeight() + ", "
                            + measurement.getDistance() + ", "
                            + measurement.getSide() + ", "
                            + "'" + measurement.getBaseOrTop().toString() + "', "
                            + "'" + dateFormat.format(measurement.getDate()) + "', "
                            + "'" + measurement.getContractor() + "', "
                            + measurement.getAzimuth() + ", "
                            + building.getSection(i % (building.getNumberOfSections() + 1) + 1).getId() + ", "
                            + building.getId() + ")";
                localDB.execSQL(queryM);
            }
        }
*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Integer, Building> getBuildingMapFromLocal() {
        LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        buildingMap = localDBExplorer.getBuildingMap(searchParameters);
        localDBExplorer.closeDB();
        Log.d(LOG_TAG, "building map from local: " + buildingMap);
        return buildingMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getFromLocalDB(int building_id) {
        LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        building = localDBExplorer.get(building_id);
        localDBExplorer.closeDB();
    }

    public void showInnerSearchResultDialogFragment(FragmentManager fragmentManager) {
        //innerSearchResultDialogFragment.show(fragmentManager, null);
    }

    public void showSearchFormDialogFragment(FragmentManager fragmentManager) {
        //searchFormDialogFragment.show(fragmentManager, null);
    }
}
