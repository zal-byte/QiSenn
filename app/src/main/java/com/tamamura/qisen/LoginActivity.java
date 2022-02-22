package com.tamamura.qisen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import Client.UserAction;
import UserSession.Session;
import handler.appHandler;

public class LoginActivity extends AppCompatActivity {

    UserAction userAction;
    TextInputEditText identifier, password;
    ImageButton btn_back;
    Session session;
    MaterialButton btn_login;

    TextView txt_preview_1;
    TextInputLayout text_input_layout_1, text_input_layout_2;

    appHandler handler;
    RelativeLayout login_relativelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        classInit();
        viewInit();
        logic();
    }

    private void classInit() {

        handler = new appHandler(this);

        session = new Session(this);
    }

    private void viewInit() {
        identifier = findViewById(R.id.identifier);
        password = findViewById(R.id.password);
        btn_back = findViewById(R.id.btn_back);
        userAction = new UserAction(this);

        btn_login = findViewById(R.id.btn_login);

        txt_preview_1 = findViewById(R.id.txt_preview_1);

        text_input_layout_1 = findViewById(R.id.text_input_layout_1);
        text_input_layout_2 = findViewById(R.id.text_input_layout_2);


        login_relativelayout = findViewById(R.id.login_relativelayout);

    }

    private void parseResult(String result) throws Exception {

        if (result == null) {
            //Tidak ada respon dari server
            Snackbar.make(LoginActivity.this.getWindow().getDecorView().getRootView(), "Tidak ada respon dari server", Snackbar.LENGTH_LONG).show();

        } else {

            JSONObject jsonObject = new JSONObject(result);
            String param = session.getWhoami().equals("siswa") ? "siswaLogin" : (session.getWhoami().equals("guru") ? "guruLogin" : (session.getWhoami().equals("admin") ? "adminLogin" : null));
            JSONArray jsonArray = jsonObject.getJSONArray(param);

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("res") == true) {
                        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                        //true
                        session.setLogin(true);

                        if (session.getWhoami().equals("siswa")) {
                            session.setNIS(object.getString("nis"));
                            session.setKelas(object.getString("kelas"));
                        } else if (session.getWhoami().equals("guru")) {
                            session.setKelas(object.getString("walikelas"));
                            session.setNIK(object.getString("nik"));
                        } else if (session.getWhoami().equals("admin")) {
                            session.setNIK(object.getString("nik"));
                        }

                        LoginActivity.this.finish();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));


                    } else {
                        if (object.getString("msg").toLowerCase().equals("no_data")) {
                            //Pengguna tidak ada atau tidak ditemukan
                            identifier.setError("Pengguna tidak ditemukan");
                        } else if (object.getString("msg").toLowerCase().equals("invalid password")) {
                            //Kata sandi salah
                            password.setError("Kata sandi salah");
                        } else if (object.getString("msg").toLowerCase().equals("something went wrong")) {
                            //Tidak bisa login
                            Snackbar.make(LoginActivity.this.getWindow().getDecorView().getRootView(), "Tidak bisa login", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void logic() {


        if (session.getWhoami().equals("siswa")) {
            txt_preview_1.setText("Masuk sebagai Siswa");
            text_input_layout_1.setHint("NIS");
            login_relativelayout.setBackground(getResources().getDrawable(R.drawable.bg_login_siswa));
        } else if (session.getWhoami().equals("guru")) {
            text_input_layout_1.setHint("NIK");
            txt_preview_1.setText("Masuk sebagai Guru");
            login_relativelayout.setBackground(getResources().getDrawable(R.drawable.bg_login_guru));
        } else if (session.getWhoami().equals("admin")) {
            text_input_layout_1.setHint("NIK");
            txt_preview_1.setText("Masuk sebagai Kurikulum");
            login_relativelayout.setBackground(getResources().getDrawable(R.drawable.bg_login_admin));
        }

        text_input_layout_2.setHint("Kata sandi");

        btn_back.setOnClickListener(View -> {
            LoginActivity.this.finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });

        btn_login.setOnClickListener(View -> {

            //Validate form data

            String iden = identifier.getText().toString();
            String passw = identifier.getText().toString();

            if (iden.isEmpty()) {
                String msg = session.getWhoami().equals("siswa") ? "Masukan NIS kamu dengan benar" : (session.getWhoami().equals("guru") ? "Masukan NIK kamu dengan benar" : null);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            } else {
                if (passw.isEmpty()) {

                    Toast.makeText(this, "Isi kata sandi dengan benar", Toast.LENGTH_SHORT).show();

                } else {

                    HashMap<String, String> param = new HashMap<>();
                    param.put("request", "userLogin");
                    param.put("user", session.getWhoami());
                    if (session.getWhoami().equals("siswa")) {
                        param.put("nis", identifier.getText().toString());
                    } else if (session.getWhoami().equals("guru")) {
                        param.put("nik", identifier.getText().toString());
                    } else if (session.getWhoami().equals("admin")) {
                        param.put("nik", identifier.getText().toString());
                    }
                    param.put("password", password.getText().toString());

                    try {
                        new AsyncTask<Void, Void, String>() {
                            ProgressDialog loading;

                            @Override
                            protected void onPostExecute(String result) {
                                super.onPostExecute(result);
                                loading.dismiss();
                                try {
                                    parseResult(result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                loading = ProgressDialog.show(LoginActivity.this, "Masuk", "Sedang masuk", false, false);
                            }

                            @Override
                            protected String doInBackground(Void... voids) {
                                String res = null;
                                try {
                                    res = userAction.userLogin(param);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return res;
                            }
                        }.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

        });


    }
}