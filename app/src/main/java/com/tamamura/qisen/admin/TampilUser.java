package com.tamamura.qisen.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
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

import Client.UserAction;

public class TampilUser extends AppCompatActivity {


    Spinner kelas_spinner;
    UserAction userAction;

    RecyclerView tampil_recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_user);


        classInit();
        init();
        logic();
    }

    private String whoami()
    {
        if( getIntent() != null )
        {
            return getIntent().getStringExtra("user");
        }else
        {
            return null;
        }
    }

    void classInit()
    {
        userAction = new UserAction(this);
    }

    void init()
    {
        kelas_spinner = findViewById(R.id.kelas_spinner);
        tampil_recyclerview = findViewById(R.id.tampil_recyclerview);
    }
    void logic()
    {
        kelas_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TampilUser.this, kelas_list.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getKelas();
    }


    List<String> kelas_list = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    private void sparseKelas(String result ) throws JSONException {
        System.out.println("getkelas : " + result);
        if( result.isEmpty())
        {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Tidak ada respon dari server", Snackbar.LENGTH_SHORT).show();
        }else
        {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("getKelas");

            if( jsonArray.length() > 0 )
            {
                for(int i = 0; i < jsonArray.length();i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if( object.getBoolean("res") != false )
                    {
                        kelas_list.add(object.getString("kelas"));
                    }else
                    {
                        if( object.getString("msg").toLowerCase().equals("eksekusi_gagal"))
                        {
                            Snackbar.make(getWindow().getDecorView().getRootView(), "Gagal mengeksekusi data", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
                if( !kelas_list.isEmpty())
                {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kelas_list);
                    kelas_spinner.setAdapter(adapter);
                }
            }else
            {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Tidak ada data", Snackbar.LENGTH_SHORT).show();
            }

        }
    }

    private void getKelas()
    {
        String param = "?request=getKelas";
        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    sparseKelas( response );
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

}