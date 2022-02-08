package com.tamamura.qisen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Client.UserAction;

public class LoginActivity extends AppCompatActivity {

    UserAction userAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        classInit();
        viewInit();
        logic();

    }
    private void classInit()
    {

    }
    private void viewInit()
    {
        userAction = new UserAction(this);
    }
    private void logic()
    {

    }
}