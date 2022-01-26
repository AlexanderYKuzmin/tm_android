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

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class MeasureGroupFragment extends Fragment implements MeasureGroup, AdapterView.OnItemClickListener {
    private final String LOG_TAG = "MeasureGroupFragment";
    // TODO: Customize parameter argument names
    public static final String ARG_GROUP = "group";
    public static final String ARG_MEASUREMENTS = "measurements";
    public static final String ARG_DEGREES = "degrees";
    // TODO: Customize parameters
    private MeasureInputActivity activity;
    private MeasureInputPresenter presenter;

    private MeasureListAdapterHelper adapterHelper;
    private SimpleAdapter adapter;
    private int group = 0;
    int[] defaultValues;
    //private ArrayList<Measurement> measurementsGroup;
    //private ArrayList<DegreeSeparated> degreeSeparatedList;

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

    /*public MeasureGroupFragment(Context context,
                                MeasureInputPresenter measureInputPresenter, int side, List groupMeasurements) {
        this.context = context;
        this.measureInputActivity = measureInputActivity;
        this.measureInputPresenter = measureInputPresenter;
        this.side = side;
        this.groupMeasurements = groupMeasurements;
    }*/

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MeasureGroupFragment newInstance(int group) {
        MeasureGroupFragment fragment = new MeasureGroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GROUP, group);
        /*args.putParcelableArrayList(ARG_MEASUREMENTS, measurementsGroup);
        args.putParcelableArrayList(ARG_DEGREES, degreeSeparatedList);*/
        fragment.setArguments(args);
        Log.d("MeasureFragmNewInstance", "group = " + group + " something else");
        /*Log.d("MeasureFragmNewInstance", "degrees = " + degreeSeparatedList.size() + " elements");
        Log.d("MeasureFragmNewInstance", "measurements = " + measurementsGroup.size() + " elements");*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "on create");
        if (getArguments() != null) {

            group = getArguments().getInt(ARG_GROUP);
            /*measurementsGroup = getArguments().getParcelableArrayList(ARG_MEASUREMENTS);
            degreeSeparatedList = getArguments().getParcelableArrayList(ARG_DEGREES);*/
            Log.d(LOG_TAG, "on create group = " + group);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measure_group_list, container, false);

        Log.d(LOG_TAG, "on create view");
        activity = (MeasureInputActivity) getActivity();
        Log.d(LOG_TAG, "Activity is " + activity.getLocalClassName());
        presenter = activity.getPresenter();
        List<Measurement> measurementsGroup = presenter.getMeasurementGroup(group);
        Log.d(LOG_TAG, "presenter " + presenter);
        /*groupMeasurements = presenter.getMeasurementGroup(group);
        degMinSecListLeft = presenter.getDegMinSecArrListByGroup(MeasureInputActivity.LEFT_LIST, group);
        degMinSecListRight = presenter.getDegMinSecArrListByGroup(MeasureInputActivity.RIGHT_LIST, group);*/

        /*if(getArguments() != null) {
           // measurementsGroup = getArguments().getParcelableArrayList(ARG_MEASUREMENTS);
            //degreeSeparatedList = getArguments().getParcelableArrayList(ARG_DEGREES);
            group = getArguments().getInt(ARG_GROUP);
            //Log.d(LOG_TAG, "measurements from BUNDLE " + measurementsGroup.get(0).getNumber() + "  " + measurementsGroup.get(0).getContractor());
            Log.d(LOG_TAG, "group from BUNDLE " + group);
        }*/
        //Log.d(LOG_TAG, "arguments " + getArguments());
        Log.d(LOG_TAG, "group = " + group);


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

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "OnItemClick pressed!");
        //checkConstFields();
        /*if(binding.etContractor.getText())  binding.etContractor.setText("Noname");
        if(binding.etContractor.getText() == null)  binding.etMeasureDate.setText("01-01-2001");
        if(binding.etTheoDistance.getText() == null)  binding.etTheoDistance.setText("0");
        if(binding.etTheoHeight.getText() == null)  binding.etTheoHeight.setText("0");*/
        //constants = getConstants(); // Contractor, Date, Distance, Height


        //defaultValues = presenter.getSingleMeasurementData(position, group);
        //Log.d(LOG_TAG, "Default Values = " + defaultValues);
        int measurementNumber = group == 1 ? position + 1 : position + 11;

       activity.showMeasureDialogFragment(measurementNumber, group);
    }

    public void updateMeasureList() {
        /*adapterHelper.updateAdapter(measureInputPresenter.getMeasurements(),
                measureInputPresenter.getDegMinSecArrList(LEFT_LIST),
                measureInputPresenter.getDegMinSecArrList(RIGHT_LIST));
        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void onItemUpdateMeasureList(Measurement measurement, DegreeSeparated degreeSeparated) {
        adapterHelper.oneItemUpdateAdapter(measurement, degreeSeparated);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateAdapter(List<Measurement> measurements, List<DegreeSeparated> degreeSeparatedList) {
        adapterHelper.updateAdapter(measurements, degreeSeparatedList);
        adapter.notifyDataSetChanged();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        updateAdapter(presenter.getMeasurementGroup(group), presenter.getDegreeSeparatedAngleListByGroup(group));
    }*/
}