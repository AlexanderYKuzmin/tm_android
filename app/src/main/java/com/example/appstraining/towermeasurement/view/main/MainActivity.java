package com.example.appstraining.towermeasurement.view.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.appstraining.towermeasurement.R;
import com.example.appstraining.towermeasurement.view.main.fragments.DeleteDialogFragment;
import com.example.appstraining.towermeasurement.view.main.fragments.SearchFormDialogFragment;
import com.example.appstraining.towermeasurement.model.adapter.SectionListAdapterHelper;
import com.example.appstraining.towermeasurement.model.adapter.SpinnerConfigAdapterHelper;
import com.example.appstraining.towermeasurement.model.adapter.SpinnerTypeAdapterHelper;
import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.model.Section;
import com.example.appstraining.towermeasurement.view.main.fragments.InnerSearchResultDialogFragment;
import com.example.appstraining.towermeasurement.view.main.fragments.SearchDialogFragment;
import com.example.appstraining.towermeasurement.view.main.fragments.SectionDialogFragment;
import com.example.appstraining.towermeasurement.view.measurement.MeasureInputActivity;
import com.example.appstraining.towermeasurement.view.result.ReportPrepareActivity;
import com.example.appstraining.towermeasurement.view.main.fragments.TowerModelingFragment;
import com.example.appstraining.towermeasurement.view.start.StartActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener
        , AdapterView.OnItemClickListener {

    public Context context = this;
    private MainPresenter mainPresenter;
    private final String LOG_TAG = "mainActivity.class";
    private final static int MEASURE_ACTIVITY_REQUEST_CODE = 0;
    private final static int REPORT_ACTIVITY_REQUEST_CODE = 1;

    private InnerSearchResultDialogFragment innerSearchResultDialogFragment;
    private SearchFormDialogFragment searchFormDialogFragment;
    private DeleteDialogFragment deleteDialogFragment;
   // private FragmentTransaction fragmentTransaction;
    private Fragment modelingFragment;

    private MainActivityMode activityMode = MainActivityMode.LAST_LOADED_OBJECT;
    private boolean isTextWatcherOn = false;

    TextView tvTitle;
    //TextView tvId;
    EditText etId;
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

        mainPresenter = new MainPresenter(this, context);
        getLifecycle().addObserver(mainPresenter);
        /*mainPresenter.setBuildingOnStart(
                Integer.parseInt(AppPropertyHandler.getProperty("id", context))
        );*/

        searchFormDialogFragment = new SearchFormDialogFragment(context, mainPresenter, this);
        innerSearchResultDialogFragment = new InnerSearchResultDialogFragment(context, mainPresenter, this);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etId = findViewById(R.id.etId);
        //tvId_1 = (EditText) findViewById(R.id.etId);
        etName = (EditText) findViewById(R.id.etObjectName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etHeight = (EditText) findViewById(R.id.etHeight);

        etSectionsNumber = (EditText) findViewById(R.id.etAmountSec);


        //btnCreate.setOnClickListener(this);
        btnFirst = findViewById(R.id.btnReg);
        btnFirst.setOnClickListener(this);
        btnSecond = findViewById(R.id.btnContinue);
        btnSecond.setOnClickListener(this);
        btnThird = findViewById(R.id.btnReset);
        btnThird.setOnClickListener(this);

        lvSections = (ListView) findViewById(R.id.lvSections);

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
        spinnerType.setSelection(1);
        Log.d(LOG_TAG, "spinnerType origin" + spinnerType.getSelectedItem());
        spinnerConfig.setAdapter(configSpinnerAdapter);
        spinnerConfig.setSelection(1);
        Log.d(LOG_TAG, "spinnerConfig origin" + spinnerConfig.getSelectedItem());

        etSectionsNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if(isTextWatcherOn) {
                if(activityMode == MainActivityMode.NEW) {
                    if (etHeight.getText().length() > 0 && s.length() > 0) {
                        Log.d(LOG_TAG, "TextWatcher is ON. Starting to create building. Editable s = " + s.toString());
                        sendDataToCreateObj(s);
                    /*mainPresenter.createBuilding(etName.getText().toString(),
                            etAddress.getText().toString(),
                            spinnerType.getSelectedItem().toString(),
                            spinnerConfig.getSelectedItem().toString(),
                            s.toString(),
                            etHeight.getText().toString());*/
                        sectionListAdapterHelper.updateAdapter(mainPresenter.getSections());
                        simpleAdapter.notifyDataSetChanged();
                        //}
                    }
                }

                isTextWatcherOn = false;

                //updateView(activityMode);
            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override                                                           // ActionBarMenu
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //isTextWatcherOn = false;
        switch (item.getItemId()){
            case R.id.mHelp:
                break;
            case R.id.mNew:
                activityMode = MainActivityMode.NEW;
                updateView(activityMode);
                break;
            case R.id.mPattern:
                activityMode = MainActivityMode.USE_TEMPLATE;
                showSearchFormDialogFragment();
                break;
            /*case R.id.mLoadFromServer:
                activityMode = MainActivityMode.LOAD_FROM_SERVER;

                if(modelingFragment != null) {
                    removeAnimatedModel();
                }
                updateView(activityMode);
                break;
            case R.id.mSaveToServer:
                break;*/
            case R.id.mLoadFromStorage:
                activityMode = MainActivityMode.LOAD_FROM_STORAGE;
                showSearchFormDialogFragment();
                break;
            case R.id.mSaveToStorage:
                mainPresenter.saveToLocalDB();
                break;
            case R.id.mDelete:
                activityMode = MainActivityMode.DELETE;
                Log.d(LOG_TAG, "(Delete) search form dialog = " + searchFormDialogFragment);
                showSearchFormDialogFragment();
                break;
            case R.id.mQuit:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnReg :
                switch (activityMode){
                    case USE_TEMPLATE:
                        Log.d(LOG_TAG, "register spinnerType = " + spinnerType.getSelectedItem());

                        mainPresenter.createBuilding(etName.getText().toString(),
                                etAddress.getText().toString(),
                                spinnerType.getSelectedItem().toString(),
                                spinnerConfig.getSelectedItem().toString(),
                                etSectionsNumber.getText().toString(),
                                etHeight.getText().toString());
                        Log.d(LOG_TAG, "Sections :" + mainPresenter.getSections());
                        sectionListAdapterHelper.updateAdapter(mainPresenter.getSections());
                        simpleAdapter.notifyDataSetChanged();
                    case NEW:
                        String postResult = mainPresenter.registerObject();
                        Log.d(LOG_TAG, "Registration result: " + postResult);
                        Toast.makeText(context, "Registration result : " + postResult, Toast.LENGTH_SHORT)
                                .show();
                        activityMode = MainActivityMode.SELECTED_OBJECT;
                        mainPresenter.mountBuilding(activityMode);
                        break;
                    case LAST_LOADED_OBJECT:
                        // write here update this object in database
                        break;
                    case LOAD_FROM_STORAGE: // Cancel
                        activityMode = MainActivityMode.LAST_LOADED_OBJECT;
                        mainPresenter.mountBuilding(activityMode);
                        //updateView(activityMode);
                }
                break;

            case R.id.btnContinue :  // This is btnSecond
                switch (activityMode) {
                    case NEW:  // btnSecond name is CONTINUE
                        break;
                    case LAST_LOADED_OBJECT: // btnSecond name is MEASURE
                    case SELECTED_OBJECT:
                        Intent intent = new Intent(context, MeasureInputActivity.class);
                        intent.putParcelableArrayListExtra(getString(R.string.startmeasures), mainPresenter.getMeasurements());
                        intent.putExtra("widthbottom", mainPresenter.getSections().get(0).getWidthBottom());
                        intent.putExtra("config", mainPresenter.getBuilding().getConfig());

                        Log.d(LOG_TAG, "Building from MainPresenter" + mainPresenter.getBuilding().getAddress());
                        Log.d(LOG_TAG, "Measurements from building from MainPresenter: " + mainPresenter.getBuilding().getMeasurements().get(0).getLeftAngle());
                        Log.d(LOG_TAG, "Measurements: " + mainPresenter.getMeasurements().get(0).getLeftAngle() + " :: " +  mainPresenter.getMeasurements().get(0).getRightAngle());
                        startActivityForResult(intent, MEASURE_ACTIVITY_REQUEST_CODE);
                        break;
                    case LOAD_FROM_STORAGE:  // OK it is like a createBtn button
                        /*new InnerSearchResultDialogFragment(this,
                                R.string.search_dialog_title,
                                etName.getText().toString(),
                                etAddress.getText().toString(),
                                mainPresenter).show(getSupportFragmentManager(), null);
                        activityMode = MainActivityMode.SELECTED_OBJECT;*/
                        Log.d(LOG_TAG, "Activity mode changed to" + activityMode.toString());
                        break;
                    /*case LOAD_FROM_SERVER:

                        new SearchDialogFragment(this, mainPresenter,
                                etName.getText().toString(),
                                etAddress.getText().toString(),
                                R.string.search_dialog_title_ru).show(getSupportFragmentManager(), null);
                        activityMode = MainActivityMode.LAST_LOADED_OBJECT;
                        Log.d(LOG_TAG, "ActivityMode changed to " + activityMode.toString());
                        break;*/
                }
                break;

            case R.id.btnReset : // This is 3d button
                Log.d(LOG_TAG, "Button REPORT is pressed. Activity MODE is " + activityMode);
                switch (activityMode) {
                    case NEW:  // btnThird name is RESET
                        updateView(activityMode);
                        break;
                    case LOAD_FROM_SERVER: // btnThird name is REPORT
                        break;
                    case SELECTED_OBJECT:
                    case LAST_LOADED_OBJECT:
                        mainPresenter.saveToLocalDB();
                        Intent reportIntent = new Intent(context, ReportPrepareActivity.class);
                        reportIntent.putExtra("building_id", mainPresenter.getBuilding().getId());
                        reportIntent.putExtra("levels", mainPresenter.getLevels());
                        startActivityForResult(reportIntent, REPORT_ACTIVITY_REQUEST_CODE);
                }
        }
    }

    @Override // sectionListAdapter
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //section edition
        if(activityMode == MainActivityMode.NEW || activityMode == MainActivityMode.USE_TEMPLATE) { // or pattern
            //EditText etBaseWidth = parent.getChildAt(position).findViewById(R.id.etBaseWidth);
            Log.d(LOG_TAG, "OnItemClick pressed. Building section chosen.");
            new SectionDialogFragment(this, mainPresenter, position + 1, getActivityMode())
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
                Log.d(LOG_TAG, "Measurements angles: " + mainPresenter.getMeasurements().get(0).getLeftAngle() + " :: " + mainPresenter.getMeasurements().get(0).getRightAngle());

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void updateView(MainActivityMode activityMode) {
        removeAnimatedModel();
        String title = "", id = "", name = " ", address = " ", type = "Башня", config = "4",
                height = "10000",  numberOfSections = "1";

        switch (activityMode) {
            case NEW:
                //removeAnimatedModel();
                title = this.getString(R.string.title_new_ru);

                btnFirst.setText(R.string.register_btn_ru);
                btnSecond.setText(R.string.continue_btn_ru);
                btnThird.setText(R.string.reset_btn_ru);

                updateFocusable(false,true, true, true, true
                        , true,true
                );
                break;
            case LOAD_FROM_SERVER:

            case LOAD_FROM_STORAGE:
                //removeAnimatedModel();
                title = this.getString(R.string.search_request_title_ru);
                etId.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                etSectionsNumber.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                etHeight.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                spinnerType.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                spinnerConfig.setBackground(getResources().getDrawable(R.drawable.etunfocusable));
                updateFocusable(false,true, true, false, false
                        , false,false
                );
                btnFirst.setText(R.string.cancel_btn_ru);
                btnSecond.setText(R.string.ok_btn_ru);
                btnThird.setText(R.string.reset_btn_ru);
                break;

            case USE_TEMPLATE:
                title = this.getString(R.string.title_new_ru);

                name = "Наименование";
                address = "Адрес";
                type = String.valueOf(mainPresenter.getBuilding().getType());
                config = String.valueOf(mainPresenter.getBuilding().getConfig());
                height = String.valueOf(mainPresenter.getBuilding().getHeight());
                numberOfSections = String.valueOf(mainPresenter.getBuilding().getNumberOfSections());

                updateFocusable(false,true, true, false, false
                        , false,false
                );

                btnFirst.setText(R.string.register_btn_ru);
                btnSecond.setText(R.string.continue_btn_ru);
                btnThird.setText(R.string.reset_btn_ru);
                break;
            case LAST_LOADED_OBJECT:
                title = this.getString(R.string.title_last_loaded_building_ru);
                etId.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
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
                btnFirst.setText(R.string.update_btn_ru);
                btnSecond.setText(R.string.measure_btn_ru);
                btnThird.setText(R.string.report_btn_ru);
                break;
            case SELECTED_OBJECT:
                title = this.getString(R.string.title_view_selected_ru);
                //tvId_1 = (EditText) findViewById(R.id.etId) ;
                etId.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                etSectionsNumber.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                etHeight.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                spinnerType.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                spinnerConfig.setBackground(getResources().getDrawable(R.drawable.etinnershadow));
                id = String.valueOf(mainPresenter.getBuilding().getId());
                //id = null;
                //tvId_1.setText(id);

                name = mainPresenter.getBuilding().getName();
                address = mainPresenter.getBuilding().getAddress();
                type = String.valueOf(mainPresenter.getBuilding().getType());
                config = String.valueOf(mainPresenter.getBuilding().getConfig());
                height = String.valueOf(mainPresenter.getBuilding().getHeight());
                numberOfSections = String.valueOf(mainPresenter.getBuilding().getNumberOfSections());
                updateFocusable(false, false, false, false, false,
                        false, false);
                btnFirst.setText(R.string.update_btn_ru);
                btnSecond.setText(R.string.measure_btn_ru);
                btnThird.setText(R.string.report_btn_ru);

                mainPresenter.setLastInputObject();
        }

        tvTitle.setText(String.valueOf(title));
        tvTitle.setShadowLayer(3,4,4, getResources().getColor(R.color.text_shadow));
        etId.setText(id);
        Log.d(LOG_TAG, "tvId get Text: " + etId.getText().toString());
        //Log.d(LOG_TAG, "tvId_1 get Text: " + tvId_1.getText().toString());
        etName.setText(name);
        etAddress.setText(address);
        spinnerType.setSelection(typeSpinnerAdapter.getPosition(type));
        spinnerConfig.setSelection(configSpinnerAdapter.getPosition(config));
        etHeight.setText(height);
        etSectionsNumber.setText(numberOfSections);

        if (!(activityMode == MainActivityMode.NEW
                || activityMode == MainActivityMode.LOAD_FROM_SERVER
                || activityMode == MainActivityMode.LOAD_FROM_STORAGE
                || activityMode == MainActivityMode.USE_TEMPLATE)
        )
        {
            Log.d(LOG_TAG, "createBtnMode = " + String.valueOf(activityMode));
            sectionListAdapterHelper.updateAdapter(mainPresenter.getSections());
            // here to start tower model
        } else {
            isTextWatcherOn = activityMode == MainActivityMode.NEW;

            if (activityMode == MainActivityMode.NEW) {
                sectionListAdapterHelper.updateAdapter();
            } else if (activityMode == MainActivityMode.USE_TEMPLATE) {
                sectionListAdapterHelper.updateAdapter(mainPresenter.getSections());
            }

        }
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateFocusable(boolean fId, boolean fName, boolean fAddress, boolean fType, boolean fConfig
            , boolean fHeight, boolean fNumberOfSections) {
        /*etId.setFocusable(fId);
        etName.setFocusable(fName);
        etAddress.setFocusable(fAddress);
        etHeight.setFocusable(fHeight);
        etSectionsNumber.setFocusable(fNumberOfSections);*/
        spinnerType.setEnabled(fType);
        spinnerConfig.setEnabled(fConfig);
        etName.setEnabled(fName);
        etAddress.setEnabled(fAddress);
        etHeight.setEnabled(fHeight);
        etSectionsNumber.setEnabled(fNumberOfSections);

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
        if (modelingFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(modelingFragment);
            fragmentTransaction.commit();
        }
    }

    private void sendDataToCreateObj(Editable s) {
        mainPresenter.createBuilding(
                etName.getText() != null ? etName.getText().toString() : "No name",
                etAddress.getText() != null ? etAddress.getText().toString() : "No address",
                spinnerType.getSelectedItem().toString(),
                spinnerConfig.getSelectedItem().toString(),
                s != null ? s.toString() : "1",
                etHeight.getText() != null ? etHeight.getText().toString() : "10000"
        );
    }

    public void showSearchFormDialogFragment() {
        searchFormDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void showInnerSearchResultDialogFragment() {
        innerSearchResultDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void showDeleteDialogFragment() {
        deleteDialogFragment = new DeleteDialogFragment(context, mainPresenter, this);
        deleteDialogFragment.show(getSupportFragmentManager(), null);
    }

    public void showMessage() {
        Toast.makeText(context, "Объекты не найдены", Toast.LENGTH_SHORT);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setActivityMode(MainActivityMode activityMode) {

    }

    @Override
    public MainActivityMode getActivityMode() {
        return activityMode;
    }
}