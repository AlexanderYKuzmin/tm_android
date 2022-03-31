package com.example.appstraining.towermeasurement.view.result;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.model.Measurement;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.view.result.fragments.JournalFragment;

import java.util.ArrayList;
import java.util.Comparator;

public class JournalViewPager extends AppCompatActivity {
    public final String LOG_TAG = "JournalViewPager";
    ViewPager2 journalPager;

    int[] distances;
    int[] levels;
    ArrayList<Measurement> measurements = new ArrayList<>();
    ArrayList<Result> results = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_view_pager);
        Log.d(LOG_TAG, "on Create JournalViewPager");

        distances = getIntent().getIntArrayExtra("theodistances");
        levels = getIntent().getIntArrayExtra("levels");
        measurements.addAll(getIntent().getParcelableArrayListExtra("measurements"));
        results.addAll(getIntent().getParcelableArrayListExtra("results"));
        Log.d(LOG_TAG, " measurements getParcelableArrayListExtra " + measurements.get(5).getId());

        int page_count = 2;
        //if(distances[2] == 0) page_count = 2; changed for 2 sides

        journalPager = (ViewPager2) findViewById(R.id.journal_pager);
        JournalFragmentPagerAdapter journalFragmentPagerAdapter = new JournalFragmentPagerAdapter(
                this, page_count, measurements, results, distances, levels
        );
        journalPager.setAdapter(journalFragmentPagerAdapter);
    }

    public static class JournalFragmentPagerAdapter extends FragmentStateAdapter {
        private final String LOG_TAG = "JournalFragmentAdapter";
        int page_count;
        ArrayList<Measurement> measurements;
        ArrayList<Result> results;
        int[] distances;
        int[] levels;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public JournalFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, int page_count,
                                           ArrayList<Measurement> measurements, ArrayList<Result> results, int[] distances,
                                           int[] levels
        ) {
            super(fragmentActivity);
            this.page_count = page_count;
            Log.d(LOG_TAG, "Constructor is running.");
            this.measurements = measurements;
            this.measurements.sort(Comparator.comparing(Measurement::getId));
            this.results = results;
            this.results.sort(Comparator.comparing(Result::getId));
            for(Result r : results){
                Log.d(LOG_TAG, "result id = " + r.getId());
            }
            Log.d(LOG_TAG, "Constructor is running. After collections");
            this.distances = distances;
            this.levels = levels;
            Log.d(LOG_TAG, "Constructor is running. After all");
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d(LOG_TAG, "create fragment. Position = " + position);
            return JournalFragment.newInstance(position, distances[position], getMeasArray(position),
                    getResArray(position), levels);
        }

        @Override
        public int getItemCount() {
            return page_count;
        }

        private Measurement[] getMeasArray(int position) {
            Measurement[] measArray = new Measurement[measurements.size() / 2]; // changed for 2 sides
            int beginIndex = measArray.length * position;
            int endIndex = beginIndex + measArray.length;
            for (int i = beginIndex, j = 0; i < endIndex; i++, j++) {
                measArray[j] = measurements.get(i);
            }
            return measArray;
        }

        private Result[] getResArray(int position) {
            Result[] resArray = new Result[results.size() / 2]; // changed for 2 sides
            int beginIndex = resArray.length * position;
            int endIndex = beginIndex + resArray.length;
            for (int i = beginIndex, j = 0; i < endIndex; i++, j++) {
                resArray[j] = results.get(i);
                Log.d(LOG_TAG, "resArray[j] = " + resArray[j].getId() + ";  i = " + i);
            }
            return resArray;
        }
    }
}