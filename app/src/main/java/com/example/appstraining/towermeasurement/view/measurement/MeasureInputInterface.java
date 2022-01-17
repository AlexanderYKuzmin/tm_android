package com.example.appstraining.towermeasurement.view.measurement;

import com.example.appstraining.towermeasurement.model.Measurement;

public interface MeasureInputInterface {
   // void sendValues(ArrayList<Measurement> measurements);
    void updateMeasureList();

    void oneItemUpdateMeasureList(Measurement measurement, int[] leftAngle, int[] rightAngle);
}
