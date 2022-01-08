package com.example.appstraining.towermeasurement.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.MainPresenter;
import com.example.appstraining.towermeasurement.MeasureInputPresenter;
import com.example.appstraining.towermeasurement.databinding.DialogItemBinding;
import com.example.appstraining.towermeasurement.databinding.MeasureDialogFragmentBinding;

public class MeasureDialogFragment extends DialogFragment {
    final String LOG_TAG = "MeasureDialogFragment";
    Context context;
    MeasureInputPresenter mMeasureInputPresenter;
    MeasureDialogFragmentBinding binding;
    String[] measureConstantsStrArr;
    int[] measureInputAngleFields;

    int measureNum;

    public MeasureDialogFragment(Context context, MeasureInputPresenter presenter, int measureNum,
                                 String[] params){
        this.context = context;
        this.mMeasureInputPresenter = presenter;
        this.measureNum = measureNum;
        measureConstantsStrArr = params;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //View v = getActivity().getLayoutInflater().inflate(R.layout.measure_dialog_fragment, null);
        binding = MeasureDialogFragmentBinding.inflate(LayoutInflater.from(getContext()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Insert measurement values")
                .setView(binding.getRoot())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(LOG_TAG, "OK button is pressed.");
                        MainPresenter.isMeasuresChanged = true;
                        measureInputAngleFields = getFieldData(); // integer[]

                        if(mMeasureInputPresenter.isAngleDataCorrect(
                                measureInputAngleFields)) {
                            double leftAngle = mMeasureInputPresenter.fromDegToDec(
                                    measureInputAngleFields[0], // degree
                                    measureInputAngleFields[1], // minutes
                                    measureInputAngleFields[2]  // seconds
                            );
                            double rightAngle = mMeasureInputPresenter.fromDegToDec(
                                    measureInputAngleFields[3],
                                    measureInputAngleFields[4],
                                    measureInputAngleFields[5]
                            );
                            int azimuth = measureInputAngleFields[6];
                            int theoDistance = measureInputAngleFields[7];
                            int theoHeight = measureInputAngleFields[8];
                            Log.d(LOG_TAG, "Left angle double: " + leftAngle);
                            Log.d(LOG_TAG, "Right angle double: " + rightAngle);
                            mMeasureInputPresenter.updateMeasurement(measureNum, leftAngle, rightAngle,
                                    azimuth, theoDistance, theoHeight, measureConstantsStrArr);

                        } else {
                            Toast.makeText(context, "Please enter correct data.",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
}
