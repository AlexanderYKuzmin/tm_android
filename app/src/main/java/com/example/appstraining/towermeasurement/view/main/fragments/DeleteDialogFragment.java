package com.example.appstraining.towermeasurement.view.main.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.adapter.SearchDialogAdapterHelper;
import com.example.appstraining.towermeasurement.model.Building;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;
import com.example.appstraining.towermeasurement.view.main.MainView;

import java.util.Map;

public class DeleteDialogFragment extends DialogFragment {
    private Context context;
    private MainPresenter mainPresenter;
    private MainView mainActivity;

    SearchDialogAdapterHelper adapterHelper;
    LoadBuildingsAdapter deleteDialogSimpleAdapter;
    ListView mLocationList;

    Map<Long, Building> buildingMap;

    public DeleteDialogFragment(Context context, MainPresenter mainPresenter, MainView mainActivity) {
        this.context = context;
        this.mainPresenter = mainPresenter;
        this.mainActivity = mainActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.search_results_fragment_dialog, null);

        buildingMap = mainPresenter.getBuildingMapFromLocal();

        adapterHelper = new SearchDialogAdapterHelper(context);
        deleteDialogSimpleAdapter = adapterHelper.getAdapter(buildingMap);

        mLocationList = (ListView)v.findViewById(R.id.lvSearchFragmentDialog);
        mLocationList.setAdapter(deleteDialogSimpleAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCustomTitle(getCustomTitle())
                .setView(v)
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Long[] checkedIds =
                                deleteDialogSimpleAdapter.getCheckedIDs().toArray(new Long[0]);

                        Toast.makeText(context,
                                String.format("Удалено %d записей", mainPresenter.deleteFromLocalDB(checkedIds)), Toast.LENGTH_LONG).show();
                        //mMainPresenter.loadBuilding(checkedIds[checkedIds.length - 1]); //here check for unselected building
                        //mMainPresenter.setBuilding(buildingMap.get(checkedIds[checkedIds.length - 1]));
                        //mMainPresenter.mountBuilding(MainActivityMode.SELECTED_OBJECT);
                        dismiss();
                    }
                });

        return  builder.create();
    }

    @SuppressLint("ResourceAsColor")
    private TextView getCustomTitle() {
        TextView title = new TextView(context);
        title.setText(R.string.inner_search_result_dialog_title_ru);
        title.setTextSize(24);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setBackgroundColor(R.color.light_blue_600);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);

        return title;
    }
}
