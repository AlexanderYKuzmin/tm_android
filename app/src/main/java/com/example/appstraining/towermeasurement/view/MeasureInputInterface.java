package com.example.appstraining.towermeasurement.view;

import com.example.appstraining.towermeasurement.model.Measurement;

import java.util.ArrayList;

public interface MeasureInputInterface {
   // void sendValues(ArrayList<Measurement> measurements);
    void updateMeasureList();

    void oneItemUpdateMeasureList(Measurement measurement, int[] leftAngle, int[] rightAngle);
}
