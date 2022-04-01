package com.example.appstraining.towermeasurement.view.main.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.adapter.SearchDialogAdapterHelper;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;
import com.example.appstraining.towermeasurement.view.main.MainView;

import java.util.Map;

public class SearchDialogFragment extends DialogFragment {
    Context mContext;
    MainView mActivity;
    MainPresenter mMainPresenter;
    SearchDialogAdapterHelper adapterHelper;
    LoadBuildingsAdapter searchDialogSimpleAdapter;
    ListView mLocationList;
    final String LOG_TAG = "SearchDialogFragment";

    int title;
    String name;
    String address;
    Map<Long, Building> buildingMap;

    /*String[] ids;
    String[] names;
    String[] addresses;*/



    public SearchDialogFragment(Context context, MainPresenter presenter,
                                String name, String address,
                                int title) {
    mContext = context;
    mMainPresenter = presenter;
    this.name = name;
    this.address = address;
    this.title = title;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.search_results_fragment_dialog, null);
        //v.setBackgroundColor(getResources().getColor(R.color.sandyLight));
        //buildingMap = mMainPresenter.loadBuildingMap(name, address);

        adapterHelper = new SearchDialogAdapterHelper(mContext);
        searchDialogSimpleAdapter = adapterHelper.getAdapter(buildingMap);



        mLocationList = (ListView)v.findViewById(R.id.lvSearchFragmentDialog);
        mLocationList.setAdapter(searchDialogSimpleAdapter);
        //mLocationList.setOnItemClickListener(this);

        /*CheckBox checkBox= (CheckBox) mLocationList.findViewById(R.id.cbLoadObject);
        checkBox.setOnCheckedChangeListener(this);*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle(title).setView(v);
        //TextView tvTitle = (TextView) titleView.findViewById(R.id.tv_title_search_form_dialog_frag_main);
        builder.setTitle(R.string.inner_search_result_dialog_title_ru)
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long[] checkedIds =
                                searchDialogSimpleAdapter.getCheckedIDs().toArray(new Long[0]);
                        mMainPresenter.saveToLocalDB(checkedIds);
                        //mMainPresenter.loadBuilding(checkedIds[checkedIds.length - 1]); //here check for unselected building
                        mMainPresenter.setBuilding(buildingMap.get(checkedIds[checkedIds.length - 1]));
                        mMainPresenter.mountBuilding(MainActivityMode.SELECTED_OBJECT);
                        dismiss();
                    }
                })
        ;

        return builder.create();
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "ON ITEM CLICK PRESSED");
        TextView tvName = view.findViewById(R.id.valSearchName);
        TextView tvId = view.findViewById(R.id.valSearchId);
        int buildingId = Integer.parseInt(tvId.getText().toString());
        System.out.println("Name: " + tvName.getText().toString());
        mMainPresenter.loadBuilding(buildingId);
        mMainPresenter.mountBuilding(getResources().getString(R.string.title_view_selected));
        *//*mMainPresenter.mountBuilding(buildingMap.get(buildingId)
                , getResources().getString(R.string.title_view_selected));*//*

        dismiss();
    }*/

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
