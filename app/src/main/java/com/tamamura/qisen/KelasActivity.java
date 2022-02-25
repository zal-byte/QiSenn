package com.tamamura.qisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

import Adapter.KelasAdapter;
import ClassModel.ModelKelas;
import Client.UserAction;
import UserSession.Session;

public class KelasActivity extends AppCompatActivity {

    UserAction userAction;
    Session session;


    RecyclerView recyclerView;
    TextView text_class_error;
    LinearLayout no_data;

    Toolbar kelas_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelas);


        classInit();
        viewInit();
        logic();

    }

    private void classInit() {

        userAction = new UserAction(this);
        session = new Session(this);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                KelasActivity.this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    private void viewInit() {

        recyclerView = (RecyclerView) findViewById(R.id.kelas_recyclerview);
        no_data = (LinearLayout) findViewById(R.id.no_data);
        text_class_error = findViewById(R.id.text_class_error);
        kelas_toolbar = findViewById(R.id.kelas_toolbar);






    }

    private KelasAdapter adapter;
    private ArrayList<ModelKelas> arr = new ArrayList<>();

    private void logic() {


        setSupportActionBar(kelas_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24);
        getSupportActionBar().setTitle(session.getKelas());


        getMyClass();

    }

    private void sparse(String result) throws JSONException {
        if (result.isEmpty()) {
            Toast.makeText(KelasActivity.this, "no response", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("myStudent");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("res") != false) {
                        ModelKelas modelKelas = new ModelKelas();
                        modelKelas.setKelas(object.getString("kelas"));
                        modelKelas.setIdentifier(object.getString("nis"));
                        modelKelas.setNama(object.getString("nama"));
                        modelKelas.setTanggal_lahir(object.getString("tanggal_lahir"));
                        modelKelas.setTempat_lahir(object.getString("tempat_lahir"));
                        modelKelas.setAlamat(object.getString("alamat"));
                        modelKelas.setFoto(object.getString("foto"));
                        modelKelas.setAgama(object.getString("agama"));

                        arr.add(modelKelas);

                    } else {
                        Snackbar.make(KelasActivity.this.getWindow().getDecorView().getRootView(), object.getString("msg"), Snackbar.LENGTH_SHORT).show();
                    }
                }

                if (arr.isEmpty()) {
                    no_data.setVisibility(View.VISIBLE);
                    text_class_error.setText("Siswa " + session.getKelas() + " Tidak ditemukan.");
                } else {
                    setData();
                }
            } else {
                Toast.makeText(KelasActivity.this, "no_data-", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setData() {
        adapter = new KelasAdapter(arr, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


    }

    @SuppressLint("StaticFieldLeak")
    private void getMyClass() {
        Toast.makeText(KelasActivity.this, session.getKelas(), Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(KelasActivity.this);

        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + "?request=myStudent&kelas=" + session.getKelas(), new Response.Listener<String>() {
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
                Toast.makeText(KelasActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(sr);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.kelas_option_menu, menu);

//        MenuItem kembali = menu.findItem(R.id.kembali);
        MenuItem searchItem = menu.findItem(R.id.kelas_search);


        SearchManager searchManager = (SearchManager) KelasActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(KelasActivity.this.getComponentName()));

        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        ArrayList<ModelKelas> filteredList = new ArrayList<>();
        for (ModelKelas item : arr) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

}