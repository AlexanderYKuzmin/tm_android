package com.example.appstraining.towermeasurement.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appstraining.towermeasurement.MainPresenter;
import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.SectionListAdapterHelper;
import com.example.appstraining.towermeasurement.SpinnerConfigAdapterHelper;
import com.example.appstraining.towermeasurement.SpinnerTypeAdapterHelper;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.model.Section;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainViewInterface, View.OnClickListener
        , AdapterView.OnItemClickListener {

    public Context context = this;
    private MainPresenter mainPresenter;
    private final String LOG_TAG = "mainActivity.class";
    private final static int MEASURE_ACTIVITY_REQUEST_CODE = 0;
    //private final static int REPORT_ACTIVITY_REQUEST_CODE = 0;

   // private FragmentTransaction fragmentTransaction;
    private Fragment modelingFragment;

    private MainActivityMode activityMode = MainActivityMode.LAST_LOADED_OBJECT;

    TextView tvTitle;
    TextView tvId;
    //EditText tvId_1;
    EditText etName, etAddress;
    Spinner spinnerType, spinnerConfig;
    EditText etHeight, etSectionsNumber;
    Button btnCreate;
    Button btnFirst, btnSecond, btnThird;
    ListView lvSections;

    SectionListAdapterHelper sectionListAdapterHelper;
    SpinnerTypeAdapterHelper spTypeHelper;
    SpinnerConfigAdapterHelper spConfigHelper;
    ArrayAdapter<String> typeSpinnerAdapter;
    ArrayAdapter<String> configSpinnerAdapter;
    SimpleAdapter simpleAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvId = (TextView) findViewById(R.id.tvId);
        //tvId_1 = (EditText) findViewById(R.id.etId);
        etName = (EditText) findViewById(R.id.etObjectName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etSectionsNumber = (EditText) findViewById(R.id.etAmountSec);
        btnCreate = (Button) findViewById(R.id.btnCreateSections);
        btnCreate.setOnClickListener(this);
        btnFirst = findViewById(R.id.btnReg);
        btnFirst.setOnClickListener(this);
        btnSecond = findViewById(R.id.btnContinue);
        btnSecond.setOnClickListener(this);
        btnThird = findViewById(R.id.btnReset);
        btnThird.setOnClickListener(this);

        lvSections = (ListView) findViewById(R.id.lvSections);

        mainPresenter = new MainPresenter(this, context);
        getLifecycle().addObserver(mainPresenter);
        //mainPresenter.getLastInputObject();

        spTypeHelper = new SpinnerTypeAdapterHelper(this);
        spConfigHelper = new SpinnerConfigAdapterHelper(this);
        typeSpinnerAdapter = spTypeHelper.getAdapter();
        configSpinnerAdapter = spConfigHelper.getAdapter();
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        configSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType = (Spinner) findViewById(R.id.spType);
        spinnerConfig = (Spinner) findViewById(R.id.spConfig);
        spinnerType.setAdapter(typeSpinnerAdapter);
        spinnerConfig.setAdapter(configSpinnerAdapter);

        sectionListAdapterHelper = new SectionListAdapterHelper(context);
        //data = sectionListAdapterHelper.getData();
        simpleAdapter = sectionListAdapterHelper.getAdapter();
        SimpleAdapter.ViewBinder binder = new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
               // Log.d(LOG_TAG, "Method setViewValue is working!!!");
                if (view.getId() == R.id.llSection) { // item.xml  lenearlayout
                    if(activityMode == MainActivityMode.LOAD_FROM_SERVER ||
                            activityMode == MainActivityMode.LOAD_FROM_STORAGE) {
                        view.setBackground(getResources().getDrawable(R.drawable.listitemstylewhite));
                        return true;
                    }
                    if(mainPresenter.getBuilding() != null) {
                        Log.d(LOG_TAG, "Object data =" + data.toString());

                        if (mainPresenter.checkSectionValues((Integer)data) ==  // to set background color
                                MainPresenter.RESULT_SECTION_VALUES_OK) {
                            view.setBackground(getResources().getDrawable(R.drawable.listitemstylegreen));
                     //       view.setBackgroundColor(getResources().getColor(R.color.green_200));
                        } else {
                            view.setBackground(getResources().getDrawable(R.drawable.listitemstyle));
                        }
                    }
                    return true;
                }
                return false;
            }
        };
                //new SimpleAdapter(context, data, R.layout.item, from, to);
        simpleAdapter.setViewBinder(binder);
        lvSections.setAdapter(simpleAdapter);
        lvSections.setOnItemClickListener(this);

        //DBHelper dbHelper = new DBHelper(context, "localDB", null, 1);
        //SQLiteDatabase localDB = dbHelper.getWritableDatabase();
    }

    @Override                                                           // ActionBarMenu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override                                                           // ActionBarMenu
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mHelp:
                break;
            case R.id.mNew:
                activityMode = MainActivityMode.NEW;
                Log.d(LOG_TAG, "ACTIVITY_MODE = " + String.valueOf(activityMode) + " and "
                        + activityMode.toString()
                );
                updateView(activityMode);
                break;
            case R.id.mPattern:
                break;
            case R.id.mLoadFromServer:
                activityMode = MainActivityMode.LOAD_FROM_SERVER;

                if(modelingFragment != null) {
                    removeAnimatedModel();
                }

                updateView(activityMode);
                break;
            case R.id.mSaveToServer:
                break;
            case R.id.mLoadFromStorage:
                activityMode = MainActivityMode.LOAD_FROM_STORAGE;
                updateView(activityMode);
                break;
            case R.id.mSaveToStorage:
                break;
            case R.id.mQuit:

        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCreateSections :
                switch (activityMode) {
                    case NEW:
                        mainPresenter.createBuilding(etName.getText().toString(),
                                etAddress.getText().toString(),
                                spinnerType.getSelectedItem().toString(),
                                spinnerConfig.getSelectedItem().toString(),
                                etSectionsNumber.getText().toString(),
                                etHeight.getText().toString());
                        sectionListAdapterHelper.updateAdapter(mainPresenter.getSections());
                        simpleAdapter.notifyDataSetChanged();
                        break;
                    case LOAD_FROM_SERVER:
                            /*mainPresenter.getMapOfObjects(etName.getText().toString(),
                                    etAddress.getText().toString());*/
                        new SearchDialogFragment(this, mainPresenter,
                                etName.getText().toString(),
                                etAddress.getText().toString(),
                                R.string.search_dialog_title).show(getSupportFragmentManager(), null);
                        activityMode = MainActivityMode.LAST_LOADED_OBJECT;
                        Log.d(LOG_TAG, "ActivityMode changed to " + activityMode.toString());
                        break;
                    case LOAD_FROM_STORAGE:
                        // to do
                        new InnerSearchDialogFragment(this,
                                R.string.search_dialog_title,
                                etName.getText().toString(),
                                etAddress.getText().toString(),
                                mainPresenter).show(getSupportFragmentManager(), null);
                        activityMode = MainActivityMode.SELECTED_OBJECT;
                        Log.d(LOG_TAG, "Activity mode changed to" + activityMode.toString());
                }
                break;
            case R.id.btnReg :
                switch (activityMode){
                    case NEW:
                        System.out.println("Register pressed!");
                        String postResult = mainPresenter.registerObject();
                        Log.d(LOG_TAG, "Registration result: " + postResult);
                        Toast.makeText(context, "Registration result : " + postResult, Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case LAST_LOADED_OBJECT:
                        // write here update this object in database
                        break;
                    case LOAD_FROM_STORAGE: // Cancel
                        activityMode = MainActivityMode.LAST_LOADED_OBJECT;
                        updateView(activityMode);
                }
                break;

            case R.id.btnContinue :  // This is btnSecond
                switch (activityMode) {
                    case NEW:  // btnSecond name is CONTINUE
                        break;
                    case LAST_LOADED_OBJECT: // btnSecond name is MEASURE
                        Intent intent = new Intent(context, MeasureInputActivity.class);
                        intent.putParcelableArrayListExtra(getString(R.string.startmeasures), mainPresenter.getMeasurements());
                        startActivityForResult(intent, MEASURE_ACTIVITY_REQUEST_CODE);
                        break;
                    case LOAD_FROM_STORAGE:  // OK it is like a createBtn button
                        new InnerSearchDialogFragment(this,
                                R.string.search_dialog_title,
                                etName.getText().toString(),
                                etAddress.getText().toString(),
                                mainPresenter).show(getSupportFragmentManager(), null);
                        activityMode = MainActivityMode.SELECTED_OBJECT;
                        Log.d(LOG_TAG, "Activity mode changed to" + activityMode.toString());
                }
                break;

            case R.id.btnReset : // This is 3d button
                Log.d(LOG_TAG, "Button REPORT is pressed. Activity MODE is " + activityMode);
                switch (activityMode) {
                    case NEW:  // btnThird name is RESET
                        break;
                    case LOAD_FROM_SERVER: // btnThird name is REPORT
                        break;
                    case SELECTED_OBJECT:
                    case LAST_LOADED_OBJECT:
                        mainPresenter.saveToLocalDB();
                        Intent reportIntent = new Intent(context, ReportPrepareActivity.class);
                        reportIntent.putExtra("building_id", mainPresenter.getBuilding().getId());
                        reportIntent.putExtra("levels", mainPresenter.getLevels());
                        startActivity(reportIntent);
                }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(activityMode == MainActivityMode.NEW) { // or pattern
            //EditText etBaseWidth = parent.getChildAt(position).findViewById(R.id.etBaseWidth);
            Log.d(LOG_TAG, "OnItemClick pressed. Building section chosen.");
            new SectionDialogFragment(this, mainPresenter, position + 1)
                    .show(getSupportFragmentManager(), null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MEASURE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {

                mainPresenter.setMeasurements(
                        data.getParcelableArrayListExtra(getString(R.string.measureslist)));
                Log.d(LOG_TAG, "Measurements from MeasurementInputActivity are set.");
                mainPresenter.setResults();
                mainPresenter.saveToLocalDB();
            }
        }
    }

    @Override
    public void updateSectionList(ArrayList<Section> sections) {
        sectionListAdapterHelper.updateAdapter(sections);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateView(MainActivityMode activityMode) {
        String title = "", id = "", name = "", address = "", type = null, config = null,
                height = "",  numberOfSections = "";

        switch (activityMode) {
            case NEW:
                title = this.getString(R.string.title_new);
                btnFirst.setText("REGISTER");
                btnSecond.setText("CONTINUE");
                btnThird.setText("RESET");
                updateFocusable(false,true, true, true, true
                        , true,true
                );
                break;
            case LOAD_FROM_SERVER:

            case LOAD_FROM_STORAGE:
                title = this.getString(R.string.search_request_title);
                tvId.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                etSectionsNumber.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                etHeight.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                spinnerType.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                spinnerConfig.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                updateFocusable(false,true, true, false, false
                        , false,false
                );
                btnFirst.setText("CANCEL");
                btnSecond.setText("OK");
                btnThird.setText("RESET");
                break;

            case USE_TEMPLATE:
                break;
            case LAST_LOADED_OBJECT:
                title = this.getString(R.string.title_last_loaded_building);
                tvId.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                etSectionsNumber.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                etHeight.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                spinnerType.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                spinnerConfig.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                id = String.valueOf(mainPresenter.getBuilding().getId());
                name = mainPresenter.getBuilding().getName();
                address = mainPresenter.getBuilding().getAddress();
                type = String.valueOf(mainPresenter.getBuilding().getType());
                config = String.valueOf(mainPresenter.getBuilding().getConfig());
                height = String.valueOf(mainPresenter.getBuilding().getHeight());
                numberOfSections = String.valueOf(mainPresenter.getBuilding().getNumberOfSections());
                updateFocusable(false, false, false, false, false,
                        false, false);
                //tvId.setText(id);
                btnFirst.setText("UPDATE");
                btnSecond.setText("MEASURE");
                btnThird.setText("REPORT");
                break;
            case SELECTED_OBJECT:
                title = this.getString(R.string.title_view_selected);
                //tvId_1 = (EditText) findViewById(R.id.etId) ;
                tvId.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                etSectionsNumber.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                etHeight.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                spinnerType.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                spinnerConfig.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                id = String.valueOf(mainPresenter.getBuilding().getId());
                //tvId_1.setText(id);
                Log.d(LOG_TAG, " SELECTED Obj update view id = " + id);
                //Log.d(LOG_TAG, "tvId_1 get Text: " + tvId_1.getText().toString());
                name = mainPresenter.getBuilding().getName();
                address = mainPresenter.getBuilding().getAddress();
                type = String.valueOf(mainPresenter.getBuilding().getType());
                config = String.valueOf(mainPresenter.getBuilding().getConfig());
                height = String.valueOf(mainPresenter.getBuilding().getHeight());
                numberOfSections = String.valueOf(mainPresenter.getBuilding().getNumberOfSections());
                updateFocusable(false, false, false, false, false,
                        false, false);
                btnFirst.setText("UPDATE");
                btnSecond.setText("MEASURE");
                btnThird.setText("REPORT");

        }

        tvTitle.setText(String.valueOf(title));
        tvTitle.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));
        tvId.setText(id);
        Log.d(LOG_TAG, "tvId get Text: " + tvId.getText().toString());
        //Log.d(LOG_TAG, "tvId_1 get Text: " + tvId_1.getText().toString());
        etName.setText(name);
        etAddress.setText(address);
        spinnerType.setSelection(typeSpinnerAdapter.getPosition(type));
        spinnerConfig.setSelection(configSpinnerAdapter.getPosition(config));
        etHeight.setText(height);
        etSectionsNumber.setText(numberOfSections);

        if(!(activityMode == MainActivityMode.NEW
                || activityMode == MainActivityMode.LOAD_FROM_SERVER
                || activityMode == MainActivityMode.LOAD_FROM_STORAGE))
        {
            Log.d(LOG_TAG, "createBtnMode = " + String.valueOf(activityMode));
            sectionListAdapterHelper.updateAdapter(mainPresenter.getSections());
            simpleAdapter.notifyDataSetChanged();
            // here to start tower model
        } else {
            sectionListAdapterHelper.updateAdapter();
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void updateFocusable(boolean fId, boolean fName, boolean fAddress, boolean fType, boolean fConfig
            , boolean fHeight, boolean fNumberOfSections) {
        tvId.setFocusable(fId);
        etName.setFocusable(fName);
        etAddress.setFocusable(fAddress);
        etHeight.setFocusable(fHeight);
        etSectionsNumber.setFocusable(fNumberOfSections);
        spinnerType.setEnabled(fType);
        spinnerConfig.setEnabled(fConfig);

    }

    public void showAnimatedModel(Bundle bundle) {
        Log.d(LOG_TAG, "showing animated model. Creating fragment");
        modelingFragment = new TowerModelingFragment();

        modelingFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, modelingFragment);
        fragmentTransaction.commit();
    }

    public void removeAnimatedModel(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(modelingFragment);
        fragmentTransaction.commit();
    }
}