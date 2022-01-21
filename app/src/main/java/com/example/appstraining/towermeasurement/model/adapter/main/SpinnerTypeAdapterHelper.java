package com.example.appstraining.towermeasurement.model.adapter.main;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.appstraining.towermeasurement.model.BuildingType;
import com.example.appstraining.towermeasurement.model.SpinnerAdapterType;

import java.util.Arrays;

public class SpinnerTypeAdapterHelper {
    Context context;
    String[] data;

    public SpinnerTypeAdapterHelper(Context context){
        this.context = context;
    }
        public ArrayAdapter<String> getAdapter() {
            data = BuildingType.getAllTypes();
            return new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
        }

        public int getIndex(String type) {
            int index = Arrays.asList(data).indexOf(type);
            return index;
        }
}
