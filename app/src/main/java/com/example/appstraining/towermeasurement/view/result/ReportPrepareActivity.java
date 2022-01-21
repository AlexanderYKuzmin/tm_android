package com.example.appstraining.towermeasurement.view.result;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.databinding.ActivityReportPrepareBinding;
import com.example.appstraining.towermeasurement.model.Result;

import java.util.ArrayList;
import java.util.Comparator;

public class ReportPrepareActivity extends AppCompatActivity {
    private final String LOG_TAG = "ReportPrepareActivity";
    private final int XOZ = 1;
    private final int YOZ = 2;
    private final int XOY = 3;
    ReportPreparePresenter rpPresenter;
    ActivityReportPrepareBinding binding;
    int buildingID;
    int levels[];

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_prepare);

        buildingID = getIntent().getIntExtra("building_id", 0);
        levels = getIntent().getIntArrayExtra("levels");
        Log.d(LOG_TAG, " Levels is recieved: Levels[1] = " + levels[1] + "; "
                + "levels[2] = " + levels[2]
        );
        rpPresenter = new ReportPreparePresenter(buildingID, levels, this);
        Log.d(LOG_TAG, "Building name: " + rpPresenter.getReportBuilding().getName() + "\n"
                + "creation date: " + rpPresenter.getReportBuilding().getCreationDate()
        );

        /*for(Measurement m : rpPresenter.getReportBuilding().getMeasurements()) {
            Log.d(LOG_TAG, "m_id =" + m.getId() + "\n"
                            + "m_s_id = " + m.getSectionId() + "\n"
                            + "m_sectionNumber = " + m.getSectionNumber() + "\n"
                            + "m_side = " + m.getSide() + "\n"
                            + "m_left angle = " + m.getLeftAngle() + "\n"
                            + "m_right angle = " + m.getRightAngle() + "\n"

            );
        }*/

        binding = ActivityReportPrepareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        updateView();

        binding.imbtnXOZ.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Intent graphViewPagerIntent = new Intent(ReportPrepareActivity.this, GraphViewPager.class);
                graphViewPagerIntent.putExtra("graphtype", XOZ);
                /*Log.d(LOG_TAG, "rpPresenter.getXOZArray = " + "\n"
                        + rpPresenter.getXOZArray()[0] + " :: " + rpPresenter.getXOZArray()[1] + ";  \n"
                        + rpPresenter.getXOZArray()[2] + " :: " + rpPresenter.getXOZArray()[3] + ";  \n"
                        + rpPresenter.getXOZArray()[4] + " :: " + rpPresenter.getXOZArray()[5] + ";  \n"
                        + rpPresenter.getXOZArray()[6] + " :: " + rpPresenter.getXOZArray()[7] + ";  \n"

                );
                int[] test = {0, 1, 10, 11, 20, 21, 5000, 5001};
                int[] testRange = {0, 50, 0, 100000};*/
               // graphViewPagerIntent.putExtra("xoz_array", test);
                graphViewPagerIntent.putExtra("xoz_array", rpPresenter.getXOZArray());
                graphViewPagerIntent.putExtra("yoz_array", rpPresenter.getYOZArray());
                graphViewPagerIntent.putExtra("xoy_array", rpPresenter.getXOYArray());
                graphViewPagerIntent.putExtra("xoz_range", rpPresenter.getXOZRange());
                //graphViewPagerIntent.putExtra("yoz_range", testRange);
                graphViewPagerIntent.putExtra("yoz_range", rpPresenter.getYOZRange());
                graphViewPagerIntent.putExtra("xoy_range", rpPresenter.getXOYRange());

                //*** Test ***
                for(int x: rpPresenter.getYOZRange()) {
                    Log.d(LOG_TAG, "yozRange[i] = " + x);
                }
                //*** Enf test ***

                startActivity(graphViewPagerIntent);
            }
        });

        binding.imbtnJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent journalViewPagerIntent = new Intent(ReportPrepareActivity.this, JournalViewPager.class);
                //journalViewPagerIntent.putExtra("", XOZ);
                journalViewPagerIntent.putExtra("theodistances", rpPresenter.getTheoDistances());
                journalViewPagerIntent.putParcelableArrayListExtra("measurements",
                        rpPresenter.getReportBuilding().getMeasurements());
                journalViewPagerIntent.putParcelableArrayListExtra("results",
                        rpPresenter.getReportBuilding().getResults());
                journalViewPagerIntent.putExtra("levels", levels);
                startActivity(journalViewPagerIntent);
            }
        });

        binding.imbtnXOY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateView() {
        int deviationPoss = rpPresenter.getReportBuilding().getHeight() / 1000;
        ArrayList<Result> reportResults = rpPresenter.getReportBuilding().getResults();
        reportResults.sort(Comparator.comparing(Result::getShiftMm));

        binding.tvAddressReport.setText(rpPresenter.getReportBuilding().getAddress());
        binding.tvTypeReport.setText((rpPresenter.getReportBuilding().getType().getBuildingTypeRu()));
        binding.tvConfigReport.setText(String.valueOf(rpPresenter.getReportBuilding().getConfig()));
        binding.tvHeightReport.setText(String.valueOf(rpPresenter.getReportBuilding().getHeight()));
        binding.tvPossibleDeviation.setText(String.valueOf(deviationPoss));
        binding.tvFactDeviation.setText(String.valueOf(reportResults.get(reportResults.size() -1).getShiftMm()));

        //rpPresenter.get
    }
}