package com.example.appstraining.towermeasurement.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.InnerSearchDialogAdapterHelper;
import com.example.appstraining.towermeasurement.MainPresenter;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.SearchDialogAdapterHelper;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.LoadBuildingsAdapter;
import com.example.appstraining.towermeasurement.model.MainActivityMode;

import java.util.Map;

public class InnerSearchDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    Context mContext;
    MainPresenter mMainPresenter;
    InnerSearchDialogAdapterHelper adapterHelper;
    SimpleAdapter searchDialogSimpleAdapter;
    ListView mLocationList;
    final String LOG_TAG = "InnerSearchDF";
    Map<Integer, Building> buildingMap;

    int title;
    String name;
    String address;

    public InnerSearchDialogFragment(Context context, int title, String name, String address,
                                     MainPresenter mainPresenter) {
        this.mContext = context;
        this.title = title;
        this.name = name;
        this.address = address;
        this.mMainPresenter = mainPresenter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.inner_search_dialog_frgament, null);

        buildingMap = mMainPresenter.getBuildingMapFromLocal(name, address);
        Log.d(LOG_TAG, "Building map is recieved. First building : "
                + buildingMap.get(1).getName() + "\n"
                + "measurement.getLeftAngle = " + buildingMap.get(1).getMeasurements().get(4).getLeftAngle() + "\n"
                + "measurement.getRightAngle = " + buildingMap.get(1).getMeasurements().get(4).getLeftAngle());

        adapterHelper = new InnerSearchDialogAdapterHelper(mContext);
        searchDialogSimpleAdapter = adapterHelper.getAdapter(buildingMap);
        mLocationList = (ListView)v.findViewById(R.id.lvOnnerSearchFD);
        mLocationList.setAdapter(searchDialogSimpleAdapter);
        mLocationList.setOnItemClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setView(v);

        return builder.create();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "onItemClick pressed");
        TextView building_id_txt = (TextView) view.findViewById(R.id.valSearchId_inner);
        int building_id = Integer.parseInt(building_id_txt.getText().toString());
        Log.d(LOG_TAG, "building_id = " + building_id);
        mMainPresenter.setBuilding(buildingMap.get(building_id));
        mMainPresenter.mountBuilding(MainActivityMode.SELECTED_OBJECT);
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
