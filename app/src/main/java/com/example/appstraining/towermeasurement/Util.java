package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Util {

    private static String FILENAME = "/app.properties";
    public static String getProperty(String key, Context context) {
        Properties properties = new Properties();;
        //AssetManager assetManager = context.getAssets();
        try{
            InputStream inputStream = new FileInputStream(context.getFilesDir().getAbsolutePath()
                    + FILENAME);
            properties.load(inputStream);
            inputStream.close();
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setProperty(String key, String value, Context context) throws IOException {
        try {
            Properties properties = new Properties();;
            AssetManager assetManager = context.getAssets();
            OutputStream outputStream = new FileOutputStream(context.getFilesDir().getAbsolutePath()
                + FILENAME);

            properties.setProperty(key, value);
            properties.store(outputStream, null);
            outputStream.close();
            Class.forName("dalvik.system.CloseGuard")
                    .getMethod("setEnabled", boolean.class)
                    .invoke(null, true);
        } catch (ReflectiveOperationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
