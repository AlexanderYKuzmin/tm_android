package com.example.appstraining.towermeasurement.model.openGL;

import android.util.Log;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;

public class DrawPreparation {
    private final String LOG_TAG = "DrawPreparation";

    Building building;
    //Measurement measurement;
    MainPresenter mainPresenter;

    private final int TOWER_EDGES = 101;
    private final int TOWER_FLATS = 102;

    public DrawPreparation(MainPresenter presenter){
        this.mainPresenter = presenter;
    }

    /*public int getDistanceToPoint(Measurement measurement){
        building = mainPresenter.getBuilding();
        Section section = building.getSection(measurement.getSectionNumber());
        //Section prevSection;
        int theoDistance = measurement.getDistance();
        int theoHeight = measurement.getTheoHeight();
        //int buildingStartLevel = building.getStartLevel(); // gonna playing with theoheight according building startlevel
        //double r1; // previous section inner circle radius
        double r2; // current section inner circle radius
        double k = 1;

        //int a1; // previous section widthBottom
        //int a2; // current section widthBottom
        int distanceToPoint;
        switch (building.getConfig()) {
            case 0:
            case 4: k = 0.5;
                break;
            case 3: k = Math.sqrt(3) / 6;
                break;
        }
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
    }*/

    private float[] getVertices() {
        building = mainPresenter.getBuilding();
        int numberOfSections = building.getNumberOfSections();
        int verticesInLevel = building.getConfig();
        int numberOfVertices = 3 * verticesInLevel * (numberOfSections + 1);
        float[] towerVertices = new float[numberOfVertices];

        switch (building.getConfig()) {
            case 0:
                break;
            case 4:
                return getVerticesConfig_4(numberOfSections, numberOfVertices, towerVertices);
            case 3:
                return getVerticesConfig_3(numberOfSections, numberOfVertices, towerVertices);
        }
        return null;
    }

    private float[] getVerticesConfig_3(int numberOfSections, int numberOfVertices,
                                        float[] towerVertices) {
        float k = (float) (1 / Math.sqrt(3));
        float a, r, R;
        float h = -1.9f;

        Log.d(LOG_TAG, "Number of vertices Config_3 = " + numberOfVertices);
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

    private float[] getVerticesConfig_4(int numberOfSections, int numberOfVertices,
                                        float[] towerVertices) {
        float k = (float) (0.5);
        float a, r;
        int h0 = building.getHeight();
        int w0 = building.getSection(1).getWidthBottom();
        float h = -1.9f;

        Log.d(LOG_TAG, "Number of vertices Config_4 = " + numberOfVertices);
        // Log.d(LOG_TAG, " getting coordinates for each level");
        for(int i = 0; i < numberOfSections * 12; i = i + 12) { // get coordinates for each level
            //Log.d(LOG_TAG, "");
            a = (float)building.getSection(i / 12 + 1).getWidthBottom() / (float)(w0*1.1);
            r = a * k;
            //r = R / 2;
            //h0 = h;
            h = h + (float)building.getSection(i / 12 + 1).getHeight() / (float)(h0/2.9);

            towerVertices[i] = -a / 2; // X1
            towerVertices[i + 1] = h; // Y1
            towerVertices[i + 2] = -r; // Z1

            towerVertices[i + 3] = a / 2; // X2
            towerVertices[i + 4] = h; // Y2
            towerVertices[i + 5] = -r; // Z2

            towerVertices[i + 6] = a/2; // X3
            towerVertices[i + 7] = h; // Y3
            towerVertices[i + 8] = r; // Z3

            towerVertices[i + 9] = -a / 2; // X4
            towerVertices[i + 10] = h; // Y4
            towerVertices[i + 11] = r; // Z4

            if(i == (numberOfSections -1) * 12) {
                a = (float)building.getSection(i / 12 + 1).getWidthTop() / 10000;
                r = a * k;
                h = h + (float)building.getSection(i / 12 + 1).getHeight() / 20000;
                towerVertices[i + 12] = -a / 2; // X1
                towerVertices[i + 13] = h; // Y1
                towerVertices[i + 14] = -r; // Z1

                towerVertices[i + 15] = a / 2; // X2
                towerVertices[i + 16] = h; // Y2
                towerVertices[i + 17] = -r; // Z2

                towerVertices[i + 18] = a / 2; // X3
                towerVertices[i + 19] = h; // Y3
                towerVertices[i + 20] = r; // Z3

                towerVertices[i + 21] = -a / 2; // X4
                towerVertices[i + 22] = h; // Y4
                towerVertices[i + 23] = r; // Z4
            }
        }
        for(float x : towerVertices) {
            //Log.d(LOG_TAG, " coordinates = " + x);
        }
        return towerVertices;
    }

    public float[] getDrawingSequence(int towerPart) {
        building = mainPresenter.getBuilding();
        float[] vertices = getVertices();
        switch (building.getConfig()) {
            case 0:
                break;
            case 4:

                return getDrawingSequenceConfig_4(towerPart, vertices);
            case 3:
                return getDrawingSequenceConfig_3(towerPart, vertices);
        }
        return null;
    }

    private float[] getDrawingSequenceConfig_3(int towerPart, float[] vertices) {
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

    private float[] getDrawingSequenceConfig_4(int towerPart, float[] vertices) {
        float[] coordinatesSequence;
        int config = building.getConfig();
        switch (towerPart) {
            case TOWER_FLATS:
                int shift = 0;
                coordinatesSequence = new float[(vertices.length + (building.getNumberOfSections() + 1) * 3)];
                for (int i = 0; i < vertices.length; i = i + 12) {
                    // Log.d(LOG_TAG, "Flat " + i / 9);
                    coordinatesSequence[i + shift] = vertices[i];          // X1
                    //Log.d(LOG_TAG, "Left side x = " + coordinatesSequence[i + shift]);
                    coordinatesSequence[i + 1 + shift] = vertices[i + 1];   // Y1
                    //Log.d(LOG_TAG, "Left side y = " + coordinatesSequence[i + 1 + shift]);
                    coordinatesSequence[i + 2 + shift] = vertices[i + 2];   // Z1
                    //Log.d(LOG_TAG, "Left side x = " + coordinatesSequence[i + shift]
                    //      + " y = " + coordinatesSequence[i + 1 + shift]
                    //    + " z = " + coordinatesSequence[i + 2 + shift]);

                    coordinatesSequence[i + 3 + shift] = vertices[i + 3]; // X2
                    coordinatesSequence[i + 4 + shift] = vertices[i + 4]; // Y2
                    coordinatesSequence[i + 5 + shift] = vertices[i + 5]; // Z2

                    coordinatesSequence[i + 6 + shift] = vertices[i + 6]; // X3
                    coordinatesSequence[i + 7 + shift] = vertices[i + 7];  // Y3
                    coordinatesSequence[i + 8 + shift] = vertices[i + 8];  // Z3

                    coordinatesSequence[i + 9 + shift] = vertices[i + 9];  // X4
                    coordinatesSequence[i + 10 + shift] = vertices[i + 10]; // Y4
                    coordinatesSequence[i + 11 + shift] = vertices[i + 11];  // Z4

                    coordinatesSequence[i + 12 + shift] = vertices[i];  // X1
                    coordinatesSequence[i + 13 + shift] = vertices[i + 1]; // Y1
                    coordinatesSequence[i + 14 + shift] = vertices[i + 2];  // Z1
                    shift += 3;
                }
                return coordinatesSequence;
            case TOWER_EDGES:
                int shift2 = 0;
                coordinatesSequence = new float[vertices.length];
                for (int j = 0; j < config; j++) {                          // j is the edge number
                    shift = j * (building.getNumberOfSections() + 1) * 3;
                    shift2 = j * 3;
                    //Log.d(LOG_TAG, "Edge :" + j);
                    for (int i = 0, k = 0; i < coordinatesSequence.length / config; i = i + 3, k += 12) {

                        coordinatesSequence[i + shift] = vertices[k + shift2];
                        coordinatesSequence[i + 1 + shift] = vertices[k + 1 + shift2];
                        coordinatesSequence[i + 2 + shift] = vertices[k + 2 + shift2];
                        if (j == 0) {
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
}
