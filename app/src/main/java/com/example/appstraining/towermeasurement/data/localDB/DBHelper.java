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
        db.execSQL("INSERT INTO buildings (b_name, b_address, b_type, b_config, b_numberofsecs, b_height, b_startlevel, b_username, b_creationdate) " +
                "VALUES ('КО094', 'Республика Коми, с. Жешарт', 'TOWER', 4, 4, 40000, 0, 'Попов', '16-03-2022');\n");

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

        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (1, 5400, 4230, 10000, 'КО094', 0, 3);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (2, 4230, 3250, 10000, 'КО094', 10000, 3);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (3, 3250, 2170, 10000, 'КО094', 20000, 3);\n");
        db.execSQL("INSERT INTO sections (s_number, s_widthbottom, s_widthtop, s_height, s_name, s_level, s_b_id) VALUES (4, 2170, 1096, 10000, 'КО094', 30000, 3);\n");


        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (1, 'KL', 0.0, 10.42, 500, 15000, 1, 1, 'BASE', '2021-11-11', 'Иванов', 90, 1, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (2, 'KL', 1.76, 9.11, 500, 15000, 2, 1, 'BASE', '2021-11-11', 'Иванов', 90, 2, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (3, 'KL', 2.74, 8.33, 500, 15000, 2, 1, 'TOP', '2021-11-11', 'Иванов', 90, 2, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (4, 'KL', 0.0, 6.63, 500, 20000, 1, 2, 'BASE', '2021-11-11', 'Иванов', 0, 1, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (5, 'KL', 1.07, 5.71, 500, 20000, 2, 2, 'BASE', '2021-11-11', 'Иванов', 0, 2, 1);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (6, 'KL', 1.65, 5.13, 500, 20000, 2, 2, 'TOP', '2021-11-11', 'Иванов', 0, 2, 1);");

        //measurements Vuhtym
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

        //measurements Zheshart
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (1, 'KL', 0.0, 6.5341, 500, 50000, 1, 1, 'BASE', '2022-03-16', 'Попов', 0, 12, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (2, 'KL', 0.7311, 5.7887, 500, 50000, 2, 1, 'BASE', '2022-03-16', 'Попов', 0, 13, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (3, 'KL', 1.3351, 5.1825, 500, 50000, 3, 1, 'BASE', '2022-03-16', 'Попов', 0, 14, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (4, 'KL', 1.9802, 4.5211, 500, 50000, 4, 1, 'BASE', '2022-03-16', 'Попов', 0, 15, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (5, 'KL', 2.6127, 3.8820, 500, 50000, 4, 1, 'TOP', '2022-03-16', 'Попов', 0, 15, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (1, 'KL', 0.0, 8.2804, 500, 40000, 1, 2, 'BASE', '2022-03-16', 'Попов', 270, 12, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (2, 'KL', 0.9514, 7.3410, 500, 40000, 2, 2, 'BASE', '2022-03-16', 'Попов', 270, 13, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (3, 'KL', 1.7248, 6.5735, 500, 40000, 3, 2, 'BASE', '2022-03-16', 'Попов', 270, 14, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (4, 'KL', 2.5599, 5.7529, 500, 40000, 4, 2, 'BASE', '2022-03-16', 'Попов', 270, 15, 3);");
        db.execSQL("INSERT INTO measures (m_number, m_circle, m_leftangle, m_rightangle, m_theoheight, m_distance, m_sectionnumber, m_side, m_baseortop, m_date, m_contractor, m_sideazimuth, m_s_id, m_b_id) VALUES (5, 'KL', 3.3709, 4.9618, 500, 40000, 4, 2, 'TOP', '2022-03-16', 'Попов', 270, 15, 3);");


        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 1, 1, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 2, 2, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 3, 2, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 4, 1, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 5, 2, 1);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (0.0, 0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 6, 2, 1);");

        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7214, 1.7214, 1.7214, 0.0, 0, 0.0, 145000, 0, 0.0, 3.4428, 1.7214, 0.0, 7, 3, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7250, 1.7250, 1.7250, 0.0036, 9, 0.00006302, 144650, -542, 0.11, 3.34, 1.7250, 13.0, 8, 4, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7269, 1.7269, 1.7269, 0.0056, 14, 0.000096962, 144750, -1084, 0.3225, 3.1314, 1.7269, 20.0, 9, 5, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7289, 1.7289, 1.7289, 0.0075, 19, 0.0001309, 145290, -1625, 0.5303, 2.9275, 1.7289, 27.0, 10, 6, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7263, 1.7263, 1.7263, 0.0049, 12, 0.000084842, 146270, -2160, 0.7389, 2.7136, 1.7263, 18.0, 11, 7, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7319, 1.7319, 1.7319, 0.0106, 27, 0.000184229, 147800, -2716, 0.9625, 2.5014, 1.7319, 38.0, 12, 8, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7319, 1.7319, 1.7319, 0.0106, 28, 0.000184229, 149660, -3250, 1.1772, 2.2867, 1.7319, 38.0, 13, 9, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7294, 1.7294, 1.7294, 0.0081, 21, 0.000140596, 152410, -3250, 1.1725, 2.2864, 1.7294, 29.0, 14, 10, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7285, 1.7285, 1.7285, 0.0071, 19, 0.000123627, 155530, -3250, 1.1722, 2.2847, 1.7285, 26.0, 15, 11, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (1.7292, 1.7292, 1.7292, 0.0078, 22, 0.000135748, 158990, -3250, 1.1722, 2.2861, 1.7292, 28.0, 16, 11, 2);");

        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5572, 2.5572, 2.5572, 0.0, 0, 0.0, 94000, 0, 0.0, 5.1144, 2.5572, 0.0, 17, 3, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5718, 2.5718, 2.5718, 0.0146, 24, 0.000254527, 93940, -542, 0.2428, 4.9008, 2.5718, 53.0, 18, 4, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5721, 2.5721, 2.5721, 0.0149, 25, 0.000259375, 94550, -1084, 0.5383, 4.6058, 2.5721, 54.0, 19, 5, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5761, 2.5761, 2.5761, 0.0189, 32, 0.000329673, 95830, -1625, 0.8419, 4.3103, 2.5761, 68.0, 20, 6, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5781, 2.5781, 2.5781, 0.0208, 36, 0.00036361, 97760, -2160, 1.1492, 4.0069, 2.5781, 75.0, 21, 7, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5856, 2.5856, 2.5856, 0.0283, 50, 0.00049451, 100270, -2716, 1.4611, 3.7100, 2.5856, 102.0, 22, 8, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5831, 2.5831, 2.5831, 0.0258, 47, 0.000450877, 103370, -3250, 1.7756, 3.3906, 2.5831, 93.0, 23, 9, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5832, 2.5832, 2.5832, 0.0260, 49, 0.000453301, 107430, -3250, 1.7758, 3.3906, 2.5832, 94.0, 24, 10, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5832, 2.5832, 2.5832, 0.0260, 51, 0.000453301, 111920, -3250, 1.7756, 3.3908, 2.5832, 94.0, 25, 11, 2);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (2.5828, 2.5828, 2.5828, 0.0256, 52, 0.000446029, 116780, -3250, 1.7753, 3.3903, 2.5828, 92.0, 26, 11, 2);");

        // zheshart test-cube results
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (3.2671, 3.2671, 3.2671, 0.0, 0, 0.0, 50000, 0, 0.0, 6.5342, 3.2671, 0.0, 27, 12, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (3.2599, 3.2599, 3.2599, -0.0072, -6, -0.000126052, 50020, -585, 0.7311, 5.7886, 3.2599, -26.0, 28, 13, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (3.2588, 3.2588, 3.2588, -0.0083, -7, -0.000145444, 51310, -1075, 1.3350, 5.1825, 3.2588, -30.0, 29, 14, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (3.2507, 3.2507, 3.2507, -0.0164, -15, -0.00028604, 53780, -1615, 1.9803, 4.5211, 3.2507, -59.0, 30, 15, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (3.2474, 3.2474, 3.2474, -0.0197, -20, -0.000344218, 57280, -2152, 2.6128, 3.8819, 3.2474, -71.0, 31, 15, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (4.1401, 4.1401, 4.1401, 0.0, 0, 0.0, 40000, 0, 0.0, 8.2803, 4.1401, 0.0, 32, 12, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (4.1463, 4.1463, 4.1463, 0.0061, 4, 0.000106659, 40160, -585, 0.9514, 7.3411, 4.1463,22.0, 33, 13, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (4.1490, 4.1490, 4.1490, 0.0089, 6, 0.00015514, 41890, -1075, 1.7247, 6.5733, 4.1490, 32.0, 34, 14, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (4.1564, 4.1564, 4.1564, 0.0163, 13, 0.000283616, 45000, -1615, 2.56, 5.7528, 4.1564, 59.0, 35, 15, 3);");
        db.execSQL("INSERT INTO results (averageKL, averageKR, averageKLKR, shift_deg, shift_mm, tan_alfa, dist_to_sec, delta_dis, beta_average_left, beta_average_right, beta_i, beta_delta, r_m_id, r_s_id, r_b_id) VALUES (4.1663, 4.1663, 4.1663, 0.0261, 22, 0.000455725, 49240, -2152, 3.3708, 4.9617, 4.1663, 94.0, 36, 15, 3);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
