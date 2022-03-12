package com.example.appstraining.towermeasurement.view.measurement.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.MeasureListAdapterHelper;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.DegreeSeparated;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.view.measurement.MeasureInputActivity;
import com.example.appstraining.towermeasurement.view.measurement.MeasureInputPresenter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MeasureGroupFragment extends Fragment implements MeasureGroup, AdapterView.OnItemClickListener {
    private final String LOG_TAG = "MeasureGroupFragment";

    public static final String ARG_GROUP = "group";
    public static final String ARG_MEASUREMENTS = "measurements";
    public static final String ARG_DEGREES = "degrees";

    private MeasureInputActivity activity;
    private MeasureInputPresenter presenter;

    private MeasureListAdapterHelper adapterHelper;
    private SimpleAdapter adapter;
    private int group = 0;
    private int startPosSecondGroup;
    private int[] defaultValues;
    private int[] dhaValues = new int[3]; // distance, height, azimuth

    EditText etTheoDistance;
    EditText etTheoHeight;
    EditText etAzimuth;
    EditText etSide;
    ListView lv;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MeasureGroupFragment() {
    }

    public static MeasureGroupFragment newInstance(int group) {
        MeasureGroupFragment fragment = new MeasureGroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GROUP, group);
        fragment.setArguments(args);
        Log.d("MeasureFragmNewInstance", "group = " + group + " something else");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "on create");
        if (getArguments() != null) {
            group = getArguments().getInt(ARG_GROUP);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measure_group_list, container, false);

        activity = (MeasureInputActivity) getActivity();
        presenter = activity.getPresenter();

        List<Measurement> measurementsGroup = presenter.getMeasurementGroup(group);

        startPosSecondGroup = measurementsGroup.size() + 1;

        etTheoDistance = view.findViewById(R.id.etTheoDistance_measureGroup);
        etTheoDistance.setText(String.valueOf(measurementsGroup.get(0).getDistance()));

        etTheoHeight = view.findViewById(R.id.etTheoHeight_measureGroup);
        etTheoHeight.setText(String.valueOf(measurementsGroup.get(0).getTheoHeight()));

        etAzimuth = view.findViewById(R.id.etAzimuth_measureGroup);
        etAzimuth.setText(String.valueOf(measurementsGroup.get(0).getAzimuth()));

        etSide = view.findViewById(R.id.etSide_measureGroup);
        etSide.setText(String.valueOf(measurementsGroup.get(0).getSide()));

        lv = view.findViewById(R.id.lvMeasures_GroupData);

        // Set the adapter
        adapterHelper = new MeasureListAdapterHelper(getContext());

        adapter = adapterHelper.getAdapter2(measurementsGroup,
                presenter.getDegreeSeparatedAngleListByGroup(group));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

        fillDhaValues();  // fill array of constants (theoDistance, theoHeight, azimuth)

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "OnItemClick pressed!");

        fillDhaValues();

        int measurementNumber = group == 1 ? position + 1 : position + startPosSecondGroup;

        activity.showMeasureDialogFragment(measurementNumber, group, dhaValues);
    }

    public void updateMeasureList() {
        /*adapterHelper.updateAdapter(measureInputPresenter.getMeasurements(),
                measureInputPresenter.getDegMinSecArrList(LEFT_LIST),
                measureInputPresenter.getDegMinSecArrList(RIGHT_LIST));
        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void onItemUpdateMeasureList(Measurement measurement, DegreeSeparated degreeSeparated) {
        adapterHelper.oneItemUpdateAdapter(measurement, degreeSeparated, startPosSecondGroup);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateAdapter(List<Measurement> measurements, List<DegreeSeparated> degreeSeparatedList) {
        adapterHelper.updateAdapter(measurements, degreeSeparatedList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fillDhaValues() {
        if(etTheoDistance.getText() != null)  dhaValues[0] = Integer.parseInt(etTheoDistance.getText().toString());
        if(etTheoHeight.getText() != null)  dhaValues[1] = Integer.parseInt(etTheoHeight.getText().toString());
        if(etAzimuth.getText() != null)  dhaValues[2] = Integer.parseInt(etAzimuth.getText().toString());
    }

    @Override
    public int[] getDhaValues() {
        return dhaValues;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause() {
        super.onPause();
        presenter.setDhaToMeasurements(group, dhaValues);
    }
}