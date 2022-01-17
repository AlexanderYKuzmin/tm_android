package com.example.appstraining.towermeasurement.data.localDB;

import android.database.sqlite.SQLiteDatabase;

import com.example.appstraining.towermeasurement.model.Building;

import java.util.ArrayList;
import java.util.Map;

public class SQLiteService {

    SQLiteDatabase mLocalDB;

    public SQLiteService(SQLiteDatabase localDB){
        mLocalDB = localDB;
    }

    void insertBuilding(Building building){

    }

    void selectBuilding(int id){

    }

    void updateBuilding(Building building){

    }

    ArrayList<Map<Integer, Building>> getBuildingMap(){
        return null;
    }
}
