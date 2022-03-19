package com.example.appstraining.towermeasurement.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;

public class BitmapConverter {
    private static final String LOG_TAG = "BitmapConverter";

    public static Bitmap getBitmapFromView(GraphView view, int expandKoef) {
        Bitmap bitmap = Bitmap.createBitmap(800, 1600, Bitmap.Config.ARGB_8888);
        //Log.d(LOG_TAG, "bitmap size: " + bitmap.getByteCount());
        System.out.println("bitmap size: " + bitmap.getByteCount());
        Canvas canvas = new Canvas(bitmap) {
            @Override
            public boolean isHardwareAccelerated() {
                return true;
            }
        };
        /*Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);*/
        view.layout(0,0,800,1600);
        view.draw(canvas);
        return bitmap;
    }
}
