package com.example.appstraining.towermeasurement.view.result;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appstraining.towermeasurement.R;
//import com.example.appstraining.towermeasurement.data.file.DocCreator;
import com.example.appstraining.towermeasurement.data.file.GraphViewCreator;
import com.example.appstraining.towermeasurement.model.Result;
import com.example.appstraining.towermeasurement.databinding.ActivityReportPrepareBinding;

import java.util.ArrayList;
import java.util.Comparator;

public class ReportPrepareActivity extends AppCompatActivity {
    private final String LOG_TAG = "ReportPrepareActivity";
    private final int XOZ = 1;
    private final int YOZ = 2;
    private final int XOY = 3;
    ReportPreparePresenter rpPresenter;
    GraphViewCreator graphViewCreator;
    private ActivityReportPrepareBinding binding;
    Long buildingID;
    int levels[];

    private boolean isXozBtnPressed = false;
    private boolean isJournalBtnPressed = false;
    private boolean isReportBtnPressed = false;

    private boolean isReportCreationFinished = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_prepare);

        buildingID = getIntent().getLongExtra("building_id", 0);
        levels = getIntent().getIntArrayExtra("levels");
        /*Log.d(LOG_TAG, " Levels is recieved: Levels[1] = " + levels[1] + "; "
                + "levels[2] = " + levels[2]
        );*/
        rpPresenter = new ReportPreparePresenter(buildingID, levels, this, this);
        graphViewCreator = GraphViewCreator.getInstance(this,
                rpPresenter.getGraphViewData().get("points"),
                rpPresenter.getGraphViewData().get("range"),
                rpPresenter.countDeviation());
        rpPresenter.createGraphView(graphViewCreator);


        /*Log.d(LOG_TAG, "Building name: " + rpPresenter.getReportBuilding().getName() + "\n"
                + "creation date: " + rpPresenter.getReportBuilding().getCreationDate()
        );*/

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

        binding.tvTitleReport.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));

        binding.imbtnXOZ.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if (!isXozBtnPressed) {
                    binding.imbtnXOZ.setImageDrawable(getDrawable(R.drawable.xozpressed));
                    isXozBtnPressed = true;
                }

                try {
                    Thread.sleep(200);
                    //binding.imbtnXOZ.setImageDrawable(getDrawable(R.drawable.testcube));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                //*** End test ***
                startActivity(graphViewPagerIntent);
                //binding.imbtnXOZ.setImageDrawable(getDrawable(R.drawable.testcube));
            }
        });

        binding.imbtnJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isJournalBtnPressed) {
                    binding.imbtnJournal.setImageDrawable(getDrawable(R.drawable.tablepressed));
                    isJournalBtnPressed = true;
                }

                try {
                    Thread.sleep(200);
                    //binding.imbtnXOZ.setImageDrawable(getDrawable(R.drawable.testcube));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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

        binding.imbtnReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (!isReportBtnPressed) {
                    binding.imbtnReport.setImageDrawable(getDrawable(R.drawable.titlepressed));
                    isReportBtnPressed = true;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rpPresenter.createReportDocFile(graphViewCreator);
                    }
                }).start();

                Toast.makeText(ReportPrepareActivity.this, "Отчет сохранен в папку \"Загрузки\"", Toast.LENGTH_LONG).show();
            }
        });

        binding.btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = getIntent();
            setResult(RESULT_OK);
            finish();
                //sendEmail();
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

        if (isReportBtnPressed) {
            binding.imbtnReport.setImageDrawable(getDrawable(R.drawable.title));
            isReportBtnPressed = false;
        }
        //rpPresenter.get
    }

    public void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i(LOG_TAG, "Send email");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ReportPrepareActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onPostResume() {
        super.onPostResume();
        isXozBtnPressed = false;
        isJournalBtnPressed = false;
        isReportBtnPressed = false;
        binding.imbtnXOZ.setImageDrawable(getDrawable(R.drawable.testcube));
        binding.imbtnJournal.setImageDrawable(getDrawable(R.drawable.table));
        binding.imbtnReport.setImageDrawable(getDrawable(R.drawable.title));
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}