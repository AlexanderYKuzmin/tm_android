package com.example.appstraining.towermeasurement.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;

import androidx.core.widget.TextViewCompat;

import com.example.appstraining.towermeasurement.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadBuildingsAdapter extends SimpleAdapter {
    private final String LOG_TAG = "LoadBuildingsAdapter";
    Context mContext;
    ArrayList<Integer> checkedIDs = new ArrayList<>();
    List<Map<String, Object>> data;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public LoadBuildingsAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.cbLoadObject);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkedIDs.add((int)data.get(position).get("id"));
                    Log.d(LOG_TAG, "List changed. Added position :" + position +
                            "\nList size = " + checkedIDs.size());
                } else {
                    checkedIDs.remove(position);
                    Log.d(LOG_TAG, "List changed. Removed position :" + position +
                            "\nList size = " + checkedIDs.size());
                }
            }
        });
        checkBox.setTag(position);
        //checkBox.setChecked();
        return v;
    }

    public ArrayList<Integer> getCheckedIDs() {
        return checkedIDs;
    }
}
