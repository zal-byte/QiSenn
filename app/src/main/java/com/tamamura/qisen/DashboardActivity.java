package com.tamamura.qisen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.tamamura.qisen.admin.TambahUser;
import com.tamamura.qisen.admin.TampilUser;
import com.tamamura.qisen.guru.CekDisini;
import com.tamamura.qisen.siswa.LaporanSaya;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

import ClassModel.ModelAdmin;
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


    MaterialButton btn_absen;

    View include_siswa, include_guru, include_admin, dashboard_view_1;
    TextView jangan_lupa_absen_hari_ini;

    CardView dashboard_cardview;


    //bunch of imagebutton

    //<Siswa>
    ImageButton siswa_laporan_saya, siswa_kelas;
    //<Guru>
    ImageButton guru_laporan_siswa, guru_kelas;
    //<Kurikulum>
    ImageButton admin_tambah_guru, admin_edit_guru, admin_hapus_guru;
    ImageButton admin_tambah_siswa, admin_edit_siswa, admin_hapus_siswa;
    //end of <bunch of imagebutton>


    ImageView header_bg;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DashboardActivity.activity = DashboardActivity.this;
        DashboardActivity.staticUserAction = new UserAction(this);
        DashboardActivity.sesi = new Session(this);
        classInit();
        viewInit();

        fetchUserInformation();

        logic();
        DashboardActivity.checkHasAbsen();
        runningTask = new LongOperation();


        if (session.getWhoami().equals("siswa")) {
            runningTask.execute();
        }

    }

    public static AsyncTask<Void, Void, String> runningTask;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (runningTask != null)
            System.out.println("Destroyed!");
        runningTask.cancel(true);


    }

    @SuppressLint("StaticFieldLeak")
    public static final class LongOperation extends AsyncTask<Void, Void, String> {
        private boolean status = true;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(Void... voids) {
            while (status) {
                try {
                    DashboardActivity.checkHasAbsen();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    status = false;
                    this.cancel(true);
                    e.printStackTrace();
                }
            }
            return "executed";
        }
    }


    private static Activity activity;
    private static UserAction staticUserAction;
    private static Session sesi;


    @SuppressLint("StaticFieldLeak")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void checkHasAbsen() {
        LocalDate localDate = LocalDate.now();
        String param = "?request=hasAbsenToday&nis=" + DashboardActivity.sesi.getNIS() + "&tanggal=" + localDate.toString() + "&kelas=" + DashboardActivity.sesi.getKelas();
        StringRequest sr = new StringRequest(Request.Method.GET, staticUserAction.api + param, new Response.Listener<String>() {
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


        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(sr);


    }

    private static void hideBtnAbsen(boolean status) {
        MaterialButton btn_absen = activity.findViewById(R.id.btn_absen);
        TextView jangan_lupa_absen_hari_ini = activity.findViewById(R.id.jangan_lupa_absen_hari_ini);
        if (status) {
            jangan_lupa_absen_hari_ini.setText("Selamat beraktivitas!");
            btn_absen.setVisibility(View.GONE);
        } else {
            jangan_lupa_absen_hari_ini.setText("Jangan lupa\nabsen hari ini!");
            btn_absen.setVisibility(View.VISIBLE);
        }
        System.out.println("Status: " + status);
    }


    private static void parse(String result) throws JSONException {
        System.out.println("Data: " + result);
        if (result.isEmpty()) {
            Snackbar.make(activity.getWindow().getDecorView().getRootView(), "Tidak ada respon dari server", Snackbar.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("hasAbsenToday");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("status") == true) {
                        DashboardActivity.hideBtnAbsen(true);
                    } else {
                        DashboardActivity.hideBtnAbsen(false);
                    }
                }
            }
        }
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
        dashboard_cardview = findViewById(R.id.dashboard_cardview_1);

        dashboard_view_1 = findViewById(R.id.dashboard_view_1);


        header_bg = findViewById(R.id.header_bg);


        setSupportActionBar(dashboard_toolbar);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_toolbar_layout);

        drawerToggle = new ActionBarDrawerToggle(this, dashboard_drawer, R.string.nav_open, R.string.nav_close);

        dashboard_drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btn_absen = findViewById(R.id.btn_absen);
        include_siswa = findViewById(R.id.include_siswa);
        include_guru = findViewById(R.id.include_guru);
        include_admin = findViewById(R.id.include_admin);

        jangan_lupa_absen_hari_ini = findViewById(R.id.jangan_lupa_absen_hari_ini);


        if (session.getWhoami().equals("siswa")) {
            include_siswa.setVisibility(View.VISIBLE);
            header_bg.setImageDrawable(getResources().getDrawable(R.drawable.hp));
        } else if (session.getWhoami().equals("guru")) {
            include_guru.setVisibility(View.VISIBLE);
            header_bg.setImageDrawable(getResources().getDrawable(R.drawable.checklist));
        } else if (session.getWhoami().equals("admin")) {
            include_admin.setVisibility(View.VISIBLE);
            dashboard_cardview.setVisibility(View.GONE);
            dashboard_view_1.setVisibility(View.GONE);
        }


        if (session.getWhoami().equals("siswa")) {
            btn_absen.setText("Foto disini!");
            jangan_lupa_absen_hari_ini.setText("Jangan lupa\nabsen hari ini!");
        } else if (session.getWhoami().equals("guru")) {
            btn_absen.setText("Cek disini!");
            btn_absen.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_checklist_24, 0, 0, 0);
            jangan_lupa_absen_hari_ini.setText("Cek Siswa hari ini!\n");
        }


        // Menu logic

        if (include_siswa.getVisibility() == View.VISIBLE) {
            siswa_laporan_saya = findViewById(R.id.siswa_laporan_saya);
            siswa_kelas = findViewById(R.id.siswa_kelas);
        } else if (include_guru.getVisibility() == View.VISIBLE) {
            guru_laporan_siswa = findViewById(R.id.guru_laporan_siswa);
            guru_kelas = findViewById(R.id.guru_kelas);
        } else if (include_admin.getVisibility() == View.VISIBLE) {

            admin_tambah_siswa = findViewById(R.id.admin_tambah_siswa);
            admin_edit_siswa = findViewById(R.id.admin_edit_siswa);
            admin_hapus_siswa = findViewById(R.id.admin_hapus_siswa);

            admin_tambah_guru = findViewById(R.id.admin_tambah_guru);
            admin_edit_guru = findViewById(R.id.admin_edit_guru);
            admin_hapus_guru = findViewById(R.id.admin_hapus_guru);


        }

        //end of menu logic

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("RestrictedApi")
    private void logic() {

        Toast.makeText(DashboardActivity.this, session.getNIK(), Toast.LENGTH_SHORT).show();
        //user name

        //end of user name

        dashboard_navigation_view.setNavigationItemSelectedListener(this);

        btn_absen.setOnClickListener(View -> {
            if (session.getWhoami().equals("siswa")) {
                DashboardActivity.runningTask.cancel(true);

                this.startActivity(new Intent(DashboardActivity.this, CameraActivity.class));
            } else if (session.getWhoami().equals("guru")) {
                this.startActivity(new Intent(DashboardActivity.this, CekDisini.class));
            }
        });


        if (include_siswa.getVisibility() == View.VISIBLE) {
            siswa_kelas.setOnClickListener(View -> {
                DashboardActivity.this.startActivity(new Intent(DashboardActivity.this, KelasActivity.class));
            });
            siswa_laporan_saya.setOnClickListener(View -> {
                Intent intent = new Intent(DashboardActivity.this, LaporanSaya.class);
                startActivity(intent);
            });
        } else if (include_guru.getVisibility() == View.VISIBLE) {
            guru_kelas.setOnClickListener(View -> {
                startActivity(new Intent(DashboardActivity.this, KelasActivity.class));
            });
            guru_laporan_siswa.setOnClickListener(View -> {

            });

        } else if (include_admin.getVisibility() == View.VISIBLE) {
            admin_tambah_siswa.setOnClickListener(View -> {
                Intent intent = new Intent(DashboardActivity.this, TambahUser.class);
                intent.putExtra("user", "siswa");
                startActivity(intent);
            });
            admin_tambah_guru.setOnClickListener(View -> {
                Intent intent = new Intent(DashboardActivity.this, TambahUser.class);
                intent.putExtra("user", "guru");
                startActivity(intent);
            });

            admin_edit_guru.setOnClickListener(View -> {
                Intent intent = new Intent(DashboardActivity.this, TampilUser.class);
                intent.putExtra("user", "guru");
                startActivity(intent);
            });
            admin_edit_siswa.setOnClickListener(View -> {
                Intent intent = new Intent(DashboardActivity.this, TampilUser.class);
                intent.putExtra("user", "siswa");
                startActivity(intent);
            });



        }


    }

    ModelSiswa siswa = new ModelSiswa();
    ModelGuru guru = new ModelGuru();
    ModelAdmin admin = new ModelAdmin();

    private void setData() {
        TextView text_nama = getSupportActionBar().getCustomView().findViewById(R.id.text_nama);
        text_nama.setText(session.getWhoami().equals("siswa") ? siswa.getNama() : (session.getWhoami().equals("guru") ? guru.getNama() : (session.getWhoami().equals("admin") ? admin.getNama() : null)));
    }


    private void userInformation(String result) throws JSONException {

        if (result.isEmpty()) {
            Toast.makeText(this, "no_data", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("[data] : " + result);
            JSONObject object = new JSONObject(result);
            String name = session.getWhoami().equals("siswa") ? "siswa" : (session.getWhoami().equals("guru") ? "guru" : (session.getWhoami().equals("admin") ? "admin" : null));
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
                        } else if (session.getWhoami().equals("admin")) {
                            admin.setNIK(obj.getString("NIK"));
                            admin.setNama(obj.getString("nama"));
                            admin.setTanggal_lahir(obj.getString("tanggal_lahir"));
                            admin.setTempat_lahir(obj.getString("tempat_lahir"));
                            admin.setAlamat(obj.getString("alamat"));
                            admin.setJenis_kelamin(obj.getString("jenis_kelamin"));
                            admin.setAgama(obj.getString("agama"));
                            admin.setFoto(obj.getString("foto"));

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
            protected String doInBackground(Void... voids) {
                String res = "";


//                try {
//
//                    res = userAction.userProfile("?request=userProfile&user=" + session.getWhoami() + param);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                String param = session.getWhoami().equals("siswa") ? "&NIS=" + session.getNIS() : (session.getWhoami().equals("guru") ? "&NIK=" + session.getNIK() : (session.getWhoami().equals("admin") ? "&NIK=" + session.getNIK() : null));
                StringRequest request = new StringRequest(Request.Method.GET, userAction.api + "?request=userProfile&user=" + session.getWhoami() + param, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            userInformation(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Didn't work : " + error.getMessage());
                    }
                });

                RequestQueue queue = Volley.newRequestQueue(DashboardActivity.this);
                queue.add(request);

                return res;
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);

        if (dashboard_drawer.isDrawerOpen(GravityCompat.START)) {
            dashboard_drawer.closeDrawer(GravityCompat.START);
        } else {
            builder.setMessage("Apakah kamu yakin ingin keluar?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DashboardActivity.super.onBackPressed();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user:
                Intent intent = new Intent(DashboardActivity.this, UserProfile.class);
                intent.putExtra("view_as", "me");
                startActivity(intent);
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