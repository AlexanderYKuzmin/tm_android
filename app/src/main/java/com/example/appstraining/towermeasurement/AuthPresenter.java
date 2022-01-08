package com.example.appstraining.towermeasurement;

import android.content.Context;

import com.example.appstraining.towermeasurement.view.AuthView;

public class AuthPresenter {
    private Context context;
    //private final LifecycleHandler mLifecycleHandler;
    private final AuthView mAuthView;

    public AuthPresenter(AuthView authView){
        mAuthView = authView;
    }


}
