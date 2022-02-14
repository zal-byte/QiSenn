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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.button.MaterialButton;

import UserSession.Session;

public class MainActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE","android.permission.INTERNET"};

    LinearLayout lay1;

    Session session;
    MaterialButton btn_siswa, btn_guru;
    TextView admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(REQUEST_CODE_PERMISSIONS);

        session = new Session(this);



        init();
        try {
            logic();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void init()
    {

        lay1 = findViewById(R.id.lay1);
        btn_siswa = findViewById(R.id.btn_siswa);
        btn_guru = findViewById(R.id.btn_guru);
        admin = findViewById(R.id.admin);

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

            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }



    public void logic() throws InterruptedException {

        if( session.isLogin() == true )
        {
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        }



        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                lay1.setVisibility(View.VISIBLE);
                lay1.startAnimation(animation);

            }
        },900);

        btn_siswa.setOnClickListener( View -> {
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            session.setWhoami("siswa");
        });
        btn_guru.setOnClickListener( View->{
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            session.setWhoami("guru");
        });
        admin.setOnClickListener( View -> {
            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            session.setWhoami("admin");
        });



    }

}