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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import okhttp3.OkHttpClient;

public class MainPresenter implements LifecycleObserver {
    private final String LOG_TAG = "mainPresenter.class";
    private final int TOWER_EDGES = 101;
    private final int TOWER_FLATS = 102;
    public static final int RESULT_SECTION_VALUES_OK = 1;

    MainView mMainActivity;
    Context context;
    Properties properties;

    public boolean isBuildingMapPrepared = false;
    public boolean isBuildingPrepared = false;
    public static boolean isMeasuresChanged = false;

    private final LocalDBExplorer dbExplorer;
    DrawPreparation drawPreparation;

    private Building building;
    private ArrayList<Section> patternSections = new ArrayList<>();
    private String[] searchParameters;
    Map<Long, Building> buildingMap;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    // fragments handle


    public MainPresenter(MainView mainActivity, Context context){
        mMainActivity = mainActivity;
        this.context = context;
        dbExplorer = new LocalDBExplorer(context);
        drawPreparation = new DrawPreparation(this);
    }

    public void setBuilding(Building building) {
        Log.d(LOG_TAG, "set building/ Building is: " + building);
        this.building = building;
    }

    public void setPatternSections(ArrayList<Section> sections){
        this.patternSections = sections;
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

    public void setBuildingMap(Map<Long, Building> buildingMap) {
        this.buildingMap = buildingMap;
    }

    public void createBuilding(String... params) {                  // for tab "new" menu
        long id = 0L;
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
        if (mMainActivity.getActivityMode() == MainActivityMode.USE_TEMPLATE) {
            building.setSections(patternSections);
        } else {
            for(int i = 0; i < numberOfSections; i++) {
                sectionNumber = i + 1;
                building.addSection(new Section(0, sectionNumber, widthBottom, widthTop,
                        sectionHeight, level, name, id));
                System.out.println("******" + building.getSection(i+1).toString());
            }
        }

        Log.d(LOG_TAG, "Sections created. Example Section #1 " + building.getSection(1).getNumber());

        // ******** Add measurements **********
        int measureNumber = 0;
        for(int side = 1; side < 3; side++) { // changed for 2 sides
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
        for(int side = 1; side < 3; side++) {  // change for 2 sides
            for (int sectionNum = 1; sectionNum < building.getNumberOfSections() + 2; sectionNum++) {           // according with top of section
                building.addResultData(new Result(0, 0.0, 0.0, 0.0, 0.0,
                0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0,
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
    public synchronized void loadBuilding(Long building_id){
        int b_id = building_id.intValue();
        Log.d(LOG_TAG, "Load Building started! Building ID = " + building_id);
        OkHttpClient okHttpClient = new OkHttpClient();
        RuVdsServer.GET(this, okHttpClient, RequestCode.GET,
                 b_id, null, null);
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

    //*** Mount building on MainActivity  3d model***
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void mountBuilding(MainActivityMode activityMode) {

        mMainActivity.updateView(activityMode);

        if (building != null) {
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

            if (building.getMeasurements() != null) {
                setResults();
            }
            mMainActivity.showAnimatedModel(bundle);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public synchronized Map<Long, Building> loadBuildingMap(String objectName, String address) {
        Log.d(LOG_TAG, "Getting MAP started!");
        buildingMap = null;
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
                + "Building_id = 2: " + buildingMap.get(2L).getId() + " : "
                + buildingMap.get(2L).getAddress() + " : "
                + buildingMap.get(2L).getName()
        );
        if(buildingMap != null) {
            isBuildingMapPrepared = false;
            buildingMap.forEach((k, v) -> v.setId(0L));
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
        double betaAverageLeft, betaAverageRight;
        double betaI;
        double betaIPrev = 0.0;
        int betaDelta;

        ArrayList<Measurement> locUsingMeasurements = getMeasurements();
        locUsingMeasurements.sort(Comparator.comparing(Measurement::getId));

        ArrayList<Result> locUsingResults = getResults();
        locUsingResults.sort(Comparator.comparing(Result::getMeasureID));

        if(isMeasuresChanged) {
            // mathematics will be here!!
            for(int i = 0; i < locUsingMeasurements.size(); i++) {
                int sectionNumber = locUsingMeasurements.get(i).getSectionNumber();


                if(i != 0 && i != locUsingMeasurements.size() / 2) {
                    //distanceToPoint = getMeasures().get(i).getDistance();
                    averageKL =
                            (getMeasurements().get(i).getLeftAngle() + getMeasurements().get(i).getRightAngle()) / 2;
                    averageKR = averageKL;
                    averageKLKR = averageKL;
                    distanceToPoint = getDistanceToPoint(locUsingMeasurements.get(i));
                    shiftDegree = averageKLKR - startAverageKLKR;
                    tanAlfa = Math.tan(Math.PI*shiftDegree/180);
                    shiftMm = (int) (tanAlfa * distanceToPoint);
                    betaAverageLeft = locUsingMeasurements.get(i).getLeftAngle(); // average beta between KL & KR
                    betaAverageRight = locUsingMeasurements.get(i).getRightAngle(); // average beta between KL & KR
                    betaI = averageKL;
                    betaDelta = (int)((betaI - betaIPrev) * 3600);
                    //betaIPrev = betaI;


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
                    betaAverageLeft = locUsingMeasurements.get(i).getLeftAngle(); // average beta between KL & KR
                    betaAverageRight = locUsingMeasurements.get(i).getRightAngle(); // average beta between KL & KR
                    betaI = averageKL;
                    betaDelta = 0;
                    betaIPrev = betaI;
                }
                locUsingResults.get(i).updateResult(averageKL, averageKR, averageKLKR, shiftDegree, shiftMm,
                        tanAlfa, distanceToPoint, 0, betaAverageLeft, betaAverageRight,
                        betaI, betaDelta
                );
                Log.d(LOG_TAG, "Results parameters: " + "\n" +
                        " average KLKR: " + averageKLKR + "\n" +
                        " shift degree: " + shiftDegree + "\n" +
                        " tan alfa: " + tanAlfa + "\n" +
                        " shift mm: " + shiftMm + "\n" +
                        " beta delta: " + betaDelta);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getLastInputObject() {
        Log.d(LOG_TAG, "get last input object");
        Long id = 0L;
        try {
            String idStr = AppPropertyHandler.getProperty("id", context);
            Log.d(LOG_TAG, "id string = " + idStr);
            if(idStr != null) id = Long.parseLong(idStr);
            Log.d(LOG_TAG, "id = " + id);
        } finally {
            if(id != 0) {
                setBuilding(getFromLocalDB(id));
                mountBuilding(MainActivityMode.LAST_LOADED_OBJECT);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String registerObject() {
        /*OkHttpClient okHttpClient = new OkHttpClient();
        return  RuVdsServer.POST(okHttpClient, building, RequestCode.REGISTER);*/
        building.setId(0L);
        long id = dbExplorer.save(building);
        building.setId(id);
        return id > 0 ? "Объект сохранен id = " + id : "Объект не сохранен";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void setLastInputObject() {                                            // save last edit object id to property
        //new FileLoader(context).saveBuildingToFile(building);
        Log.d(LOG_TAG, "Method quit started set id = " + building.getId());

        String idStr = building.getId() != null ? String.valueOf(building.getId()) : null;
        Log.d(LOG_TAG, "idStr = " + idStr);
        try {
            if (idStr != null) {
                AppPropertyHandler.setProperty("id", idStr, context);
            }
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
                            Math.pow((theoDistance - r2), 2) // +r2 or -r2 depends on what the start distance to the tower is. To axis( - r2) or to edge (+r2)
            );
        }
        Log.d(LOG_TAG, "Distance to point is: " + distanceToPoint);
        Log.d(LOG_TAG, "r2 = " + r2);
        return distanceToPoint;
    }

    public void saveToLocalDB() {
        //LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        dbExplorer.update(building);
        //dbExplorer.closeDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveToLocalDB(Long... ids) {
        //LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        List<Building> chosenBuildings = new ArrayList<>();
        buildingMap.forEach((k, v) -> {
            if (Arrays.stream(ids).anyMatch(id -> id == k.longValue())) chosenBuildings.add(v);
        });
        Log.d(LOG_TAG, "buildings size after filtering by stream is " + chosenBuildings.size());
        /*for(int i = 0; i < ids.length; i++) {
            Log.d(LOG_TAG, "buildingMap in Main Presenter is " + buildingMap);
            Building building = buildingMap.get(2);
            Log.d(LOG_TAG, "building from buildingMap " + building.getId() + " : " + building.getName());
            chosenBuildings.add(building);
            Log.d(LOG_TAG, "Building added ot LocalDatabase id = " + ids[i] + " from list building is " + chosenBuildings.get(i));
            Log.d(LOG_TAG, "building = " + buildingMap.get(ids[i].intValue()).getName());
            //if(i == (ids.length - 1)) building = chosenBuildings.get(i);
        }*/
        long[] savedIds = dbExplorer.save(chosenBuildings);
        setBuilding(chosenBuildings.get(chosenBuildings.size() - 1));
        building.setId(savedIds[savedIds.length - 1]);
        setLastInputObject();
        //dbExplorer.closeDB();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<Long, Building> getBuildingMapFromLocal() {
        LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        buildingMap = localDBExplorer.getBuildingMap(searchParameters);
        localDBExplorer.closeDB();
        Log.d(LOG_TAG, "building map from local: " + buildingMap);
        return buildingMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Building getFromLocalDB(long building_id) {
        LocalDBExplorer localDBExplorer = new LocalDBExplorer(context);
        //building = localDBExplorer.get(building_id);
        //localDBExplorer.closeDB();
        return localDBExplorer.get(building_id, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int deleteFromLocalDB(Long[] ids) {
        return dbExplorer.delete(ids);
    }

    public void showInnerSearchResultDialogFragment(FragmentManager fragmentManager) {
        //innerSearchResultDialogFragment.show(fragmentManager, null);
    }

    public void showSearchFormDialogFragment(FragmentManager fragmentManager) {
        //searchFormDialogFragment.show(fragmentManager, null);
    }

    /*public void setMainActivityMode (MainActivityMode mode) {

    }

    public void getActivityMode() {
        mMainActivity.
    }*/
}
