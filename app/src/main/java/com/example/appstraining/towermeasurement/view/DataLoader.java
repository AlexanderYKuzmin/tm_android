package com.example.appstraining.towermeasurement.view;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.appstraining.towermeasurement.data.localDB.DBHelper;
import com.example.appstraining.towermeasurement.model.Building;

public class DataLoader {
    private  DBHelper mDBHelper;
    private SQLiteDatabase mLocalDB;
    private Context mContext;

    public DataLoader(Context context) {
        mContext = context;
    }

    public void saveBuildingToDisk(Building building) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}
