package com.example.appstraining.towermeasurement;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;

import androidx.annotation.RequiresApi;

import com.example.appstraining.towermeasurement.model.Building;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InnerSearchDialogAdapterHelper {
    Context context;

    public final String LOG_TAG = "InnerSearchDialogAH";

    public final String ATTRIBUTE_NAME_LL = "layout_name";
    public final String ATTRIBUTE_NAME = "name";
    public final String ATTRIBUTE_ADDRESS = "address";
    public final String ATTRIBUTE_ID = "id";
    public final String ATTRIBUTE_CREATION_DATE = "creation date";

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    String[] from = {ATTRIBUTE_NAME_LL, ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_ADDRESS, ATTRIBUTE_CREATION_DATE};
    int[] to = {R.id.layout_inner_search_item, R.id.valSearchId_inner, R.id.valSearchName_inner, R.id.valSearchAddress_inner,
            R.id.tvCreationDate_inner};

    ArrayList<Map<String, Object>> data = new ArrayList<>();

    public InnerSearchDialogAdapterHelper(Context context) {
        this.context = context;
    }

    public SimpleAdapter getAdapter(Map<Long, Building> buildingMap){
        for (Map.Entry<Long, Building> entry : buildingMap.entrySet()){
            Log.d(LOG_TAG, "creation date:" + entry.getValue().getCreationDate());
            java.sql.Date creationDate = entry.getValue().getCreationDate();

            Map<String, Object> map = new HashMap<>();
            map.put(ATTRIBUTE_NAME_LL, entry.getKey());
            map.put(ATTRIBUTE_ID, entry.getKey());
            map.put(ATTRIBUTE_NAME, entry.getValue().getName());
            map.put(ATTRIBUTE_ADDRESS, entry.getValue().getAddress());

            if(creationDate == null) map.put(ATTRIBUTE_CREATION_DATE, "");
            else map.put(ATTRIBUTE_CREATION_DATE, dateFormat.format(creationDate));

            data.add(map);
        }
        //return new SimpleAdapter(context, data, R.layout.dialog_item, from, to);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, data, R.layout.inner_search_dialog_item, from, to);
        SimpleAdapter.ViewBinder binder = new SimpleAdapter.ViewBinder() {
            private int count = 0;
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                /*Log.d(LOG_TAG, "setViewValues view.getId = " + view.getId()
                + "     data = " + data.toString());*/
                if (view.getId() == R.id.layout_inner_search_item) {
                    /*Log.d(LOG_TAG, "count = " + count);
                    Log.d(LOG_TAG, "data = " + data);
*/
                    if ((Long) data % 2 != 0) {
                        Log.d(LOG_TAG, "changing color.");
                        view.setBackground(context.getDrawable(R.color.lite_sky));
                    }
                    return true;
                }
                return false;
            }
        };
        simpleAdapter.setViewBinder(binder);
        return simpleAdapter;
    }
}








