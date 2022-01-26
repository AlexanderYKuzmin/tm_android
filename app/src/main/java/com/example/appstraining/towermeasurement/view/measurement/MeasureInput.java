package com.example.appstraining.towermeasurement.view.measurement;

import androidx.fragment.app.Fragment;

import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureGroup;

public interface MeasureInput {
   // void sendValues(ArrayList<Measurement> measurements);
    //void updateMeasureList();

    //void oneItemUpdateMeasureList(Measurement measurement, int[] leftAngle, int[] rightAngle);
    MeasureInputPresenter getPresenter();

    void showMeasureDialogFragment(int measurementNumber, int group);

    MeasureGroup getFragment(int group);
}
