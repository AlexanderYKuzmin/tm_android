package com.example.appstraining.towermeasurement.data.file;

import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsProvider;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.GraphicType;
import com.example.appstraining.towermeasurement.util.BitmapConverter;
import com.example.appstraining.towermeasurement.util.JsonConverter;
import com.example.appstraining.towermeasurement.view.result.ReportPrepareActivity;
import com.jjoe64.graphview.GraphView;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLoader {
    final String LOG_TAG = "FileLoader";
    private final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    Context context;

    //private final File path = ;
    //String  filePath;

    private static FileLoader instance;

    private FileLoader(Context context) {
        this.context = context;
        //lastInputObjectPath = mContext.getFilesDir().getAbsolutePath() + "/lastObject.json";
    }

    public static FileLoader getInstance(Context context) {
        if(instance == null) {
            instance = new FileLoader(context);
        }
        return instance;
    }

    public void saveBuildingToFile(Building building, String filePath) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(JsonConverter.createJsonStringFromObject(building));
            writer.close();
            Log.d(LOG_TAG, "Current building is saved to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Building loadBuildingFromFile (String filePath) {
        if(Files.exists(Paths.get(filePath))){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(filePath));
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

    public void saveReportDocx(String fileName, XWPFDocument document) {
        String filePath = path + File.separator + fileName;
        //File appSpecificExternalDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        /*ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put();*/
        try(FileOutputStream fos = new FileOutputStream(filePath)) {
            document.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGraphPng(String fileName, GraphView graphView, int expandKoef) {
        String filePath = path + File.separator + fileName;
        //File appSpecificExternalDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        try(FileOutputStream fos = new FileOutputStream(filePath)) {
            Bitmap bitmap = BitmapConverter.getBitmapFromView(graphView, expandKoef);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

