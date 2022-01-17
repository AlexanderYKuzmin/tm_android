package com.example.appstraining.towermeasurement.view.measurement;

import android.content.Context;
import android.util.Log;

import com.example.appstraining.towermeasurement.model.Measurement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.appstraining.towermeasurement.view.measurement.MeasureInputActivity.LEFT_LIST;

public class MeasureInputPresenter {
    private final String LOG_TAG = "MeasureInputPresenter";
    private Context context;
    private MeasureInputInterface mMeasureInputActivity;

    private ArrayList<Measurement> measurements;

    public MeasureInputPresenter(Context context, MeasureInputInterface measureInputActivity){
        this.context = context;
        mMeasureInputActivity = measureInputActivity;

    }

    public void sendValues(ArrayList<Measurement> measurements) {

    }

    public void setMeasurements(ArrayList<Measurement> measurements){
        this.measurements = measurements;
    }

    public void updateMeasurement(int measureNum, double leftAngle, double rightAngle, int azimuth,
                                  int theoDistance, int theoHeight, String... params) {
        Log.d(LOG_TAG, "Update measurement started.");
        final String DEAFULT_DATE = "01-01-2001";
        final String REGEX_DATE = "([1-3][0-9]-){2}20\\d{2}";

        String contractor = params[0];
        String dateStr = params[1];
        //int theoHeight = t;
        //int distance = Integer.parseInt(params[2]);

        Pattern datePattern = Pattern.compile(REGEX_DATE);
        Matcher matcher = datePattern.matcher(dateStr);

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        java.util.Date date = null;
        if(!matcher.matches()) { dateStr = DEAFULT_DATE; }
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            Log.d(LOG_TAG, "Parsing date is not succeed.");
            e.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        Measurement measurement = measurements.get(measureNum - 1);
        measurement.setLeftAngle(leftAngle);
        measurement.setRightAngle(rightAngle);
        measurement.setTheoHeight(theoHeight);
        measurement.setDistance(theoDistance); // distance to axis building
        measurement.setDate(sqlDate);
        measurement.setContractor(contractor);
        measurement.setAzimuth(azimuth);
        Log.d(LOG_TAG, "Measurements are updated. MeasureNum = " );
        //Log.d(LOG_TAG, "left angle: " + );
        mMeasureInputActivity.oneItemUpdateMeasureList(measurement, fromDecToDeg(leftAngle),
                fromDecToDeg(rightAngle));
    }

    /*public void setMeasurement(int measureNum, double leftAngle, double rightAngle,
                               int azimuth){
        measurements.get(measureNum).
    }*/

    public ArrayList<Measurement> getMeasurements() {
        return measurements;
    }

    public ArrayList<int[]> getDegMinSecArrList(int sideDirection) {
        ArrayList<int[]> degArrayList = new ArrayList<>();
        double dec = 0.0;
        for(int i = 0; i < measurements.size(); i++) {
            if (sideDirection == LEFT_LIST) {
                dec = measurements.get(i).getLeftAngle();
            } else {
                dec = measurements.get(i).getRightAngle();
            }
            degArrayList.add(fromDecToDeg(dec));
        }
        return degArrayList;
    }

    public int[] fromDecToDeg(double dec){
        int[] degArray = new int[3];
            degArray[0] = (int) dec; // degree integer part
            double tempVal = ((dec - degArray[0]) * 3600); // remainder to seconds
            double tempVal2 = tempVal/60; // double value of minutes
            degArray[1] = (int) tempVal2; // integer minutes
            double tempVal3 = tempVal2 - degArray[1]; // remainder of minutes
            degArray[2] = (int) Math.round(tempVal3*60);

        return degArray;
    }

    public double fromDegToDec(int d, int m, int s){

        int[] deg = {d, m, s};
        Log.d(LOG_TAG, "Come angle data: " + deg[0] + " : " + deg[1] + " : " + deg[2]);
        Log.d(LOG_TAG, "deg[1]/60 = " + deg[1]/60 + "; deg[2]/3600 = " + deg[2]/3600);
        return ((double)deg[0] + (double)deg[1]/60 + (double)deg[2]/3600);
    }

    /*private int[] combineDegArray(String dStr,String mStr, String sStr){
        int d = Integer.parseInt(dStr);
        int m = Integer.parseInt(mStr);
        int s = Integer.parseInt(sStr);
        int[] degArray = {d, m, s};
        return degArray;
    }*/

    public boolean isAngleDataCorrect(int... params) {
            //if(Integer.)
        if(params[0] > 360 || params[0] < -360) return false;
        if(params[1] > 60 || params[1] < -60) return false;
        if(params[2] > 60 || params[2] < -60) return false;
        if(params[3] > 360 || params[3] < -360) return false;
        if(params[4] > 60 || params[4] < -60) return false;
        if(params[5] > 60 || params[5] < -60) return false;
        if(params[6] > 360 || params[6] < -360) return false;

        return true;
    }

    public boolean isConstantsCorrect(String... params) {
        return false;
    }

    /*public void checkAndUpdateMeasurement(int measureNum, String... fields) {
        int leftDeg = Integer.parseInt(fields[0]);
        int leftMin = Integer.parseInt(fields[1]);
        int leftSec = Integer.parseInt(fields[2]);
        int rightDeg = Integer.parseInt(fields)
    }*/

    public int[] getSingleMeasurementData(int position) {
        Measurement measurement = measurements.get(position);
        int[] leftAngleDeg = fromDecToDeg(measurement.getLeftAngle());
        int[] rightAngleDeg = fromDecToDeg(measurement.getRightAngle());

        return  new int[]{
                leftAngleDeg[0],
                leftAngleDeg[1],
                leftAngleDeg[2],
                rightAngleDeg[0],
                rightAngleDeg[1],
                rightAngleDeg[2],
                measurement.getAzimuth(),
                measurement.getDistance(),
                measurement.getTheoHeight(),
                measurement.getNumber()
        };
    }
}
