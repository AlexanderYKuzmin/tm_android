package com.example.appstraining.towermeasurement.view.measurement.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.DegreeSeparated;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.util.DegreeNumericConverter;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;
import com.example.appstraining.towermeasurement.view.measurement.MeasureInputPresenter;
import com.example.appstraining.towermeasurement.databinding.MeasureDialogFragmentBinding;

public class MeasureDialogFragment extends DialogFragment {
    private final String LOG_TAG = "MeasureDialogFragment";
    private Context context;
    private MeasureInputPresenter mMeasureInputPresenter;
    private MeasureDialogFragmentBinding binding;
    private String[] measureConstantsStrArr;
    private int[] measureInputAngleFields;
    private int[] measureDefaultValues;

    private int measureNum;
    private int positionInListView;

    public MeasureDialogFragment(Context context, MeasureInputPresenter presenter, int measureNum){
        this.context = context;
        this.mMeasureInputPresenter = presenter;
        this.measureNum = measureNum;
        //measureConstantsStrArr = params;
        //measureDefaultValues = defaultValues;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //View v = getActivity().getLayoutInflater().inflate(R.layout.measure_dialog_fragment, null);
        binding = MeasureDialogFragmentBinding.inflate(LayoutInflater.from(getContext()));

        setDefaultFieldData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCustomTitle(getCustomTitle())
                .setView(binding.getRoot())
                .setPositiveButton(R.string.btn_ok_measure_dialog_frag_measurement_ru, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(LOG_TAG, "OK button is pressed.");
                        MainPresenter.isMeasuresChanged = true;
                        measureInputAngleFields = getFieldData(); // integer[]
                        Log.d(LOG_TAG, "left angle d m s: "
                                + measureInputAngleFields[0] + " "
                                + measureInputAngleFields[1] + " "
                                + measureInputAngleFields[2] + " "
                        );
                        if(mMeasureInputPresenter.isAngleDataCorrect(
                                measureInputAngleFields)) {
                            double leftAngle = DegreeNumericConverter.fromDegToDec(
                                    measureInputAngleFields[0], // degree
                                    measureInputAngleFields[1], // minutes
                                    measureInputAngleFields[2]  // seconds
                            );
                            double rightAngle = DegreeNumericConverter.fromDegToDec(
                                    measureInputAngleFields[3],
                                    measureInputAngleFields[4],
                                    measureInputAngleFields[5]
                            );
                            int azimuth = measureInputAngleFields[6];
                            int theoDistance = measureInputAngleFields[7];
                            int theoHeight = measureInputAngleFields[8];
                            Log.d(LOG_TAG, "Left angle double: " + leftAngle);
                            //Log.d(LOG_TAG, "Right angle double: " + rightAngle);
                            mMeasureInputPresenter.updateMeasurement(measureNum, leftAngle, rightAngle,
                                    azimuth, theoDistance, theoHeight);

                        } else {
                            Toast.makeText(context, "Please enter correct data.",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                })
        .setNegativeButton(R.string.btn_cancel_measure_dialog_frag_measurement_ru, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        Log.d(LOG_TAG, "Measure Dialog Fragment is created.");
        return builder.create();
    }

    private int[] getFieldData(){
        String leftDegMeasFrag = String.valueOf(binding.etLeftDegMeasFrag.getText());
        String leftMinMeasFrag = String.valueOf(binding.etLeftMinMeasFrag.getText());
        String leftSecMeasFrag = binding.etLeftSecMeasFrag.getText().toString();
        String rightDegMeasFrag = binding.etRightDegMeasFrag.getText().toString();
        String rightMinMeasFrag = binding.etRightMinMeasFrag.getText().toString();
        String rightSecMeasFrag = binding.etRightSecMeasFrag.getText().toString();
        String azimuth = binding.etAzimuthMeasFrag.getText().toString();
        String distance = binding.etTheoDistanceMeasFrag.getText().toString();
        String theoHeight = binding.etTheoHeightMeasFrag.getText().toString();
        int leftDeg = 0, leftMin = 0, leftSec = 0;
        int rightDeg = 0, rightMin = 0, rightSec = 0;
        int azim = 0, dist = 0, height = 0;
        if (!leftDegMeasFrag.isEmpty())  leftDeg = Integer.parseInt(leftDegMeasFrag);
        if (!leftMinMeasFrag.isEmpty())  leftMin = Integer.parseInt(leftMinMeasFrag);
        if (!leftSecMeasFrag.isEmpty())  leftSec = Integer.parseInt(leftSecMeasFrag);
        if (!rightDegMeasFrag.isEmpty())  rightDeg = Integer.parseInt(rightDegMeasFrag);
        if (!rightMinMeasFrag.isEmpty())  rightMin = Integer.parseInt(rightMinMeasFrag);
        if (!rightSecMeasFrag.isEmpty())  rightSec = Integer.parseInt(rightSecMeasFrag);
        if (!azimuth.isEmpty())  azim = Integer.parseInt(azimuth);
        if (!distance.isEmpty())  dist = Integer.parseInt(distance);
        if (!theoHeight.isEmpty())  height = Integer.parseInt(theoHeight);
        int[] angleFields = {
                leftDeg,
                leftMin,
                leftSec,
                rightDeg,
                rightMin,
                rightSec,
                azim,
                dist,
                height
        };
        return angleFields;
    }

    private void setDefaultFieldData() {
        Measurement measurement = mMeasureInputPresenter.getMeasurements().get(measureNum - 1);
        DegreeSeparated degreeSeparated = new DegreeSeparated(
                DegreeNumericConverter.fromDecToDeg(measurement.getLeftAngle()),
                DegreeNumericConverter.fromDecToDeg(measurement.getRightAngle())
        );

        binding.etLeftDegMeasFrag.setText(String.valueOf(degreeSeparated.getDegreeLeft()));
        binding.etLeftMinMeasFrag.setText(String.valueOf(degreeSeparated.getMinuteLeft()));
        binding.etLeftSecMeasFrag.setText(String.valueOf(degreeSeparated.getSecondLeft()));
        binding.etRightDegMeasFrag.setText(String.valueOf(degreeSeparated.getDegreeRight()));
        binding.etRightMinMeasFrag.setText(String.valueOf(degreeSeparated.getMinuteRight()));
        binding.etRightSecMeasFrag.setText(String.valueOf(degreeSeparated.getSecondRight()));
        binding.etAzimuthMeasFrag.setText(String.valueOf(measurement.getAzimuth()));
        binding.etTheoDistanceMeasFrag.setText(String.valueOf(measurement.getDistance()));
        binding.etTheoHeightMeasFrag.setText(String.valueOf(measurement.getTheoHeight()));
        binding.tvMeasureNumberMeasFrag.setText(String.valueOf(measureNum));
        binding.tvGroupNumberMeasFrag.setText(String.valueOf(measurement.getSide()));
    }

    private TextView getCustomTitle() {
        TextView tvTitle = new TextView(context);
        tvTitle.setText(R.string.title_measurement_dialog_ru);
        tvTitle.setTextSize(24);
        tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        tvTitle.setBackgroundColor(getResources().getColor(R.color.light_blue_600));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        return tvTitle;
    }
}
