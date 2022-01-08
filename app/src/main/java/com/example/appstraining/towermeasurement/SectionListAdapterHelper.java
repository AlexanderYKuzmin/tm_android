package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.model.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SectionListAdapterHelper {
    private final String LOG_TAG = "SectionAH";
    private Context context;
    ArrayList<Map<String, Integer>> data = new ArrayList<>();
    public static final String ATTRIBUTE_NAME_LL = "lenearlayout_name";
    public static final String ATTRIBUTE_SECTION_NUMBER = "section_number";
    public static final String ATTRIBUTE_SECTION_BASE_WIDTH = "section_base_width";
    public static final String ATTRIBUTE_SECTION_TOP_WIDTH = "section_top_width";
    public static final String ATTRIBUTE_SECTION_HEIGHT = "section_height";
   // private static final String ATTRIBUTE_SECTION_TOP_LEVEL = "section_top_level";

    // массив имен атрибутов, из которых будут читаться данные
    String[] from = { ATTRIBUTE_NAME_LL, ATTRIBUTE_SECTION_NUMBER, ATTRIBUTE_SECTION_BASE_WIDTH,
            ATTRIBUTE_SECTION_TOP_WIDTH, ATTRIBUTE_SECTION_HEIGHT };
    // массив ID View-компонентов, в которые будут вставлять данные
    int[] to = { R.id.llSection, R.id.tvSecNumber, R.id.valBaseWidth, R.id.valTopWidth, R.id.valSecHeight };

    public SectionListAdapterHelper(Context context){
        this.context = context;
    }

    public SimpleAdapter getAdapter() {
        data = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put(ATTRIBUTE_NAME_LL, 1);
        m.put(ATTRIBUTE_SECTION_NUMBER, 1);
        m.put(ATTRIBUTE_SECTION_BASE_WIDTH, 0);
        m.put(ATTRIBUTE_SECTION_TOP_WIDTH, 0);
        m.put(ATTRIBUTE_SECTION_HEIGHT, 0);
        data.add(m);
        return new SimpleAdapter(context, data, R.layout.section_list_item, from, to);
    }

    public SimpleAdapter getAdapter(ArrayList<Section> sections){
        data = new ArrayList<Map<String, Integer>>(sections.size());
        Map<String, Integer> m;
        for (int i = 0; i < sections.size(); i++) {
            m = new HashMap<String, Integer>();
            m.put(ATTRIBUTE_SECTION_NUMBER, i + 1);
            m.put(ATTRIBUTE_SECTION_BASE_WIDTH, sections.get(i).getWidthBottom());
            m.put(ATTRIBUTE_SECTION_TOP_WIDTH, sections.get(i).getWidthTop());
            m.put(ATTRIBUTE_SECTION_HEIGHT, sections.get(i).getHeight());
            data.add(m);
        }
        return new SimpleAdapter(context, data, R.layout.section_list_item, from, to);
    }

    public void updateAdapter() {
        data.clear();
        Log.d(LOG_TAG, "updateAdapter(). sections.size() = " + "default");
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put(ATTRIBUTE_NAME_LL, 1);
        m.put(ATTRIBUTE_SECTION_NUMBER, 1);
        m.put(ATTRIBUTE_SECTION_BASE_WIDTH, 0);
        m.put(ATTRIBUTE_SECTION_TOP_WIDTH, 0);
        m.put(ATTRIBUTE_SECTION_HEIGHT, 0);
        data.add(m);
    }

    public void updateAdapter(ArrayList<Section> sections) {
        //data = new ArrayList<Map<String, Integer>>(sections.size());
        data.clear();
        Log.d(LOG_TAG, "updateAdapter. sections.size() = " + sections.size());
        Map<String, Integer> m;
        for (int i = 0; i < sections.size(); i++) {
            m = new HashMap<String, Integer>();
            m.put(ATTRIBUTE_NAME_LL, i + 1);
            m.put(ATTRIBUTE_SECTION_NUMBER, i + 1);
            m.put(ATTRIBUTE_SECTION_BASE_WIDTH, sections.get(i).getWidthBottom());
            m.put(ATTRIBUTE_SECTION_TOP_WIDTH, sections.get(i).getWidthTop());
            m.put(ATTRIBUTE_SECTION_HEIGHT, sections.get(i).getHeight());

            if(i < data.size()) data.set(i, m);
            else  data.add(m);
        }
    }

    public ArrayList<Map<String, Integer>> getData(){

        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put(ATTRIBUTE_SECTION_NUMBER, 1);
        m.put(ATTRIBUTE_SECTION_BASE_WIDTH, 0);
        m.put(ATTRIBUTE_SECTION_TOP_WIDTH, 0);
        m.put(ATTRIBUTE_SECTION_HEIGHT, 0);
        data.add(m);
        return data;
    }

    public ArrayList<Map<String, Integer>> getData(ArrayList<Section> sections){
        Map<String, Integer> m;
        for (int i = 0; i < sections.size(); i++) {
            m = new HashMap<String, Integer>();
            m.put(ATTRIBUTE_SECTION_NUMBER, i + 1);
            m.put(ATTRIBUTE_SECTION_BASE_WIDTH, sections.get(i).getWidthBottom());
            m.put(ATTRIBUTE_SECTION_TOP_WIDTH, sections.get(i).getWidthTop());
            m.put(ATTRIBUTE_SECTION_HEIGHT, sections.get(i).getHeight());

            data.add(m);
        }
        return data;
    }
}
