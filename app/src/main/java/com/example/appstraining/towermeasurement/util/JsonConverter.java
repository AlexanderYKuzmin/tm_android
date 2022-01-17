package com.example.appstraining.towermeasurement.util;

import android.util.Log;

import com.example.appstraining.towermeasurement.model.BaseOrTop;
import com.example.appstraining.towermeasurement.model.Building;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonConverter {
    static final String LOG_TAG = "JsonConverter";
    static Type buildingMapType = new TypeToken<Map<Integer, Building>>() {}.getType();

    public static Map<Integer, Building> createMapFromJson(String jsonString){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd").create();
        Map<Integer, Building> map = gson.fromJson(jsonString, buildingMapType);
        Log.d(LOG_TAG, "JSON is: " + jsonString);
        return map;
    }

    public static Building createObjectFromJson(String jsonString) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd").create();
        Log.d(LOG_TAG, "JSON is: " + jsonString);
        //Gson gson = builder.create();
        Building building = gson.fromJson(jsonString, Building.class);

        return building;
    }

    public static String createJsonStringFromObject(Building building) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd").create();
        Log.d(LOG_TAG, "id = " + building.getId() + "\n"
            + "Section id = " +building.getSection(1).getId() + "\n"
                + "Measurement id = " + building.getMeasurement(1, 1, BaseOrTop.BASE) + "\n"
                + "Result id = " + building.getResult(31)
        );
        //Gson gson = builder.create();
        String jsonString = gson.toJson(building);
        return jsonString;
    }
}
