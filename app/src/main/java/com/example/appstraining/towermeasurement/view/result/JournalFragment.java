package com.example.appstraining.towermeasurement.view.result;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appstraining.towermeasurement.model.adapter.JournalAdapterHelper;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Result;

public class JournalFragment extends Fragment {
    private final String LOG_TAG = "JournalFragment";

    static final String ARGUMENT_POSITION = "position";
    static final String ARGUMENT_DISTANCE = "distance";
    static final String ARGUMENT_LEVELS = "levels";
    static final String ARGUMENT_MEASUREMENTS = "measurements";
    static final String ARGUMENT_RESULTS = "results";

    private int position;
    private int theoDistance;
    private int theoHeight;
    private String point;
    private Measurement[] measurements;
    private Result[] results;
    private int[] levels;

    private SimpleAdapter adapter;
    private JournalAdapterHelper jah;
    static JournalFragment newInstance(int position, int distance, Measurement[] measurements,
                                       Result[] results, int[] levels) {
        JournalFragment journalFragment = new JournalFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_POSITION, position);
        arguments.putIntArray(ARGUMENT_LEVELS, levels);
        arguments.putInt(ARGUMENT_DISTANCE, distance);
        arguments.putParcelableArray(ARGUMENT_MEASUREMENTS, measurements);
        arguments.putParcelableArray(ARGUMENT_RESULTS, results);
        journalFragment.setArguments(arguments);
        return journalFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theoDistance = getArguments().getInt(ARGUMENT_DISTANCE);
        position = getArguments().getInt(ARGUMENT_POSITION);
        levels = getArguments().getIntArray(ARGUMENT_LEVELS);
        measurements = (Measurement[]) getArguments().getParcelableArray(ARGUMENT_MEASUREMENTS);
        results = (Result[]) getArguments().getParcelableArray(ARGUMENT_RESULTS);
        Log.d(LOG_TAG, "theoDistance = " + theoDistance + "; position = " + position);
        Log.d(LOG_TAG, "Measurements size = " + measurements.length);
        for (int i = 0; i < levels.length; i++) {
            Log.d(LOG_TAG, "levels[i] = " + levels[i]);
        }

        jah = new JournalAdapterHelper(getActivity().getApplicationContext());
        adapter = jah.getAdapter(levels, results, measurements);

        switch (position) {
            case 0:
                point = "A";
                break;
            case 1:
                point = "B";
                break;
            case 2:
                point = "C";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal_page, null);

        TextView tvPoint = (TextView) view.findViewById(R.id.tvPoint);
        TextView tvDistance = (TextView) view.findViewById(R.id.tvTheoDistance_journal);
        ListView lvJournal = (ListView) view.findViewById(R.id.lvJournal);

        Log.d(LOG_TAG, "Prepare text to set tvPoint is: " + point + "; tvDistance is: " +theoDistance);
        tvPoint.setText(point);
        tvDistance.setText(String.valueOf(theoDistance));
        Log.d(LOG_TAG, "adapter count: " + adapter.getCount());
        Log.d(LOG_TAG, "get item 0: " + adapter.getItem(0).toString());
        Log.d(LOG_TAG, "get item 1: " + adapter.getItem(1).toString());
        Log.d(LOG_TAG, "get item 2: " + adapter.getItem(2).toString());
        lvJournal.setAdapter(adapter);

        return view;


    }
}
