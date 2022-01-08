package com.example.appstraining.towermeasurement.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.MainPresenter;
import com.example.appstraining.towermeasurement.R;

public class SectionDialogFragment extends DialogFragment {
    final String LOG_TAG = "SectionDialogFragment";
    Context context;
    MainPresenter mainPresenter;
    int secNum;
    TextView tvSecNum;
    EditText etWidthBottom, etWidthTop, etHeight;

    public SectionDialogFragment(Context context, MainPresenter presenter, int secNum){
        this.context = context;
        this.mainPresenter = presenter;
        this.secNum = secNum;
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
        builder.setTitle("Change section parameters")
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainPresenter.updateSection(secNum,
                        etWidthBottom.getText().toString(),
                        etWidthTop.getText().toString(),
                        etHeight.getText().toString()
                );
                Log.d(LOG_TAG, "OK pressed!!");
            }
        });

        return builder.create();
    }
}
