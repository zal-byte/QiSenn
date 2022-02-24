package com.tamamura.qisen.guru;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tamamura.qisen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import Adapter.CekDisiniAdapter;
import ClassModel.ModelCekDisini;
import Client.UserAction;
import UserSession.Session;

public class CekDisini extends AppCompatActivity {

    RecyclerView cekdisini_recyclerview;
    Session session;
    UserAction userAction;

    MaterialButton hadir, tidak_hadir;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_disini);

        userAction = new UserAction(this);
        session = new Session(this);

        init();
        logic();
    }

    ImageButton cek_disini_back;

    @RequiresApi(api = Build.VERSION_CODES.O)
    void logic()
    {
        cek_disini_back.setOnClickListener( View -> {
            CekDisini.this.finish();
        });


        hadir.setOnClickListener( View -> {
            loads("hadir");
        });
        tidak_hadir.setOnClickListener( View -> {
            loads("tidak_hadir");
        });

        load();
    }


    ArrayList<ModelCekDisini> modelCekDisinis = new ArrayList<>();
    void sparse(String result) throws JSONException {
        System.out.println("cekDisini : " + result);

        JSONObject jsonObject = new JSONObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("cekDisini");




        if(jsonArray.length()>0)
        {
            for(int i = 0; i < jsonArray.length();i++)
            {
                JSONObject object = jsonArray.getJSONObject(i);
                if( object.getBoolean("res") != false)
                {

                    ModelCekDisini cekDisini = new ModelCekDisini();
                    cekDisini.setNama(object.getString("nama"));
                    cekDisini.setStatus_absen(object.getString("status_absen"));
                    cekDisini.setImg_date(object.getString("img_date"));
                    cekDisini.setImg_time(object.getString("img_time"));
                    cekDisini.setPath(object.getString("path"));

                    modelCekDisinis.add(cekDisini);


                }else
                {
                    Toast.makeText(this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }

            //set adapter
            setAdapter();

        }else
        {
            Toast.makeText(this, "tidak ada data", Toast.LENGTH_SHORT).show();
        }
    }

    CekDisiniAdapter adapter;
    void setAdapter()
    {
        adapter = new CekDisiniAdapter(this, modelCekDisinis);
        cekdisini_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cekdisini_recyclerview.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void loads(String test)
    {
        modelCekDisinis.clear();
        LocalDate localDate =  LocalDate.now();
        String sparam = "?request=cekDisiniHadir&tanggal=" + localDate.toString() + "&kelas=" + session.getKelas() + "&status=" + test;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + sparam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    sparse( response );
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

        queue.add(sr);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void load()
    {
        LocalDate localDate = LocalDate.now();


        String sparam = "?request=cekDisini&tanggal=" +localDate.toString() + "&kelas=" + session.getKelas();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + sparam, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    sparse(response);
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

        queue.add(sr);
    }
    void init()
    {
        cek_disini_back = findViewById(R.id.cek_disini_back);
        cekdisini_recyclerview = findViewById(R.id.cek_disini_recyclerview);

        hadir = findViewById(R.id.hadir);
        tidak_hadir = findViewById(R.id.tidak_hadir);
    }
}