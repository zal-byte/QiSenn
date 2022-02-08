package com.tamamura.qisen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import Client.UserAction;
import UserSession.Session;

public class LoginActivity extends AppCompatActivity {

    UserAction userAction;
    TextInputEditText identifier,password;
    ImageView ic_back_login;
    Session session;
    MaterialButton btn_login;

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
        session = new Session(this);
    }
    private void viewInit()
    {
        identifier = findViewById(R.id.identifier);
        password = findViewById(R.id.password);
        ic_back_login = findViewById(R.id.ic_back_login);
        userAction = new UserAction(this);

        btn_login = findViewById(R.id.btn_login);
    }
    private void logic()
    {
        ic_back_login.setOnClickListener( View -> {
            LoginActivity.this.finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

        btn_login.setOnClickListener( View -> {
            Toast.makeText(this, "Hello world", Toast.LENGTH_SHORT).show();
        });
    }
}