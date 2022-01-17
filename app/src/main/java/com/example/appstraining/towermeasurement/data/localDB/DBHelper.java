package com.example.appstraining.towermeasurement.data.localDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "DBHelper";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "localDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table buildings ("
                + "b_id integer primary key,"
                + "b_name text,"
                + "b_address text,"
                + "b_type text,"
                + "b_config integer,"
                + "b_numberofsecs integer,"
                + "b_height integer,"
                + "b_startlevel integer,"
                + "b_username text,"
                + "b_creationdate text"
                + ");");
        db.execSQL("create table sections ("
                + "s_id integer primary key,"
                + "s_number integer,"
                + "s_widthbottom integer,"
                + "s_widthtop integer,"
                + "s_height integer,"
                + "s_name text,"
                + "s_level integer,"
                + "s_b_id integer,"
                + "FOREIGN KEY (s_b_id) REFERENCES buildings(b_id)"
                + ");");
        db.execSQL("create table measures ("
                + "m_id integer primary key,"
                + "m_number integer,"
                + "m_circle text,"
                + "m_leftangle double,"
                + "m_rightangle double,"
                + "m_theoheight integer,"
                + "m_distance integer,"
                + "m_sectionnumber integer,"
                + "m_side integer,"
                + "m_baseortop text,"
                + "m_date date,"
                + "m_contractor text,"
                + "m_sideazimuth integer,"
                + "m_s_id integer,"
                + "m_b_id integer,"
                + "FOREIGN KEY (m_s_id) REFERENCES sections(s_id),"
                + "FOREIGN KEY (m_b_id) REFERENCES buildings(b_id)"
                + ");");
        db.execSQL("create table results ("
                + "r_id integer primary key,"
                + "averageKL real,"
                + "averageKR real,"
                + "averageKLKR real,"
                + "shift_deg real,"
                + "shift_mm integer,"
                + "tan_alfa real,"
                + "dist_to_sec integer,"
                + "delta_dis integer,"
                + "beta_average real,"
                + "beta_i real,"
                + "beta_delta real,"
                + "r_s_id integer,"
                + "r_b_id integer,"
                + "r_m_id integer,"
                + "FOREIGN KEY (r_s_id) REFERENCES sections(s_id),"
                + "FOREIGN KEY (r_b_id) REFERENCES buildings(b_id),"
                + "FOREIGN KEY (r_m_id) REFERENCES measures(m_id)"
                + ");");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
