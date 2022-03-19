package com.example.appstraining.towermeasurement.model.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.appstraining.towermeasurement.model.BuildingType;

import java.util.Arrays;

public class SpinnerConfigAdapterHelper {
    Context context;
    String[] data = {"3", "4", "0"};

    public SpinnerConfigAdapterHelper(Context context){
        this.context = context;
    }

    public ArrayAdapter<String> getAdapter() {
        return new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
}

    public int getIndex(String type) {
        int index = Arrays.asList(data).indexOf(type);
        return index;
    }
}
