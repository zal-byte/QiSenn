package com.tamamura.qisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import UserSession.Session;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.INTERNET"};

    RelativeLayout lay1;
    Button test, tost;

    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(REQUEST_CODE_PERMISSIONS);

        session = new Session(this);



        init();
        logic();


    }
    private void init()
    {

        lay1 = findViewById(R.id.lay1);
        test = findViewById(R.id.test);
        tost = findViewById(R.id.tost);

    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    // Function to check and request permission
    public void checkPermission(int requestCode)
    {
        ActivityCompat.requestPermissions(MainActivity.this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                logic();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    public void logic()
    {



        new Handler().postDelayed(() -> {
            Animation slide_up = AnimationUtils.loadAnimation(MainActivity.this.getApplicationContext(), R.anim.slide_up);

            lay1.setVisibility(View.VISIBLE);
            lay1.startAnimation(slide_up);
        },900);


    }

}