package com.tamamura.qisen;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class DashboardActivity extends AppCompatActivity {


    Session session;

    DrawerLayout dashboard_drawer;
    NavigationView dashboard_navigation_view;

    Toolbar dashboard_toolbar;

    UserAction userAction;

    appHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

    }

    private void logic() {

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar_layout);


        //user name


        //end of user name

    }

    ModelSiswa siswa = new ModelSiswa();
    ModelGuru guru = new ModelGuru();

    private void setData() {
        TextView text_nama = getSupportActionBar().getCustomView().findViewById(R.id.text_nama);
        text_nama.setText(session.getWhoami().equals("siswa") ? siswa.getNama() : guru.getNama());
    }


    private void userInformation(String result) throws JSONException {

        if (result.isEmpty()) {
            handler.showToast("no_data");
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
                        handler.showToast(obj.getString("msg"));
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
}