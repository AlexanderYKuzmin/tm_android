package com.example.appstraining.towermeasurement.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appstraining.towermeasurement.AuthPresenter;
import com.example.appstraining.towermeasurement.R;

public class StartActivity extends AppCompatActivity implements AuthView {
    TextView tvGreating;
    EditText etEmail;
    EditText etPassword;
    Button btnLogin;

    AuthView authView;
    AuthPresenter authPresenter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent); //Перенести отсюда в презентер
        authPresenter = new AuthPresenter(this);

        tvGreating = (TextView) findViewById(R.id.tvGreating);
        tvGreating.setGravity(Gravity.CENTER);
        tvGreating.setText("Welcome to\n Tower Measurement!");
        tvGreating.setTextAppearance(R.style.TitleStyle);
        tvGreating.setShadowLayer(5,8,8, getResources().getColor(R.color.text_shadow));

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainScreen();
            }
        });
    }

    @Override
    public void openMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void showPasswordError() {

    }
}
