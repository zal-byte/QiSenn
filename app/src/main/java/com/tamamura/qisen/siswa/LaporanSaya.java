package com.tamamura.qisen.siswa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tamamura.qisen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.LaporanAdapter;
import ClassModel.ModelLaporan;
import Client.UserAction;
import UserSession.Session;

public class LaporanSaya extends AppCompatActivity {

    RecyclerView laporan_recyclerview;
    UserAction userAction;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_saya);

        userAction = new UserAction(this);
        session = new Session(this);

        init();
        logic();
    }

    void logic() {
        load();
    }

    ArrayList<ModelLaporan> modelLaporans = new ArrayList<>();

    void parse(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);

        JSONArray jsonArray = jsonObject.getJSONArray("myAbsen");
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                if (object.getBoolean("res") != false) {
                    ModelLaporan modelLaporan = new ModelLaporan();
                    modelLaporan.setNis(object.getString("nis"));
                    modelLaporan.setStatus_absen(object.getString("status_absen"));
                    modelLaporan.setTanggal_absen(object.getString("tanggal_absen"));



                    modelLaporans.add(modelLaporan);

                } else {
                    Toast.makeText(LaporanSaya.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }


            setData();

        } else {
            Toast.makeText(LaporanSaya.this, "no_data-_-_", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData() {
        laporan_recyclerview.setLayoutManager(new LinearLayoutManager(LaporanSaya.this, LinearLayoutManager.VERTICAL, false));


        LaporanAdapter adapter = new LaporanAdapter(modelLaporans, this);
        laporan_recyclerview.setAdapter(adapter);

    }

    void load() {

        String param = "?request=myAbsen&nis=" + session.getNIS();
        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parse(response);
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


    void init() {
        laporan_recyclerview = findViewById(R.id.recyclerview_laporan);
    }
}