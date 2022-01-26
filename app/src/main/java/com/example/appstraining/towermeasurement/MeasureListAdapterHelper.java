package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.model.DegreeSeparated;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasureListAdapterHelper {
    final String LOG_TAG = "MeasureListAdapter";
    private Context context;
    ArrayList<Map<String, Object>> data = new ArrayList<>();

    public static final String ATTRIBUTE_MEASURE_NUMBER = "measure_number";
    public static final String ATTRIBUTE_LEFTANGLE_DD = "left_angle_deg";
    public static final String ATTRIBUTE_LEFTANGLE_MM = "left_angle_min";
    public static final String ATTRIBUTE_LEFTANGLE_SS = "left_angle_sec";
    public static final String ATTRIBUTE_RIGHTANGLE_DD = "right_angle_deg";
    public static final String ATTRIBUTE_RIGHTANGLE_MM = "right_angle_min";
    public static final String ATTRIBUTE_RIGHTANGLE_SS = "right_angle_sec";
    public static final String ATTRIBUTE_SIDE = "side";
    public static final String ATTRIBUTE_AZIMUTH = "azimuth";
    public static final String ATTRIBUTE_BASEORTOP = "base_or_top";
    public static final String ATTRIBUTE_SECTION_NUMBER = "section_number";
    public static final String ATTRIBUTE_THEO_DISTANCE = "theo_distance";
    public static final String ATTRIBUTE_THEO_HEIGHT = "theo_height";
    // private static final String ATTRIBUTE_SECTION_TOP_LEVEL = "section_top_level";


    // Array of attr names, from which data are reading
    String[] from = { ATTRIBUTE_MEASURE_NUMBER, ATTRIBUTE_LEFTANGLE_DD,
            ATTRIBUTE_LEFTANGLE_MM, ATTRIBUTE_LEFTANGLE_SS, ATTRIBUTE_RIGHTANGLE_DD,
            ATTRIBUTE_RIGHTANGLE_MM, ATTRIBUTE_RIGHTANGLE_SS, ATTRIBUTE_SIDE, ATTRIBUTE_AZIMUTH,
            ATTRIBUTE_BASEORTOP, ATTRIBUTE_SECTION_NUMBER, ATTRIBUTE_THEO_DISTANCE,
            ATTRIBUTE_THEO_HEIGHT
    };
    // Array ID View-components, to which data are input
    int[] to = { R.id.tvMeasureNumber, R.id.etLeftDD, R.id.etLeftMM, R.id.etLeftSS,
            R.id.etRightDD, R.id.etRightMM, R.id.etRightSS, R.id.tvSide, R.id.tvAzimuth,
            R.id.tvBaseOrTop, R.id.tvMeasureSecNum, R.id.tvTheoDistance, R.id.tvTheoHeight
    };

    public MeasureListAdapterHelper(Context context) { this.context = context; }

    public SimpleAdapter getAdapter(List<Measurement> measurements,
                                    ArrayList<int[]> leftListDeg, ArrayList<int[]> rightListDeg){
        data = new ArrayList<Map<String, Object>>(measurements.size());
        Map<String, Object> m;
        for (int i = 0; i < measurements.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_MEASURE_NUMBER, i + 1);
            m.put(ATTRIBUTE_LEFTANGLE_DD, leftListDeg.get(i)[0] + "\u00B0");
            m.put(ATTRIBUTE_LEFTANGLE_MM, leftListDeg.get(i)[1] + "\'");
            m.put(ATTRIBUTE_LEFTANGLE_SS, leftListDeg.get(i)[2] + "\"");
            m.put(ATTRIBUTE_RIGHTANGLE_DD, rightListDeg.get(i)[0] + "\u00B0");
            m.put(ATTRIBUTE_RIGHTANGLE_MM, rightListDeg.get(i)[1] + "\'") ;
            m.put(ATTRIBUTE_RIGHTANGLE_SS, rightListDeg.get(i)[2] + "\"");
            m.put(ATTRIBUTE_SIDE, measurements.get(i).getSide());
            m.put(ATTRIBUTE_AZIMUTH, measurements.get(i).getAzimuth());
            m.put(ATTRIBUTE_BASEORTOP, String.valueOf(measurements.get(i).getBaseOrTop()));
            m.put(ATTRIBUTE_SECTION_NUMBER, measurements.get(i).getSectionNumber());
            m.put(ATTRIBUTE_THEO_DISTANCE, measurements.get(i).getDistance());
            m.put(ATTRIBUTE_THEO_HEIGHT, measurements.get(i).getTheoHeight());
            data.add(m);
        }
        return new SimpleAdapter(context, data, R.layout.measure_lv_item, from, to);
    }

    public void updateAdapter(List<Measurement> measurements,
                              List<DegreeSeparated> degreeSeparatedList) {
        //data = new ArrayList<Map<String, Integer>>(sections.size());
        Map<String, Object> m;
        for (int i = 0; i < measurements.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_MEASURE_NUMBER, measurements.get(i).getNumber());
            m.put(ATTRIBUTE_LEFTANGLE_DD, degreeSeparatedList.get(i).getDegreeLeft() + "\u00B0");
            m.put(ATTRIBUTE_LEFTANGLE_MM, degreeSeparatedList.get(i).getMinuteLeft() + "\'");
            m.put(ATTRIBUTE_LEFTANGLE_SS, degreeSeparatedList.get(i).getSecondLeft() + "\"");
            m.put(ATTRIBUTE_RIGHTANGLE_DD, degreeSeparatedList.get(i).getDegreeRight() + "\u00B0");
            m.put(ATTRIBUTE_RIGHTANGLE_MM, degreeSeparatedList.get(i).getMinuteRight() + "\'") ;
            m.put(ATTRIBUTE_RIGHTANGLE_SS, degreeSeparatedList.get(i).getSecondRight() + "\"");
            m.put(ATTRIBUTE_SIDE, measurements.get(i).getSide());
            m.put(ATTRIBUTE_AZIMUTH, measurements.get(i).getAzimuth());
            m.put(ATTRIBUTE_BASEORTOP, String.valueOf(measurements.get(i).getBaseOrTop()));
            m.put(ATTRIBUTE_SECTION_NUMBER, measurements.get(i).getSectionNumber());
            m.put(ATTRIBUTE_THEO_DISTANCE, measurements.get(i).getDistance());
            m.put(ATTRIBUTE_THEO_HEIGHT, measurements.get(i).getTheoHeight());
            data.set(i, m);
        }
    }

    public void oneItemUpdateAdapter(Measurement measurement, DegreeSeparated degreeSeparated) {
        Map<String, Object> m = new HashMap<>();
        m.put(ATTRIBUTE_MEASURE_NUMBER, measurement.getNumber());
        m.put(ATTRIBUTE_LEFTANGLE_DD, degreeSeparated.getDegreeLeft() + "\u00B0");
        m.put(ATTRIBUTE_LEFTANGLE_MM, degreeSeparated.getMinuteLeft() + "\'");
        m.put(ATTRIBUTE_LEFTANGLE_SS, degreeSeparated.getSecondLeft() + "\"");
        m.put(ATTRIBUTE_RIGHTANGLE_DD, degreeSeparated.getDegreeRight() + "\u00B0");
        m.put(ATTRIBUTE_RIGHTANGLE_MM, degreeSeparated.getMinuteRight() + "\'") ;
        m.put(ATTRIBUTE_RIGHTANGLE_SS, degreeSeparated.getSecondRight() + "\"");
        m.put(ATTRIBUTE_SIDE, measurement.getSide());
        m.put(ATTRIBUTE_AZIMUTH, measurement.getAzimuth() + "\u00b0");
        m.put(ATTRIBUTE_BASEORTOP, String.valueOf(measurement.getBaseOrTop()));
        m.put(ATTRIBUTE_SECTION_NUMBER, measurement.getSectionNumber());
        m.put(ATTRIBUTE_THEO_DISTANCE, measurement.getDistance());
        m.put(ATTRIBUTE_THEO_HEIGHT, measurement.getTheoHeight());
        data.set(measurement.getNumber() - (measurement.getSide() == 1 ? 1 : 11), m);
        /*Log.d(LOG_TAG, "List data is updated, set data position: "
                + (measurement.getNumber() - 1));*/
        /*Log.d(LOG_TAG, "LEFT ANGLE :"
                + data.get(measurement.getNumber() - 1).get(ATTRIBUTE_LEFTANGLE_DD) + " : "
                + data.get(measurement.getNumber() - 1).get(ATTRIBUTE_LEFTANGLE_MM) + " : "
                + data.get(measurement.getNumber() - 1).get(ATTRIBUTE_LEFTANGLE_SS) + " : "
        );
        Log.d(LOG_TAG, "RIGHT ANGLE :"
                + data.get(measurement.getNumber() - 1).get(ATTRIBUTE_RIGHTANGLE_DD) + " : "
                + data.get(measurement.getNumber() - 1).get(ATTRIBUTE_RIGHTANGLE_MM) + " : "
                + data.get(measurement.getNumber() - 1).get(ATTRIBUTE_RIGHTANGLE_SS) + " : "
        );*/
    }
    // ***********************************
    public SimpleAdapter getAdapter2(List<Measurement> measurements,
                                     List<DegreeSeparated> degreeSeparatedList){
        data = new ArrayList<Map<String, Object>>(measurements.size());
        Map<String, Object> m;
        for (int i = 0; i < measurements.size(); i++) {
            m = new HashMap<String, Object>();
            m.put(ATTRIBUTE_MEASURE_NUMBER, measurements.get(i).getNumber());
            m.put(ATTRIBUTE_LEFTANGLE_DD, degreeSeparatedList.get(i).getDegreeLeft() + "\u00B0");
            m.put(ATTRIBUTE_LEFTANGLE_MM, degreeSeparatedList.get(i).getMinuteLeft() + "\'");
            m.put(ATTRIBUTE_LEFTANGLE_SS, degreeSeparatedList.get(i).getSecondLeft() + "\"");
            m.put(ATTRIBUTE_RIGHTANGLE_DD, degreeSeparatedList.get(i).getDegreeRight() + "\u00B0");
            m.put(ATTRIBUTE_RIGHTANGLE_MM, degreeSeparatedList.get(i).getMinuteRight() + "\'") ;
            m.put(ATTRIBUTE_RIGHTANGLE_SS, degreeSeparatedList.get(i).getSecondRight() + "\"");
            m.put(ATTRIBUTE_SIDE, measurements.get(i).getSide());
            m.put(ATTRIBUTE_AZIMUTH, measurements.get(i).getAzimuth());
            m.put(ATTRIBUTE_BASEORTOP, String.valueOf(measurements.get(i).getBaseOrTop()));
            m.put(ATTRIBUTE_SECTION_NUMBER, measurements.get(i).getSectionNumber());
            m.put(ATTRIBUTE_THEO_DISTANCE, measurements.get(i).getDistance());
            m.put(ATTRIBUTE_THEO_HEIGHT, measurements.get(i).getTheoHeight());
            data.add(m);
        }
        return new SimpleAdapter(context, data, R.layout.measure_lv_item, from, to);
    }
}
