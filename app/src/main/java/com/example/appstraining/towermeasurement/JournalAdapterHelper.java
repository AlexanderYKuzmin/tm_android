package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JournalAdapterHelper {
    private final String LOG_TAG = "JournalAdapterHelper";
    private Context context;
    ArrayList<Map<String, Object>> data = new ArrayList<>();

    public static final String ATTRIBUTE_NUMBER = "number";
    public static final String ATTRIBUTE_LEVEL = "level";
    public static final String ATTRIBUTE_LEFT = "left";
    public static final String ATTRIBUTE_RIGHT = "right";
    public static final String ATTRIBUTE_AVERAGE = "average";
    public static final String ATTRIBUTE_DEVIATION_DEG = "dev_deg";
    public static final String ATTRIBUTE_DEVIATION_MM = "dev_mm";

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NUMBER, ATTRIBUTE_LEVEL, ATTRIBUTE_LEFT,
            ATTRIBUTE_RIGHT, ATTRIBUTE_AVERAGE, ATTRIBUTE_DEVIATION_DEG, ATTRIBUTE_DEVIATION_MM };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.tvNo_journal, R.id.tvLevel_journal, R.id.tvLeft_journal, R.id.tvRight_journal,
            R.id.tvAverage_journal, R.id.tvDevDeg_journal, R.id.tvDevMm_journal };

    public JournalAdapterHelper(Context context){
        this.context = context;
    }

    public SimpleAdapter getAdapter(int[] levels, Result[] results, Measurement[] measurements) {
        data = new ArrayList<Map<String, Object>>();

        for(int i = 0; i < levels.length; i++) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_NUMBER, i);
            m.put(ATTRIBUTE_LEVEL, levels[i]);
            m.put(ATTRIBUTE_LEFT, measurements[i].getLeftAngle());
            m.put(ATTRIBUTE_RIGHT, measurements[i].getRightAngle());
            m.put(ATTRIBUTE_AVERAGE, results[i].getAverageKLKR());
            m.put(ATTRIBUTE_DEVIATION_DEG, results[i].getShiftDegree());
            m.put(ATTRIBUTE_DEVIATION_MM, results[i].getShiftMm());
            data.add(m);
            Log.d(LOG_TAG, "ATTRIBUTE_NUMBER = " + i);
            Log.d(LOG_TAG, "ATTRIBUTE_LEVEL = " + levels[i]);
            Log.d(LOG_TAG, "ATTRIBUTE_LEFT = " + measurements[i].getLeftAngle());
            Log.d(LOG_TAG, "ATTRIBUTE_DEVIATION_DEG = " + results[i].getShiftDegree());
        }
        Log.d(LOG_TAG, "data for SimpleAdapter data.get(0).get(ATTRIBUTE_LEVEL): " +
                data.get(0).get(ATTRIBUTE_LEVEL));
        Log.d(LOG_TAG, "data for SimpleAdapter data.get(4).get(ATTRIBUTE_LEVEL): " +
                data.get(4).get(ATTRIBUTE_LEVEL));

        return new SimpleAdapter(context, data, R.layout.fragment_journal_list_item, from, to);
    }
}
