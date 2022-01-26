package com.example.appstraining.towermeasurement.view.measurement.fragments;

import android.os.Bundle;

import com.example.appstraining.towermeasurement.model.DegreeSeparated;
import com.example.appstraining.towermeasurement.model.Measurement;

import java.nio.ByteBuffer;
import java.util.List;

public interface MeasureGroup {

    void onItemUpdateMeasureList(Measurement measurement, DegreeSeparated degreeSeparated);

    void updateAdapter(List<Measurement> measurements, List<DegreeSeparated> degreeSeparatedList);
}
