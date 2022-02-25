package com.example.appstraining.towermeasurement;

import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.BuildingType;
import com.example.appstraining.towermeasurement.model.CircleTheo;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.model.Section;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TestData {
    private static final Long BUILDING_ID = 3L;
    private static final String NAME = "КО0094";
    private static final String ADDRESS = "Республика Коми, с. Жешарт";
    private static final int BUILDING_HEIGHT = 40000;
    private static final BuildingType BUILDING_TYPE = BuildingType.TOWER;
    private static final int BUILDING_CONFIG = 4;
    private static final Date CREATION_DATE = new Date(System.currentTimeMillis());

    private static final int NUMBER_OF_SECTIONS = 4;
    private static final int SECTION_HEIGHT = 10000;

    private static final double[] leftAngles = new double[] {
            0.0, 1.16, 2.31, 3.41, 4.26,
            0.0, 0.88, 1.75, 2.64, 3.28
    };

    private static final double[] rightAngles = new double[] {
            9.68, 8.77, 7.89, 7.07, 6.40,
            11.62, 9.99, 8.46, 6.99, 5.81
    };

    private static final Integer[] sides = new Integer[] {
            1, 1, 1, 1, 1,
            2, 2, 2, 2, 2
    };

    private static final int[] bottoms = new int[] {
         5400, 4330, 3250, 2170
    };

    private static final int[] tops = new int[] {
            4330, 3250, 2170, 1096
    };

    private static final double[] shiftsDegree = new double[] {
            0.00, 0.13, 0.26, 0.40, 0.49,
            0.00, -0.38, -0.70, -1.00, -1.27
    };

    private static final int[] shiftsMm = new int[] {
            0, 73, 160, 256, 359,
            0, -193, -423, -721, -1095
    };

    private static final double[] tangents = new double[] {
           0.00, 0.002190149, 0.004553644, 0.007028702, 0.008585049,
           0.00, -0.006588713, -0.01227125, -0.017399294, -0.022125658
    };

    private static final int[] distancesToSec = new int[] {
            32000, 34030, 38640, 45040, 52580,
            27000, 29290, 34460, 41450, 49480
    };

    private static final int[] distancesDelta = new int[] {
            0, 530, 1065, 1597, 2130,
            0, 530, 1065, 1597, 2130
    };

    private static final double[] betasAverageLeft = leftAngles;

    private static final double[] betasAverageRight = rightAngles;

    private static final double[] betasI = new double[] {
            4.84, 4.96, 5.10, 5.24, 5.33,
            5.81, 5.43, 5.11, 4.81, 4.54
    };

    private static final int[] betasDelta = new int[] {
            0, 456, 943, 1454, 1775,
            0, -1359, -2531, -2923, -3263
    };

    public static final int[] levels = new int[] {
            0, 8000, 16000, 24000, 32000,
            40000, 48000, 56000, 64000, 72000
    };

   /* public static Building testBuilding = new Building(
            2L,
            "KO223_2", // name
            "Республика Коми, Прилузский район, с Вухтым", // address
            BuildingType.TOWER, // type
            3, // configuration
            9,  // quantity sections
            72000, // height
            0,   // startLevel
            "NoUserName", // Contractor
            null, // Creation date
            new ArrayList<Section>(),
            new ArrayList<Measurement>(),
            new ArrayList<Result>()
    );*/

    public static Building getTestBuilding() {
        return new Building(
                BUILDING_ID,
                NAME,
                ADDRESS,
                BUILDING_TYPE,
                BUILDING_CONFIG,
                NUMBER_OF_SECTIONS,
                BUILDING_HEIGHT,
                0,
                "Kuzmin",
                CREATION_DATE,
                (ArrayList<Section>) getTestSections(),
                (ArrayList<Measurement>) getTestMeasurements(),
                (ArrayList<Result>) getTestResults()
        );
    }

    public static List<Measurement> getTestMeasurements() {

        List<Measurement> testMeasurements = new ArrayList<Measurement>();

        for (int i = 0; i < leftAngles.length/2; i++) {  // first side of tower
            BaseOrTop baseOrTop = i == leftAngles.length / 2 - 1 ? BaseOrTop.TOP : BaseOrTop.BASE;
            int sectionNum = i < leftAngles.length / 2 - 1 ? i + 1 : i;

            testMeasurements.add(
                    new Measurement(100 + i + 1, i + 1, 1, CircleTheo.KL, // id, number, side
                            leftAngles[i], rightAngles[i], // left right angle
                            1000, 32000,// theo height, distance
                            sectionNum, baseOrTop, 100 + sectionNum, BUILDING_ID.intValue(), // sectionNum, base or top, sectionId, buildingId
                            new Date(System.currentTimeMillis()), "Kuzmin", 0)
            );
        }

        for (int i = leftAngles.length/2; i < leftAngles.length; i++) {  // second side of tower
            BaseOrTop baseOrTop = i == leftAngles.length - 1 ? BaseOrTop.TOP : BaseOrTop.BASE;
            int sectionNum = i < leftAngles.length - 1 ? i + 1 - leftAngles.length / 2 : i - leftAngles.length / 2;
            testMeasurements.add(
                    new Measurement(100 + i + 1, i + 1, 2, CircleTheo.KL, // id, number, side
                            leftAngles[i], rightAngles[i], // left / right angle
                            1000, 45000,// theo height, distance
                            sectionNum, baseOrTop, 100 + sectionNum, BUILDING_ID.intValue(), // sectionNum, base or top, sectionId, buildingId
                            new Date(System.currentTimeMillis()), "Kuzmin", 0)
            );
        }

        /*for (int i = leftAngles.length; i < leftAngles.length + leftAngles.length/2; i++) {  // third side of tower
            BaseOrTop baseOrTop = i == leftAngles.length + leftAngles.length / 2 - 1 ? BaseOrTop.TOP : BaseOrTop.BASE;
            int sectionNum = i < leftAngles.length + leftAngles.length / 2 - 1 ? i + 1 - leftAngles.length : i - leftAngles.length;
            testMeasurements.add(
                    new Measurement(100 + i + 1, i + 1, 3, CircleTheo.KL, // id, number, side
                            0, 0, // left / right angle
                            0, 0,// theo height, distance
                            sectionNum, baseOrTop, 100 + sectionNum, BUILDING_ID.intValue(), // sectionNum, base or top, sectionId, buildingId
                            new Date(System.currentTimeMillis()), "Kuzmin", 0)
            );
        }*/
        return testMeasurements;
    }

    public static List<Result> getTestResults() {
        List<Result> testResults = new ArrayList<Result>();

        for (int i = 0; i < leftAngles.length/2; i++) {  // first side of tower
            int sectionNum = i < leftAngles.length / 2 - 1 ? i + 1 : i;

            double averageKL = (leftAngles[i] + rightAngles[i]) / 2;
            double averageKR = averageKL;
            double averageKLKR = averageKL;
            testResults.add(
                    new Result(100 + i + 1,
                            averageKL, averageKR, averageKLKR,
                            shiftsDegree[i], shiftsMm[i], tangents[i], distancesToSec[i], distancesDelta[i],
                            betasAverageLeft[i], betasAverageRight[i], betasI[i], betasDelta[i],
                            BUILDING_ID.intValue(), 100 + sectionNum, 100 + i + 1)
            );
        }

        for (int i = leftAngles.length/2; i < leftAngles.length; i++) {  // second side of tower
            int sectionNum = i < leftAngles.length - 1 ? i + 1 - leftAngles.length / 2 : i - leftAngles.length / 2;
            double averageKL = (leftAngles[i] + rightAngles[i]) / 2;
            double averageKR = averageKL;
            double averageKLKR = averageKL;
            testResults.add(
                    new Result(100 + i + 1,
                            averageKL, averageKR, averageKLKR,
                            shiftsDegree[i], shiftsMm[i], tangents[i], distancesToSec[i], distancesDelta[i],
                            betasAverageLeft[i], betasAverageRight[i], betasI[i], betasDelta[i],
                            BUILDING_ID.intValue(), 100 + sectionNum, 100 + i + 1)
            );
        }

        /*for (int i = leftAngles.length; i < leftAngles.length + leftAngles.length / 2; i++) {  // third side of tower // Костыль
            int sectionNum = i < leftAngles.length + leftAngles.length / 2 - 1 ? i + 1 - leftAngles.length : i - leftAngles.length;
            double averageKL =0.0;
            double averageKR = averageKL;
            double averageKLKR = averageKL;
            testResults.add(
                    new Result(100 + i + 1,
                            averageKL, averageKR, averageKLKR,
                            0.0, 0, 0.0, 0, 0,
                            0, 0, 0, 0,
                            BUILDING_ID.intValue(), 100 + sectionNum, 100 + i + 1)
            );
        }*/

        return testResults;
    }

    public static List<Section> getTestSections() {
        List<Section> testSections = new ArrayList<Section>();
        for (int i = 0; i < NUMBER_OF_SECTIONS; i++) {
            testSections.add(
                    new Section(100 + i + 1, i + 1, // id, number
                            bottoms[i], tops[i], SECTION_HEIGHT, i * SECTION_HEIGHT, // width bottom, width top, height, level
                            NAME, BUILDING_ID)
            );
        }
        return testSections;
    }
}
