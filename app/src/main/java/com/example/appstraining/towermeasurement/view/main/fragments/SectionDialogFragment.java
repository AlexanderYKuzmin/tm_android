package com.example.appstraining.towermeasurement.view.main.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.model.Section;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;

import java.util.stream.Collectors;

public class SectionDialogFragment extends DialogFragment {
    private final String LOG_TAG = "SectionDialogFragment";
    private Context context;
    private MainPresenter mainPresenter;
    private MainActivityMode activityMode;
    private int secNum;
    private TextView tvSecNum;
    private EditText etWidthBottom, etWidthTop, etHeight;

    public SectionDialogFragment(Context context, MainPresenter presenter, int secNum, MainActivityMode activityMode){
        this.context = context;
        this.mainPresenter = presenter;
        this.secNum = secNum;
        this.activityMode = activityMode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.section_dialog_frag, null);
        tvSecNum = v.findViewById(R.id.tvSecNum_secFrag);
        tvSecNum.setText(String.valueOf(secNum));
        etWidthBottom = v.findViewById(R.id.etWidthBottom_secFrag);
        etWidthTop = v.findViewById(R.id.etWidthTop_secFrag);
        etHeight = v.findViewById(R.id.etHeight_secFrag);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        setDefaultFieldData();

        builder.setCustomTitle(getCustomTitle())
                .setView(v)
                .setPositiveButton(R.string.btn_section_dialog_fragment_main_ok_ru, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainPresenter.updateSection(secNum,
                        etWidthBottom.getText().toString(),
                        etWidthTop.getText().toString(),
                        etHeight.getText().toString()
                );
                Log.d(LOG_TAG, "OK pressed!!");
            }
        })
                .setNegativeButton(R.string.btn_section_dialog_fragment_main_cancel_ru, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private void setDefaultFieldData() {
        switch (activityMode) {
            case NEW:
                if (secNum == 1) {
                    //etWidthBottom.setText("0");
                    //etHeight.setText("0");
                } else if (secNum == mainPresenter.getSections().size()){
                    Section prevSection = mainPresenter.getSections().get(secNum - 2);
                    int sumHeightOfPrevSections =
                            mainPresenter.getSections().stream().map(Section::getHeight).mapToInt(Integer::intValue).sum();
                    etWidthBottom.setText(String.valueOf(prevSection.getWidthTop()));
                    etWidthBottom.setEnabled(false);
                    etHeight.setText(String.valueOf(mainPresenter.getBuilding().getHeight() - sumHeightOfPrevSections));
                    etHeight.setEnabled(false);
                } else {
                    Section prevSection = mainPresenter.getSections().get(secNum - 2);
                    etWidthBottom.setText(String.valueOf(prevSection.getWidthTop()));
                    etWidthBottom.setEnabled(false);
                    //etHeight.setText("0");
                }
                //etWidthTop.setText("0");
                break;
            case USE_TEMPLATE:
                Section section = mainPresenter.getSections().get(secNum - 1);
                etWidthBottom.setText(String.valueOf(section.getWidthBottom()));
                etWidthTop.setText(String.valueOf(section.getWidthTop()));
                etHeight.setText(String.valueOf(section.getHeight()));
        }

    }

    private TextView getCustomTitle() {
        TextView tvTitle = new TextView(context);
        tvTitle.setText(R.string.section_dialog_fragment_title);
        tvTitle.setTextSize(24);
        tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        tvTitle.setBackgroundColor(getResources().getColor(R.color.light_blue_600));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setTextColor(getResources().getColor(R.color.maincolor));

        return tvTitle;
    }
}
