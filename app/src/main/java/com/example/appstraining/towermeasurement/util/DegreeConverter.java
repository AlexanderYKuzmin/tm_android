package com.example.appstraining.towermeasurement.util;

import android.util.Log;

public class DegreeConverter {

    public static int[] fromDecToDeg(double dec) {
        int[] degArray = new int[3];
        //Log.d("Degree Converter", "double dec = " + dec);
        degArray[0] = (int) dec; // degree integer part
        //Log.d("Degree Converter", "after int transformation dec = " + dec);

        double tempVal = (int)((dec - degArray[0]) * 3600); // remainder to seconds
        Log.d("Degree Converter", "seconds without deg = " + tempVal);
        double tempVal2 = tempVal / 60; // double value of minutes
        Log.d("Degree Converter", "minutes without deg = " + tempVal2);
        degArray[1] = (int) tempVal2; // integer minutes
        double tempVal3 = tempVal2 - degArray[1]; // remainder of minutes
        degArray[2] = (int) (tempVal3 * 60);

        return degArray;
    }

    public static double fromDegToDec(int d, int m, int s){
        final String LOG_TAG = "DegreeConverter";
        int[] deg = {d, m, s};
        /*Log.d(LOG_TAG, "Come angle data: " + deg[0] + " : " + deg[1] + " : " + deg[2]);
        Log.d(LOG_TAG, "deg[1]/60 = " + (double)deg[1]/60 + "; deg[2]/3600 = " + (double)deg[2]/3600);*/
        double angleBeforeRound = (double)deg[0] + (double)deg[1]/60 + (double)deg[2]/3600;
        /*Log.d(LOG_TAG, " before rounding double angle = " + (angleBeforeRound));
        double angleTest10000 = angleBeforeRound * 10000;
        Log.d(LOG_TAG, " after rounding double angle x10000 = " + (angleTest10000));
        double angleTestAfterRound = Math.round(angleBeforeRound * 10000);
        Log.d(LOG_TAG, " after rounding double angle = " + (angleTestAfterRound));
        Log.d(LOG_TAG, " after rounding double angle .10000 = " + (angleTestAfterRound/10000));*/
        //double resultAngleAfterRound = angleTestAfterRound/10000;
        return (double) Math.round(angleBeforeRound * 10000) / 10000;
    }
}
//Math.round((((double)deg[0] + (double)deg[1]/60 + (double)deg[2]/3600)) * 10000) / 10000