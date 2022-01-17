package com.example.appstraining.towermeasurement.data.file;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.util.JsonConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLoader {
    final String LOG_TAG = "FileLoader";
    String  lastInputObjectPath;
    Context mContext;

    public FileLoader(Context context) {
        mContext = context;
        lastInputObjectPath = mContext.getFilesDir().getAbsolutePath() + "/lastObject.json";
    }

    public void saveBuildingToFile(Building building) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(lastInputObjectPath));
            writer.write(JsonConverter.createJsonStringFromObject(building));
            writer.close();
            Log.d(LOG_TAG, "Current building is saved to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Building loadBuildingFromFile () {
        if(Files.exists(Paths.get(lastInputObjectPath))){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(lastInputObjectPath));
                StringBuffer result = new StringBuffer();
                String line = "";
                while (true) {
                    if (!((line = reader.readLine()) != null)) break;
                    result.append(line);
                }
                reader.close();
                return JsonConverter.createObjectFromJson(result.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
}

