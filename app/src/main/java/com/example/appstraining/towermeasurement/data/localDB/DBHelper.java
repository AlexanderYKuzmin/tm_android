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
                + "b_id integer primary key autoincrement, "
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
                + "s_id integer primary key autoincrement,"
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
                + "m_id integer primary key autoincrement,"
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
                + "r_id integer primary key autoincrement,"
                + "averageKL real,"
                + "averageKR real,"
                + "averageKLKR real,"
                + "shift_deg real,"
                + "shift_mm integer,"
                + "tan_alfa real,"
                + "dist_to_sec integer,"
                + "delta_dis integer,"
                + "beta_average_left real,"
                + "beta_average_right real,"
                + "beta_i real,"
                + "beta_delta real,"
                + "r_s_id integer,"
                + "r_b_id integer,"
                + "r_m_id integer,"
                + "FOREIGN KEY (r_s_id) REFERENCES sections(s_id),"
                + "FOREIGN KEY (r_b_id) REFERENCES buildings(b_id),"
                + "FOREIGN KEY (r_m_id) REFERENCES measures(m_id)"
                + ");");

        db.execSQL("INSERT INTO buildings (b_name, b_address, b_type, b_config, b_numberofsecs, b_height, b_startlevel, b_username, b_creationdate) " +
                "VALUES ('Тест Куб', 'Республика Удмуртия, г. Ижевск, ул. Кирова, 15', 'TOWER', 4, 2, 10000, 0, 'Иванов', '21-10-2022');\n");
        db.execSQL("INSERT INTO buildings (b_name, b_address, b_type, b_config, b_numberofsecs, b_height, b_startlevel, b_username, b_creationdate) " +
                "VALUES ('КО223', 'Республика Коми, с. Вухтым', 'TOWER', 3, 9, 72000, 0, 'Петров', '14-03-2022');\n");

        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (1, 2000, 1500, 5000, 'Тест Куб', 0, 1);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (2, 1500, 1200, 5000, 'Тест Куб', 5000, 1);\n");

        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (1, 9000, 7917, 8000, 'КО223', 0, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (2, 7917, 6833, 8000, 'КО223', 8000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (3, 6833, 5750, 8000, 'КО223', 16000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (4, 5750, 4667, 8000, 'КО223', 24000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (5, 4667, 3583, 8000, 'КО223', 32000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (6, 3583, 2500, 8000, 'КО223', 40000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (7, 2500, 2500, 8000, 'КО223', 48000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (8, 2500, 2500, 8000, 'КО223', 56000, 2);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (9, 2500, 2500, 8000, 'КО223', 64000, 2);\n");


        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (1, 'KL', 0.0, 11.42, 500, 15000, 1, 1, 'BASE', '2021-11-11', 'Иванов', 90, 1, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (2, 'KL', 1.76, 10.11, 500, 15000, 2, 1, 'BASE', '2021-11-11', 'Иванов', 90, 2, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (3, 'KL', 2.74, 9.33, 500, 15000, 2, 1, 'TOP', '2021-11-11', 'Иванов', 90, 2, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (4, 'KL', 0.0, 7.63, 500, 20000, 1, 2, 'BASE', '2021-11-11', 'Иванов', 0, 1, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (5, 'KL', 1.07, 6.71, 500, 20000, 2, 2, 'BASE', '2021-11-11', 'Иванов', 0, 2, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (6, 'KL', 1.65, 6.13, 500, 20000, 2, 2, 'TOP', '2021-11-11', 'Иванов', 0, 2, 1);");


        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (1, 'KL', 0.0, 3.4428, 500, 145000, 1, 1, 'BASE', '2022-03-14', 'Петров', 0, 3, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (2, 'KL', 0.11, 3.34, 500, 145000, 2, 1, 'BASE', '2022-03-14', 'Петров', 0, 4, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (3, 'KL', 0.3225, 3.1314, 500, 145000, 3, 1, 'BASE', '2022-03-14', 'Петров', 0, 5, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (4, 'KL', 0.5303, 2.9275, 500, 145000, 4, 1, 'BASE', '2022-03-14', 'Петров', 0, 6, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (5, 'KL', 0.7389, 2.7136, 500, 145000, 5, 1, 'BASE', '2022-03-14', 'Петров', 0, 7, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (6, 'KL', 0.9625, 2.5014, 500, 145000, 6, 1, 'BASE', '2022-03-14', 'Петров', 0, 8, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (7, 'KL', 1.1772, 2.2867, 500, 145000, 7, 1, 'BASE', '2022-03-14', 'Петров', 0, 9, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (8, 'KL', 1.1725, 2.2864, 500, 145000, 8, 1, 'BASE', '2022-03-14', 'Петров', 0, 10, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (9, 'KL', 1.1722, 2.2847, 500, 145000, 9, 1, 'BASE', '2022-03-14', 'Петров', 0, 11, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (10, 'KL', 1.1722, 2.2861, 500, 145000, 9, 1, 'TOP', '2022-03-14', 'Петров', 0, 11, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (11, 'KL', 0.0, 5.1144, -1500, 94000, 1, 2, 'BASE', '2022-03-14', 'Петров', 240, 3, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (12, 'KL', 0.2428, 4.9008, -1500, 94000, 2, 2, 'BASE', '2022-03-14', 'Петров', 240, 4, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (13, 'KL', 0.5383, 4.6058, -1500, 94000, 3, 2, 'BASE', '2022-03-14', 'Петров', 240, 5, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (14, 'KL', 0.8419, 4.3103, -1500, 94000, 4, 2, 'BASE', '2022-03-14', 'Петров', 240, 6, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (15, 'KL', 1.1492, 4.0069, -1500, 94000, 5, 2, 'BASE', '2022-03-14', 'Петров', 240, 7, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (16, 'KL', 1.4611, 3.71, -1500, 94000, 6, 2, 'BASE', '2022-03-14', 'Петров', 240, 8, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (17, 'KL', 1.7756, 3.3906, -1500, 94000, 7, 2, 'BASE', '2022-03-14', 'Петров', 240, 9, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (18, 'KL', 1.7758, 3.3906, -1500, 94000, 8, 2, 'BASE', '2022-03-14', 'Петров', 240, 10, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (19, 'KL', 1.7756, 3.3908, -1500, 94000, 9, 2, 'BASE', '2022-03-14', 'Петров', 240, 11, 2);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (20, 'KL', 1.7753, 3.3903, -1500, 94000, 9, 2, 'TOP', '2022-03-14', 'Петров', 240, 11, 2);");




        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 1, 1, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 2, 2, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 3, 2, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 4, 1, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 5, 2, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 6, 2, 1);");

        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 7, 1, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 8, 2, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 9, 3, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 10, 4, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 11, 5, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 12, 6, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 13, 7, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 14, 8, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 15, 9, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 16, 9, 2);");

        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 17, 1, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 18, 2, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 19, 3, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 20, 4, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 21, 5, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 22, 6, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 23, 7, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 24, 8, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 25, 9, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 26, 9, 2);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
