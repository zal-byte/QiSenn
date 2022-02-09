package com.tamamura.qisen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import UserSession.Session;

public class DashboardActivity extends AppCompatActivity {


    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        classInit();
        viewInit();
        logic();
    }

    private void classInit()
    {
        session = new Session(this);
    }
    private void viewInit()
    {

    }
    private void logic()
    {

    }
}