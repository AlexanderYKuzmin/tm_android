package com.example.appstraining.towermeasurement;

import android.util.Log;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.RequestCode;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RuVdsServer {
    static final String LOG_TAG = "RuVdsServerService";
    static final String URL = "http://194.87.94.207:8080//TowerServlet/TowerMeasurementServlet";
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static String postResponse;
    static  Type buildingMapType = new TypeToken<Map<Integer, Building>>() {}.getType();

    OkHttpClient okHttpClient;

    private void connect(){

    }

    private void disconnect(){

    }

    public static String POST(OkHttpClient client, Building building, RequestCode requestCode){
        Log.d(LOG_TAG, "Starting to POST, Request code = " + requestCode.toString());
        String postURL = URL + "?"
                + "reqcode=" + requestCode;
        RequestBody body = RequestBody.Companion.create(createJsonStringFromObject(building),JSON);
        Request request = new Request.Builder()
                .url(postURL)
                .post(body)
                .build();
        postResponse = null;
        //Response response = null;
        /*try {
            response = client.newCall(request).execute();
            Log.d(LOG_TAG, "Response : " + response.body().string());
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(LOG_TAG, "Failed!");
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) {
                //mainPresenter.ruvdsThreadName = Thread.currentThread().getName();
                try {
                    final String myResponse = response.body().string();
                    Log.d(LOG_TAG, "Response : " + myResponse);
                    RuVdsServer.postResponse = myResponse;
                /*MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        return myResponse;
                    }
                });*/
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Somthing wrong with post data");
                    e.printStackTrace();
                }
            }
        });
        return postResponse;
    }

    public static void GET(MainPresenter mainPresenter, OkHttpClient client, RequestCode requestCode, int id
            , String name, String address) {
        String getURL = URL + "?"
                + "reqcode=" + requestCode + "&"
                + "objname=" + name + "&"
                + "building_id=" + id;
        Log.d(LOG_TAG, " getUrl = " + getURL);
        Request request = new Request.Builder().url(getURL).build();
        Response response = null;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //mainPresenter.ruvdsThreadName = Thread.currentThread().getName();
                switch (requestCode){
                    case GET:
                        mainPresenter.setBuilding(createObjectFromJson(response.body().string()));
                        mainPresenter.isBuildingPrepared = true;
                        break;
                    case SEARCH:
                        mainPresenter.setBuildingMap(createMapFromJson(response.body().string()));
                        mainPresenter.isBuildingMapPrepared = true;

                }

                Log.d(LOG_TAG, "isBuildingPrepared = " + mainPresenter.isBuildingMapPrepared);

            }
        });

    }

    public ArrayList<Map<Integer, Building>> getBuildingMap(String name, String address) {
        return null;
    }

    private static Map<Integer,Building> createMapFromJson(String jsonString){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd").create();
        Map<Integer, Building> map = gson.fromJson(jsonString, buildingMapType);
        Log.d(LOG_TAG, "JSON is: " + jsonString);
        return map;
    }

    public static Building createObjectFromJson(String jsonString) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd").create();
        Building building = gson.fromJson(jsonString, Building.class);
        Log.d(LOG_TAG, "Created object from json. JSON is: " + jsonString);
        return building;
    }

    public static String createJsonStringFromObject(Building building) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setDateFormat("yyyy-MM-dd").create();
        String jsonString = gson.toJson(building);
        Log.d(LOG_TAG, "Json created. " + jsonString);
        return jsonString;
    }
}
