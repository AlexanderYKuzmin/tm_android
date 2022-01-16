package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.model.Building;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InnerSearchDialogAdapterHelper {
    Context context;

    public final String LOG_TAG = "InnerSearchDialogAH";
    public final String ATTRIBUTE_NAME = "name";
    public final String ATTRIBUTE_ADDRESS = "address";
    public final String ATTRIBUTE_ID = "id";
    public final String ATTRIBUTE_CREATION_DATE = "creation date";

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    String[] from = {ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_ADDRESS, ATTRIBUTE_CREATION_DATE};
    int[] to = {R.id.valSearchId_inner, R.id.valSearchName_inner, R.id.valSearchAddress_inner,
            R.id.tvCreationDate_inner};

    ArrayList<Map<String, Object>> data = new ArrayList<>();

    public InnerSearchDialogAdapterHelper(Context context) {
        this.context = context;
    }

    public SimpleAdapter getAdapter(Map<Integer, Building> buildingMap){
        for (Map.Entry<Integer, Building> entry : buildingMap.entrySet()){
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
        return new SimpleAdapter(context, data, R.layout.inner_search_dialog_item, from, to);
    }
}








