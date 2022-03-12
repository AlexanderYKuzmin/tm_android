package com.example.appstraining.towermeasurement.view.main.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.InnerSearchDialogAdapterHelper;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.view.main.MainView;

import java.util.Map;

public class InnerSearchResultDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    Context context;
    MainPresenter mainPresenter;
    MainView mainActivity;

    InnerSearchDialogAdapterHelper adapterHelper;
    SimpleAdapter searchDialogSimpleAdapter;
    ListView mLocationList;
    final String LOG_TAG = "InnerSearchDF";
    Map<Long, Building> buildingMap;

    //int title;
    /*String name;
    String address;*/

    /*public InnerSearchResultDialogFragment(Context context, int title, String name, String address,
                                           MainPresenter mainPresenter) {
        this.mContext = context;
        this.title = title;
        this.name = name;
        this.address = address;
        this.mMainPresenter = mainPresenter;
    }*/
    public InnerSearchResultDialogFragment(Context context, MainPresenter mainPresenter, MainView mainActivity) {
        this.context = context;
        this.mainPresenter = mainPresenter;
        this.mainActivity = mainActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.inner_search_dialog_frgament, null);
        Log.d(LOG_TAG, "mainPresenter = " + mainPresenter);
        buildingMap = mainPresenter.getBuildingMapFromLocal();

        int title;
        if (buildingMap.isEmpty() || buildingMap == null) {
            Log.d(LOG_TAG, "Building Map is EMPTY");
            mainActivity.showMessage();
            //dismiss();
            title = R.string.no_object_found;
        } else {
            /*Log.d(LOG_TAG, "Building map is recieved. First building : "
                    + buildingMap.get(1).getName() + "\n"
                    + "measurement.getLeftAngle = " + buildingMap.get(1).getMeasurements().get(4).getLeftAngle() + "\n"
                    + "measurement.getRightAngle = " + buildingMap.get(1).getMeasurements().get(4).getLeftAngle());*/

            title = R.string.inner_search_result_dialog_title_ru;
            adapterHelper = new InnerSearchDialogAdapterHelper(context);
            searchDialogSimpleAdapter = adapterHelper.getAdapter(buildingMap);
            mLocationList = (ListView) v.findViewById(R.id.lvOnnerSearchFD);
            mLocationList.setAdapter(searchDialogSimpleAdapter);
            mLocationList.setOnItemClickListener(this);

        }

        TextView tvTitle = new TextView(context);
        tvTitle.setText(title);
        tvTitle.setTextSize(24);
        tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        tvTitle.setBackgroundColor(getResources().getColor(R.color.green_200));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(getResources().getColor(R.color.black));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(tvTitle)
                .setView(v);

        if(buildingMap.isEmpty()) {
            builder.setPositiveButton(R.string.btn_search_form_main_ok_ru, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });
        }
        return builder.create();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "onItemClick pressed");
        TextView building_id_txt = (TextView) view.findViewById(R.id.valSearchId_inner);
        long building_id = Long.parseLong(building_id_txt.getText().toString());
        Log.d(LOG_TAG, "building_id = " + building_id);

        mainPresenter.setBuilding(buildingMap.get(building_id));
        mainPresenter.setPatternSections(buildingMap.get(building_id).getSections());
        // change smth
        if (mainActivity.getActivityMode() == MainActivityMode.USE_TEMPLATE) {
            mainPresenter.mountBuilding(MainActivityMode.USE_TEMPLATE);
        } else {
            mainPresenter.mountBuilding(MainActivityMode.SELECTED_OBJECT);
        }

        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
