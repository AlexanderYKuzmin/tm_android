package com.example.appstraining.towermeasurement.view.main.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.databinding.SearchFormDialogFragmentBinding;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.view.main.MainActivity;
import com.example.appstraining.towermeasurement.view.main.MainPresenter;
import com.example.appstraining.towermeasurement.view.main.MainView;

public class SearchFormDialogFragment extends DialogFragment {
    private final String LOG_TAG = "SearchFormDialogFrag";

    private MainPresenter mainPresenter;
    private Context context;
    private MainView mainActivity;
    private SearchFormDialogFragmentBinding binding;

    public SearchFormDialogFragment(Context context, MainPresenter presenter, MainView mainActivity) {
        this.context = context;
        this.mainPresenter = presenter;
        this.mainActivity = mainActivity;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        binding = SearchFormDialogFragmentBinding.inflate(LayoutInflater.from(context));
        Log.d(LOG_TAG, "binding = " + binding.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        TextView title = new TextView(context);
        title.setText(R.string.search_form_main_title_ru);
        title.setTextSize(24);
        title.setTypeface(title.getTypeface(), Typeface.BOLD);
        title.setBackgroundColor(R.color.teal_200);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);

        builder.setCustomTitle(title)
                .setView(binding.getRoot())
                .setPositiveButton(R.string.btn_search_form_main_ok_ru, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(LOG_TAG, "OK button is pressed.");
                        mainPresenter.setSearchParameters(
                                binding.etNameSearchFormDialogFragMain.getText().toString(),
                                binding.etAddressSearchFormDialogFragMain.getText().toString()
                        );
                        switch (mainActivity.getActivityMode()) {
                            case LOAD_FROM_STORAGE:
                                mainActivity.showInnerSearchResultDialogFragment();
                                break;
                            case DELETE:
                                mainActivity.showDeleteDialogFragment();
                        }
                    }
                })
                .setNegativeButton(R.string.btn_search_form_main_cancel_ru, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        Log.d(LOG_TAG, "Search form dialog fragment is created");
        Log.d(LOG_TAG, "builder = " + builder);
        return builder.create();
    }
}
