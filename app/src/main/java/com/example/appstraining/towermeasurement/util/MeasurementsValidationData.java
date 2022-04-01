package com.example.appstraining.towermeasurement.util;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.Measurement;

import java.util.List;

public class MeasurementsValidationData {

    public static boolean isMeasurementConsistent(Measurement measurement, int widthBottom, int config) {
        if (measurement.getSectionNumber() == 1) {     // do not check for others measures except 1 section
            double leftAngle = measurement.getLeftAngle();
            double rightAngle = measurement.getRightAngle();

            double defaultAngle =               // get whole angle (or right angle - left angle
                    CustomMath.getDefaultAngle(
                            widthBottom, measurement.getDistance(), measurement.getTheoHeight(), measurement.getSide(), config);

            double currentWholeAngle = rightAngle - leftAngle;
            return Math.abs(defaultAngle - currentWholeAngle) < 0.5 && measurement.getDistance() > 5000;
        }
        return measurement.getDistance() > 5000;
    }

    public static boolean isMeasurementListConsistent(List<Measurement> measurementList) {
        return false;
    }

    public static boolean isAngleDataCorrect(int[] angleData) {
        return (angleData[0] <= 180 && angleData[0] >= 0)
                && (angleData[1] <= 60 && angleData[1] >= 0)
                && (angleData[2] <= 60 && angleData[2] >= 0)
                && (angleData[3] <= 360 && angleData[3] >= 0)
                && (angleData[4] <= 60 && angleData[4] >= 0)
                && (angleData[5] <= 60 && angleData[5] >= 0)
                && (angleData[6] <= 360 && angleData[6] >= 0);
    }
}
