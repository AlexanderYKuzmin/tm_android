package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.view.ReportPrepareActivity;

import java.util.ArrayList;
import java.util.Comparator;

public class ReportPreparePresenter {
    private final String LOG_TAG = "ReportPreparePresenter";
    ReportPrepareActivity reportPrepareActivity;
    Context mContext;
    int buildingID;

    Building reportBuilding;
    LocalDBExplorer localDBExplorer;

    int[] levels;
    int[] xozArray, yozArray, xoyArray;
    public final static int MIN_X = 0;
    public final static int MAX_X = 1;
    public final static int MIN_Y = 2;
    public final static int MAX_Y = 3;
    private int[] xozRange = {0, 0, 0, 0};
    private int[] yozRange = {0, 0, 0, 0};
    private int[] xoyRange = {0, 0, 0, 0}; // min x, max x, min y, max y;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ReportPreparePresenter (int buildingID, int[] levels, ReportPrepareActivity reportPrepareActivity) {
        this.reportPrepareActivity = reportPrepareActivity;
        this.buildingID = buildingID;
        this.levels = levels;

        localDBExplorer = new LocalDBExplorer(reportPrepareActivity.getApplicationContext());
        reportBuilding = localDBExplorer.getBuilding(buildingID);
        for(Result r : reportBuilding.getResults()) {
            Log.d(LOG_TAG, "result id = " + r.getId());
        }
        localDBExplorer.closeDB();
    }

    public Building getReportBuilding() {
        return reportBuilding;
    }

    public void mountBuilding() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getXOZArray() {        // { shift, height, shift2, height2 ....}
        Log.d(LOG_TAG, "Get XOZ Array.");
        ArrayList<Result> reportResults = reportBuilding.getResults();
        reportResults.sort(Comparator.comparing(Result::getId));
        Log.d(LOG_TAG, "reportBuilding.getResults().size() = " + reportResults.size());
        ArrayList<Measurement> reportMeasurements = reportBuilding.getMeasurements();
        reportMeasurements.sort(Comparator.comparing(Measurement::getId));
        //int size = reportBuilding.getResults().size()/reportBuilding.getConfig() * 2;
        int size = reportBuilding.getResults().size() / 3 * 2;
        xozArray = new int[size];
        xozArray[0] = 0;
        xozArray[1] = levels[0];
        for(int i = 2, j = 1; i < (size - 2); i += 2, j++){
            int sectionNumber = reportMeasurements.get(j).getSectionNumber();
            Log.d(LOG_TAG, "results size = " + size + "\n"
                    + " j = " + j + "; i = " + i + "\n"
                    + "current section number = " + sectionNumber
                    + " current level = " + levels[j]
            );
            xozArray[i] = reportResults.get(j).getShiftMm();
            xozArray[i + 1] = levels[j];

            if(xozRange[MIN_X] > xozArray[i]) xozRange[MIN_X] = xozArray[i]; // set new minX in XOZ
            if(xozRange[MAX_X] < xozArray[i]) xozRange[MAX_X] = xozArray[i]; // set new max X in XOZ
            Log.d(LOG_TAG, " xoz[i] = " + xozArray[i] + "; xoz[i+1] = " + xozArray[i + 1]);
            if( i == size -2 -2) {
                xozArray[i + 2] = reportResults.get(j + 1).getShiftMm();
                xozArray[i + 3] = reportBuilding.getHeight();
                if(xozRange[MIN_X] > xozArray[i + 2]) xozRange[MIN_X] = xozArray[i]; // set new minX in XOZ
                if(xozRange[MAX_X] < xozArray[i + 2]) xozRange[MAX_X] = xozArray[i]; // set new max X in XOZ
                xozRange[MAX_Y] = xozArray[i + 3];
                Log.d(LOG_TAG, " xoz[i + 2] = " + xozArray[i + 2] + "; xoz[i+3] = " + xozArray[i + 3]);
            }
        }
        xoyRange[MIN_X] = xozRange[MIN_X];
        xoyRange[MAX_X] = xozRange[MAX_X];
        return xozArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] getYOZArray() {
        ArrayList<Result> reportResults = reportBuilding.getResults();
        reportResults.sort(Comparator.comparing(Result::getId));
        ArrayList<Measurement> reportMeasurements = reportBuilding.getMeasurements();
        reportMeasurements.sort(Comparator.comparing(Measurement::getId));
        //int size = reportBuilding.getResults().size()/reportBuilding.getConfig() * 2;
        int size = reportBuilding.getResults().size() / 3 * 2;
        double k = 0.866; // cos 30
        yozArray = new int[size];
        yozArray[0] = 0;
        yozArray[1] = levels[0];
        for(int i = 2, j = size/2 +1; i < (size - 2); i += 2, j++){
            int sectionNumber = reportMeasurements.get(j - size/2).getSectionNumber();
            Log.d(LOG_TAG, "results size = " + size + "\n"
                    + " j = " + j + "; i = " + i + "\n"
                    + "current section number = " + sectionNumber
                    + " current level = " + levels[j - size/2] + "\n"
                    + "shifMm = " + reportResults.get(j).getShiftMm()
            );
            yozArray[i] = (int) Math.round(reportResults.get(j).getShiftMm() * k);
            yozArray[i + 1] = levels[j - size/2];
            if(yozRange[MIN_X] > yozArray[i]) yozRange[MIN_X] = yozArray[i]; // set new minX in YOZ
            if(yozRange[MAX_X] < yozArray[i]) yozRange[MAX_X] = yozArray[i]; // set new max X in YOZ
            Log.d(LOG_TAG, " yoz[i] = " + yozArray[i] + "; yoz[i+1] = " + yozArray[i + 1]);
            if( i == (size -2 -2)) {
                yozArray[i + 2] = (int) Math.round(reportResults.get(j + 1).getShiftMm() * k);
                yozArray[i + 3] = reportBuilding.getHeight();
                if(yozRange[MIN_X] > yozArray[i + 2]) yozRange[MIN_X] = yozArray[i + 2]; // set new minX in YOZ
                if(yozRange[MAX_X] < yozArray[i + 2]) yozRange[MAX_X] = yozArray[i + 2]; // set new max X in YOZ
                yozRange[MAX_Y] = yozArray[i + 3];
                Log.d(LOG_TAG, " yoz[i + 2] = " + yozArray[i + 2] + "; yoz[i+3] = " + yozArray[i + 3]);
            }
        }

        //*** Test ***
        Log.d(LOG_TAG, "YOZ Range");
        for (int x:
             yozRange) {
            Log.d(LOG_TAG, "");
        }
        //*** End test ***

        xoyRange[MIN_Y] = yozRange[MIN_X];
        xoyRange[MAX_Y] = yozRange[MAX_X];
        return yozArray;
    }

    public int[] getXOYArray() {
        xoyArray = new int[xozArray.length];
        Log.d(LOG_TAG, "XOY array :");
        for(int i = 0; i < xozArray.length; i += 2) {
            xoyArray[i] = xozArray[i];
            xoyArray[i + 1] = yozArray[i];
            Log.d(LOG_TAG,
            + xozArray[i] + " :: " + xozArray[i + 1]);
        }
        return xoyArray;
    }

    public int[] getTheoDistances() {
        int[] theoDistances = new int[3];
        int step = reportBuilding.getNumberOfSections() + 1;
        int iMax = reportBuilding.getMeasurements().size() / step;
        Log.d(LOG_TAG, "getDistance iMax = " + iMax +"\n"
        + "step = " + step);
        for(int i = 0; i < 3; i++){
            theoDistances[i] = reportBuilding.getMeasurements().get(i * step).getDistance();
        }
        return theoDistances;
    }

    public int[] getXOZRange() {
        return xozRange;
    }

    public int[] getYOZRange() {
        return yozRange;
    }

    public int[] getXOYRange() {
        return xoyRange;
    }
}
