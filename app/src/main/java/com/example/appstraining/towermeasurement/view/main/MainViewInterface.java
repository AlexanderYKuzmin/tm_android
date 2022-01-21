package com.example.appstraining.towermeasurement.view.main;

import android.os.Bundle;

import com.example.appstraining.towermeasurement.model.MainActivityMode;
import com.example.appstraining.towermeasurement.model.Section;

import java.util.ArrayList;

public interface MainViewInterface {

    void updateView(MainActivityMode activityMode);

    void updateFocusable(boolean mId, boolean fName, boolean fAddress, boolean fType, boolean fConfig
            , boolean fHeight, boolean fNumberOfSections);

    void updateSectionList(ArrayList<Section> sections);

    void showAnimatedModel(Bundle bundle);

    void removeAnimatedModel();

    void onPause();

    void showSearchFormDialogFragment();

    void showInnerSearchResultDialogFragment();

    void showMessage();

}
