package com.tamamura.qisen;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import ClassModel.ModelGuru;
import ClassModel.ModelSiswa;
import Client.UserAction;
import UserSession.Session;
import handler.appHandler;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Session session;

    DrawerLayout dashboard_drawer;
    NavigationView dashboard_navigation_view;
    ActionBarDrawerToggle drawerToggle;

    Toolbar dashboard_toolbar;

    UserAction userAction;

    appHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        classInit();
        viewInit();

        fetchUserInformation();

        logic();
    }

    private void classInit() {
        session = new Session(this);
        userAction = new UserAction(this);
        handler = new appHandler(this);
    }

    private void viewInit() {
        dashboard_drawer = findViewById(R.id.dashboard_drawer);
        dashboard_navigation_view = findViewById(R.id.nav_view);
        dashboard_toolbar = findViewById(R.id.dashboard_toolbar);


        setSupportActionBar(dashboard_toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar_layout);

        drawerToggle = new ActionBarDrawerToggle(this, dashboard_drawer, R.string.nav_open, R.string.nav_close);

        dashboard_drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    private void logic() {


        //user name

        //end of user name

        dashboard_navigation_view.setNavigationItemSelectedListener(this);

    }

    ModelSiswa siswa = new ModelSiswa();
    ModelGuru guru = new ModelGuru();

    private void setData() {
        TextView text_nama = getSupportActionBar().getCustomView().findViewById(R.id.text_nama);
        text_nama.setText(session.getWhoami().equals("siswa") ? siswa.getNama() : guru.getNama());
    }


    private void userInformation(String result) throws JSONException {

        if (result.isEmpty()) {
            Toast.makeText(this, "no_data", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject object = new JSONObject(result);
            String name = session.getWhoami().equals("siswa") ? "siswa" : (session.getWhoami().equals("guru") ? "guru" : null);
            JSONArray jsonArray = object.getJSONArray(name);

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    if (obj.getBoolean("res")) {
                        if (session.getWhoami().equals("siswa")) {
                            siswa.setNIS(obj.getString("NIS"));
                            siswa.setNama(obj.getString("nama"));
                            siswa.setTanggal_lahir(obj.getString("tanggal_lahir"));
                            siswa.setTempat_lahir(obj.getString("tempat_lahir"));
                            siswa.setAlamat(obj.getString("alamat"));
                            siswa.setJenis_kelamin(obj.getString("jenis_kelamin"));
                            siswa.setAgama(obj.getString("agama"));
                            siswa.setFoto(obj.getString("foto"));
                            siswa.setKelas(obj.getString("kelas"));
                        } else if (session.getWhoami().equals("guru")) {
                            guru.setNIK(obj.getString("NIK"));
                            guru.setNama(obj.getString("nama"));
                            guru.setTanggal_lahir(obj.getString("tanggal_lahir"));
                            guru.setTempat_lahir(obj.getString("tempat_lahir"));
                            guru.setAlamat(obj.getString("alamat"));
                            guru.setJenis_kelamin(obj.getString("jenis_kelamin"));
                            guru.setAgama(obj.getString("agama"));
                            guru.setFoto(obj.getString("foto"));
                        }
                    } else {
                        Toast.makeText(this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                }
            }

        }
        setData();
    }

    @SuppressLint("StaticFieldLeak")
    private void fetchUserInformation() {
        new AsyncTask<Void, Void, String>() {


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    userInformation(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res = "";


                try {
                    String user = session.getWhoami().equals("siswa") ? "siswa" : (session.getWhoami().equals("guru") ? "guru" : null);
                    String param = session.getWhoami().equals("siswa") ? "&NIS=" + session.getNIS() : (session.getWhoami().equals("guru") ? "&NIK=" + session.getNIK() : null);
                    res = userAction.userProfile("?user=" + user + param);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return res;
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {

        if (dashboard_drawer.isDrawerOpen(GravityCompat.START)) {
            dashboard_drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                Toast.makeText(this, "Selected `user`", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                handler.userLogout();
                break;
            default:
                break;
        }
        return true;
    }
}