package com.example.appstraining.towermeasurement.view.measurement;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.MeasureListAdapterHelper;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.databinding.ActivityMeasureInputBinding;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureDialogFragment;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureGroup;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureGroupFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    private String[] constants = new String[2];
    private int[] defaultValues;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private boolean isBtnGroup1pressed = true;
    private boolean isBtnGroup2pressed = false;

    private MeasureGroup measureGroupFragment1;
    private MeasureGroup measureGroupFragment2;
    private FragmentManager fragmentManager;

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

        // ***** TEST ***********
        if (measureInputPresenter.getMeasurements() != null) {
            Log.d(LOG_TAG, "Set measurements is : \n"
                    + "left angle : " + measureInputPresenter.getMeasurements().get(0).getLeftAngle() + "\n"
                    + "right angle : " + measureInputPresenter.getMeasurements().get(0).getRightAngle() + "\n"
                    + "circle : " + measureInputPresenter.getMeasurements().get(0).getCircle() + "\n"
                    + "date : " + dateFormat.format(measureInputPresenter.getMeasurements().get(0).getDate()) + "\n"
                    + "azimuth : " + measureInputPresenter.getMeasurements().get(0).getAzimuth() + "\n"

            );
        } else Log.d(LOG_TAG, "Measurements is NULL");
        // ***********************

        binding.tvMeasureInputTitle.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));
        binding.etContractor.setText(measureInputPresenter.getMeasurements().get(0).getContractor());

        Date utilDate = new Date(measureInputPresenter.getMeasurements().get(0).getDate().getTime());
        Log.d(LOG_TAG, "util date to set text is: " + utilDate);
        binding.etMeasureDate.setText(dateFormat.format(utilDate));
        Log.d(LOG_TAG, "Date: " + measureInputPresenter.getMeasurements().get(0).getDate());

        measureGroupFragment1 = MeasureGroupFragment.newInstance(GROUP_1);
        measureGroupFragment2 = MeasureGroupFragment.newInstance(GROUP_2);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.measureGroup_container, (Fragment) measureGroupFragment1)
                .addToBackStack("myBackStack")
                .commit();

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

                measureInputPresenter.setCommonConstantsToMeasurements(getCommonConstants());

                if (binding.radioMeasureGroup1MeasureInput.isChecked()) {   // sve constants from fragment
                    measureGroupFragment1.fillDhaValues();
                    measureInputPresenter.setDhaToMeasurements(GROUP_1, measureGroupFragment1.getDhaValues());
                }

                if (binding.radioMeasureGroup2MeasureInput.isChecked()) {   // sve constants from fragment
                    measureGroupFragment2.fillDhaValues();
                    measureInputPresenter.setDhaToMeasurements(GROUP_2, measureGroupFragment2.getDhaValues());
                }

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
                            //.addToBackStack("myBackStack")
                            .commit();
                    break;
            case R.id.radio_measureGroup2_measureInput:
                if (checked)
                    // Ninjas rule
                    measureGroupFragment1.updateAdapter(measureInputPresenter.getMeasurementGroup(GROUP_1),
                            measureInputPresenter.getDegreeSeparatedAngleListByGroup(GROUP_1));
                    fragmentManager.beginTransaction()
                            .replace(R.id.measureGroup_container, (Fragment) measureGroupFragment2)  // Сделать сохранение данных при переключении
                            //.addToBackStack("myBackStack")
                            .commit();
                    break;
        }
    }

    private String[] getCommonConstants(){
        String[] constants = {
                binding.etContractor.getText().toString(),
                binding.etMeasureDate.getText().toString()};
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showMeasureDialogFragment(int measurementNumber, int group, int[] dhaValues) {
        Log.d(LOG_TAG, "dhaValues height: " + dhaValues[1]);
        measureInputPresenter.setDhaToMeasurements(group, dhaValues); /// ПЕРЕДЕЛАТЬ !! Индивидуально на каждый фрагмент. Изменить список  целиком.

        // Test
        //transMeasurements.forEach(measurement -> System.out.println(measurement.getSide() + "::" + measurement.getTheoHeight()));

        new MeasureDialogFragment(context, measureInputPresenter, measurementNumber).show(getSupportFragmentManager(), null);
    }
}