package com.example.appstraining.towermeasurement.model.adapter;

import android.content.Context;
import android.util.Log;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.view.main.fragments.LoadBuildingsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchDialogAdapterHelper {
    Context context;

    public final String LOG_TAG = "SearchDialogAdapterHel";
    public final String ATTRIBUTE_NAME = "name";
    public final String ATTRIBUTE_ADDRESS = "address";
    public final String ATTRIBUTE_ID = "id";
    public final String ATTRIBUTE_CREATION_DATE = "creation date";
    public final String ATTRIBUTE_CHECKED = "checked";

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    String[] from = {ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_ADDRESS, ATTRIBUTE_CREATION_DATE, ATTRIBUTE_CHECKED};
    int[] to = {R.id.valSearchId, R.id.valSearchName, R.id.valSearchAddress, R.id.tvCreationDate, R.id.cbLoadObject};
    ArrayList<Map<String, Object>> data = new ArrayList<>();

    public SearchDialogAdapterHelper(Context context) {
        this.context = context;
    }

    public LoadBuildingsAdapter getAdapter(Map<Long, Building> buildingMap){

        for (Map.Entry<Long, Building> entry : buildingMap.entrySet()){
            Log.d(LOG_TAG, "creation date:" + entry.getValue().getCreationDate());
            java.sql.Date creationDate = entry.getValue().getCreationDate();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_ID, entry.getKey());
            map.put(ATTRIBUTE_NAME, entry.getValue().getName());
            map.put(ATTRIBUTE_ADDRESS, entry.getValue().getAddress());

            if(creationDate == null) map.put(ATTRIBUTE_CREATION_DATE, "");
            else map.put(ATTRIBUTE_CREATION_DATE, dateFormat.format(creationDate));

            data.add(map);
        }
        //return new SimpleAdapter(context, data, R.layout.dialog_item, from, to);
        return new LoadBuildingsAdapter(context, data, R.layout.dialog_item, from, to);
    }
}
