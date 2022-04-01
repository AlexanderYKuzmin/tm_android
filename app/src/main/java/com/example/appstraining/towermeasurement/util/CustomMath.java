package com.example.appstraining.towermeasurement.util;

import android.util.Log;

import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.Building;

public class CustomMath {
    private static final String LOG_TAG = "CustomMath";

    public static final int RIGHT_ANGLE = 1;
    public static final int LEFT_ANGLE = -1;

    public static int getHypotenuse(int legOne, int legTwo) {
        return (int)Math.round(Math.sqrt(Math.pow(legOne, 2) + Math.pow(legTwo, 2)));
    }

    public static double getDefaultAngle(int widthBottom, int theoDistance, int theoHeight, int side, int config) {
        double defaultAngle = 0.0;

        int deltaDistance = 0; // inner circle radius or half of width bottom
       /* int widthBottom = building.getSection(1).getWidthBottom();
        int theoDistance = building.getMeasurement(1, side, BaseOrTop.BASE).getDistance();
        int theoHeight = building.getMeasurement(1, side, BaseOrTop.BASE).getTheoHeight();*/

        if (config == 3) {
            deltaDistance = (int) Math.round(widthBottom * Math.sqrt(3) / 2);
        } else if (config == 4) {
            deltaDistance = widthBottom / 2;
        }

        int distanceRes = getHypotenuse(theoHeight, theoDistance - deltaDistance); // distance from theodolite vision point to the tower edge bottom
        double tanDefaultAngle = (double) (widthBottom / 2) / distanceRes;

        Log.d(LOG_TAG, "tangets = " + tanDefaultAngle);
        Log.d(LOG_TAG, "width bottom = " + widthBottom);

        defaultAngle = Math.atan(tanDefaultAngle);

        Log.d(LOG_TAG, "Default angle = " + defaultAngle);
        return defaultAngle;
    }
}
