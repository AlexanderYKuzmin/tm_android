package com.example.appstraining.towermeasurement.view.measurement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.appstraining.towermeasurement.MeasureListAdapterHelper;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.databinding.ActivityMeasureInputBinding;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureDialogFragment;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureGroup;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureGroupFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MeasureInputActivity extends AppCompatActivity implements MeasureInput, View.OnClickListener {
    private final String LOG_TAG = "MeasureInputActivity";
    private ArrayList<Measurement> measurements = new ArrayList<>();
    private ActivityMeasureInputBinding binding;
    private Context context = this;
    private MeasureInputPresenter measureInputPresenter;
    private MeasureListAdapterHelper adapterHelper;
    private SimpleAdapter adapter;

    public static final int LEFT_LIST = 0;  // the list of angles on the left tower's side
    public static final int RIGHT_LIST = 1;
    public static final int GROUP_1 = 1;
    public static final int GROUP_2 = 2;

    private String[] constants;
    private int[] defaultValues;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private boolean isBtnGroup1pressed = true;
    private boolean isBtnGroup2pressed = false;

    private MeasureGroup measureGroupFragment1;
    private MeasureGroup measureGroupFragment2;
    private FragmentManager fragmentManager;

    TextView title;
    EditText etDistance;
    EditText etHeight;
    EditText etAzimuth;
    EditText etSide;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_measure_input);
        binding = ActivityMeasureInputBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        measureInputPresenter = new MeasureInputPresenter(context, this);
        //adapterHelper = new MeasureListAdapterHelper(context);
        Log.d(LOG_TAG, "MesuareInputPresenter " + measureInputPresenter);
        measureInputPresenter.setMeasurements(
                getIntent().getParcelableArrayListExtra(getString(R.string.startmeasures)));
        Log.d(LOG_TAG, "Set measurements is : \n"
                + "left angle : " + measureInputPresenter.getMeasurements().get(0).getLeftAngle() + "\n"
                + "right angle : " + measureInputPresenter.getMeasurements().get(0).getRightAngle() + "\n"
                + "circle : " + measureInputPresenter.getMeasurements().get(0).getCircle() + "\n"
                + "date : " + measureInputPresenter.getMeasurements().get(0).getDate() + "\n"
                + "azimuth : " + measureInputPresenter.getMeasurements().get(0).getAzimuth() + "\n"

        );

        /*adapter = adapterHelper.getAdapter(
                measureInputPresenter.getMeasurements(),
                measureInputPresenter.getDegMinSecArrList(LEFT_LIST),
                measureInputPresenter.getDegMinSecArrList(RIGHT_LIST));*/
        /*binding.lvMeasures.setAdapter(adapter);
        binding.lvMeasures.setOnItemClickListener(this);*/
        //title = (TextView) findViewById(R.id.tvMeasureInputTitle);
       /* etDistance = (EditText) findViewById(R.id.etTheoDistance_measureGroup);
        etHeight = (EditText) findViewById(R.id.etTheoHeight_measureGroup);
        etAzimuth = (EditText) findViewById(R.id.etAzimuth_measureGroup);
        etSide = (EditText) findViewById(R.id.etSide_measureGroup);*/

        //title.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));


        binding.tvMeasureInputTitle.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));
        binding.etContractor.setText(measureInputPresenter.getMeasurements().get(0).getContractor());
        binding.etMeasureDate.setText(
                dateFormat.format(measureInputPresenter.getMeasurements().get(0).getDate()));

        //binding.btnMeasureGroup1.setPressed(true);


       /* measureGroupFragment1 = MeasureGroupFragment.newInstance(GROUP_1, measureInputPresenter.getMeasurementGroup(GROUP_1),
                measureInputPresenter.getDegreeSeparatedAngleListByGroup(GROUP_1));

        measureGroupFragment2 = MeasureGroupFragment.newInstance(GROUP_2, measureInputPresenter.getMeasurementGroup(GROUP_2),
                measureInputPresenter.getDegreeSeparatedAngleListByGroup(GROUP_2));*/
        measureGroupFragment1 = MeasureGroupFragment.newInstance(GROUP_1);
        measureGroupFragment2 = MeasureGroupFragment.newInstance(GROUP_2);


        //.d(LOG_TAG, "fragment created " + measureGroupFragment1 + " get Arguments = " + measureGroupFragment1.getArguments().getInt(MeasureGroupFragment.ARG_GROUP));
        /*measureGroupFragment1 = new MeasureGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MeasureGroupFragment.ARG_GROUP, GROUP_1);
        *//*bundle.putParcelableArrayList(MeasureGroupFragment.ARG_MEASUREMENTS, measureInputPresenter.getMeasurementGroup(GROUP_1));
        bundle.putParcelableArrayList(MeasureGroupFragment.ARG_DEGREES, measureInputPresenter.getDegreeSeparatedAngleListByGroup(measurements, GROUP_1));*//*
        measureGroupFragment1.setArguments(bundle);*/


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.measureGroup_container, (Fragment) measureGroupFragment1)
                .addToBackStack("myBackStack")
                .commit();

        /*binding.btnMeasureGroup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnMeasureGroup2.setPressed(false);

                fragmentManager.beginTransaction()
                        .replace(R.id.measureGroup_container, (Fragment) measureGroupFragment1)  // Сделать сохранение данных при переключении
                        .addToBackStack("myBackStack")
                        .commit();

                binding.btnMeasureGroup1.setPressed(true);
            }
        });

        binding.btnMeasureGroup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // fragmentManager.beginTransaction()
                binding.btnMeasureGroup1.setPressed(false);

                fragmentManager.beginTransaction()
                        .replace(R.id.measureGroup_container, (Fragment) measureGroupFragment2)  // Сделать сохранение данных при переключении
                        .addToBackStack("myBackStack")
                        .commit();

                binding.btnMeasureGroup2.setPressed(true);
            }
        });*/
        binding.radioMeasureGroup1MeasureInput.setOnClickListener(this);
        binding.radioMeasureGroup2MeasureInput.setOnClickListener(this);

        binding.radioMeasureGroup1MeasureInput.setChecked(true);

        binding.btnMeasureOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Log.d(LOG_TAG, "Measurement (0) : KL =" + measureInputPresenter.getMeasurements().get(0).getCircle() +"\n"
                    + " left anlgle = " + measureInputPresenter.getMeasurements().get(0).getLeftAngle() +"\n"
                    + " right anlgle = " + measureInputPresenter.getMeasurements().get(0).getRightAngle() +"\n"

                );
                intent.putParcelableArrayListExtra(getString(R.string.measureslist), measureInputPresenter.getMeasurements());
                //intent.putExtra(getString(R.string.ismeasureschanged), true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.radio_measureGroup1_measureInput:
                if (checked)
                    // Pirates are the best
                    measureGroupFragment2.updateAdapter(measureInputPresenter.getMeasurementGroup(GROUP_2),
                            measureInputPresenter.getDegreeSeparatedAngleListByGroup(GROUP_2));
                    fragmentManager.beginTransaction()
                            .replace(R.id.measureGroup_container, (Fragment) measureGroupFragment1)  // Сделать сохранение данных при переключении
                            .addToBackStack("myBackStack")
                            .commit();
                    break;
            case R.id.radio_measureGroup2_measureInput:
                if (checked)
                    // Ninjas rule
                    measureGroupFragment1.updateAdapter(measureInputPresenter.getMeasurementGroup(GROUP_1),
                            measureInputPresenter.getDegreeSeparatedAngleListByGroup(GROUP_1));
                    fragmentManager.beginTransaction()
                            .replace(R.id.measureGroup_container, (Fragment) measureGroupFragment2)  // Сделать сохранение данных при переключении
                            .addToBackStack("myBackStack")
                            .commit();
                    break;
        }
    }
    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "OnItemClick pressed!");
        //checkConstFields();
        *//*if(binding.etContractor.getText())  binding.etContractor.setText("Noname");
        if(binding.etContractor.getText() == null)  binding.etMeasureDate.setText("01-01-2001");
        if(binding.etTheoDistance.getText() == null)  binding.etTheoDistance.setText("0");
        if(binding.etTheoHeight.getText() == null)  binding.etTheoHeight.setText("0");*//*
        constants = getConstants(); // Contractor, Date, Distance, Height
        defaultValues = measureInputPresenter.getSingleMeasurementData(position);
        Log.d(LOG_TAG, "Default Values = " + defaultValues);
        new MeasureDialogFragment(this, measureInputPresenter, position + 1, constants, defaultValues)
                .show(getSupportFragmentManager(), null);
    }

    public void updateMeasureList() {
        adapterHelper.updateAdapter(measureInputPresenter.getMeasurements(),
                measureInputPresenter.getDegMinSecArrList(LEFT_LIST),
                measureInputPresenter.getDegMinSecArrList(RIGHT_LIST));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void oneItemUpdateMeasureList(Measurement measurement, int[] leftAngle,
                                         int[] rightAngle) {

        adapterHelper.oneItemUpdateAdapter(measurement, leftAngle, rightAngle);
        adapter.notifyDataSetChanged();
    }*/

    private String[] getConstants(){
        String[] constants = {
                binding.etContractor.getText().toString(),
                binding.etMeasureDate.getText().toString()};
                //binding.etTheoDistance.getText().toString(),
               // binding.etTheoHeight.getText().toString() };
        return constants;
    }

    public MeasureInputPresenter getPresenter() {
        return measureInputPresenter;
    }

    public MeasureGroup getFragment(int group) {
        switch(group) {
            case GROUP_1: return measureGroupFragment1;
            case GROUP_2: return measureGroupFragment2;
        }
        return null;
    }

    @Override
    public void showMeasureDialogFragment(int measurementNumber, int group) {
        new MeasureDialogFragment(context, measureInputPresenter, measurementNumber).show(getSupportFragmentManager(), null);
    }
}