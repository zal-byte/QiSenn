package com.tamamura.qisen.guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.tamamura.qisen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Adapter.LaporanSiswaAdapter;
import ClassModel.ModelLaporanKelas;
import Client.UserAction;
import UserSession.Session;

public class LaporanSiswa extends AppCompatActivity {

    RecyclerView tampil_recyclerview;
    Spinner tanggal_spinner;


    UserAction userAction;
    Session session;

    Toolbar laporan_siswa_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_siswa);
        classInit();
        init();
        logic();
    }

    void classInit() {
        userAction = new UserAction(this);
        session = new Session(this);
    }

    void init() {
        tampil_recyclerview = findViewById(R.id.tampil_recyclerview);
        tanggal_spinner = findViewById(R.id.tanggal_spinner);
        laporan_siswa_toolbar = findViewById(R.id.laporan_kelas_toolbar);


        setSupportActionBar(laporan_siswa_toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24);
        getSupportActionBar().setTitle("Laporan Siswa");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LaporanSiswa.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void logic() {

        tanggal_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getAbsenByTanggal(tanggal_list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getTanggal();
    }

    private void getTanggal() {
        String param = "?request=getTanggal&kelas=" + session.getKelas();
        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    tanggal_parse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }

    private void getAbsenByTanggal(String tanggal) {
        String param = "?request=getAbsenByTanggal&kelas=" + session.getKelas() + "&tanggal=" + tanggal;
        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    kelas_parse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }

    List<String> tanggal_list = new ArrayList<>();

    void tanggal_parse(String response) throws JSONException {
        tanggal_list.clear();
        if (response.isEmpty()) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Tidak ada respon dari server", Snackbar.LENGTH_LONG).show();
        } else {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("getTanggal");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("res") != false) {
                        tanggal_list.add(object.getString("tanggal_absen"));
                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView(), object.getString("msg"), Snackbar.LENGTH_LONG).show();
                    }
                }
                setSpinnerTanggal();
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Tidak ada data", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    ArrayAdapter<String> tanggal_adapter;

    void setSpinnerTanggal() {
        tanggal_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tanggal_list);
        tanggal_spinner.setAdapter(tanggal_adapter);
    }

    ArrayList<ModelLaporanKelas> data = new ArrayList<>();

    void kelas_parse(String response) throws JSONException {
        if (response.isEmpty()) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Tidak ada respon dari server", Snackbar.LENGTH_LONG).show();
        } else {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("getAbsenByTanggal");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("res") != false) {
                        ModelLaporanKelas mlk = new ModelLaporanKelas();
                        mlk.setImg_date(object.getString("img_date"));
                        mlk.setImg_time(object.getString("img_time"));
                        mlk.setPath(object.getString("path"));
                        mlk.setJam_absen(object.getString("jam_absen"));
                        mlk.setTanggal_absen(object.getString("tanggal_absen"));
                        mlk.setStatus_absen(object.getString("status_absen"));
                        mlk.setNis(object.getString("nis"));


                        data.add(mlk);
                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView(), object.getString("msg"), Snackbar.LENGTH_LONG).show();
                    }
                }
                setData();
            } else {
                Snackbar.make(getWindow().getDecorView().getRootView(), "no_data", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    LaporanSiswaAdapter adapter;

    void setData() {
        adapter = new LaporanSiswaAdapter(this, data);
        if (!data.isEmpty()) {

            tampil_recyclerview.setLayoutManager(new LinearLayoutManager(this));
            tampil_recyclerview.setAdapter(adapter);

        } else {
            Toast.makeText(this, "null on arraylist", Toast.LENGTH_SHORT).show();
        }
    }

}