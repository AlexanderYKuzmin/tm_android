package com.example.appstraining.towermeasurement.view.measurement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.example.appstraining.towermeasurement.MeasureListAdapterHelper;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.databinding.ActivityMeasureInputBinding;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.view.measurement.fragments.MeasureDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MeasureInputActivity extends AppCompatActivity implements MeasureInputInterface, AdapterView.OnItemClickListener {
    private final String LOG_TAG = "MeasureInputActivity";
    private ArrayList<Measurement> measurements = new ArrayList<>();
    private ActivityMeasureInputBinding binding;
    private Context context = this;
    private MeasureInputPresenter measureInputPresenter;
    private MeasureListAdapterHelper adapterHelper;
    private SimpleAdapter adapter;

    public static final int LEFT_LIST = 0;  // the list of angles on the left tower's side
    public static final int RIGHT_LIST = 1;

    private String[] constants;
    private int[] defaultValues;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_measure_input);
        binding = ActivityMeasureInputBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        measureInputPresenter = new MeasureInputPresenter(context, this);
        adapterHelper = new MeasureListAdapterHelper(context);

        measureInputPresenter.setMeasurements(
                getIntent().getParcelableArrayListExtra(getString(R.string.startmeasures)));
        Log.d(LOG_TAG, "Set measurements is : \n"
                + "left angle : " + measureInputPresenter.getMeasurements().get(0).getLeftAngle() + "\n"
                + "right angle : " + measureInputPresenter.getMeasurements().get(0).getRightAngle() + "\n"
                + "circle : " + measureInputPresenter.getMeasurements().get(0).getCircle() + "\n"
                + "date : " + measureInputPresenter.getMeasurements().get(0).getDate() + "\n"
                + "azimuth : " + measureInputPresenter.getMeasurements().get(0).getAzimuth() + "\n"

        );

        adapter = adapterHelper.getAdapter(
                measureInputPresenter.getMeasurements(),
                measureInputPresenter.getDegMinSecArrList(LEFT_LIST),
                measureInputPresenter.getDegMinSecArrList(RIGHT_LIST));
        binding.lvMeasures.setAdapter(adapter);
        binding.lvMeasures.setOnItemClickListener(this);

        binding.tvMeasureInputTitle.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));
        binding.etContractor.setText(measureInputPresenter.getMeasurements().get(0).getContractor());
        binding.etMeasureDate.setText(
                dateFormat.format(measureInputPresenter.getMeasurements().get(0).getDate()));

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "OnItemClick pressed!");
        //checkConstFields();
        /*if(binding.etContractor.getText())  binding.etContractor.setText("Noname");
        if(binding.etContractor.getText() == null)  binding.etMeasureDate.setText("01-01-2001");
        if(binding.etTheoDistance.getText() == null)  binding.etTheoDistance.setText("0");
        if(binding.etTheoHeight.getText() == null)  binding.etTheoHeight.setText("0");*/
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
    }

    private String[] getConstants(){
        String[] constants = {
                binding.etContractor.getText().toString(),
                binding.etMeasureDate.getText().toString()};
                //binding.etTheoDistance.getText().toString(),
               // binding.etTheoHeight.getText().toString() };
        return constants;
    }
}