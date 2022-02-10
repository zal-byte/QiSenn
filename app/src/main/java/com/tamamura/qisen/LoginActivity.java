package com.tamamura.qisen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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


    appHandler handler;


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
    }

    private void parseResult(String result) throws Exception {

        if( result.isEmpty() )
        {
            Toast.makeText(this, "no_data", Toast.LENGTH_SHORT).show();
        }else
        {
            JSONObject jsonObject = new JSONObject(result);
            String param = session.getWhoami().equals("siswa") ? "siswaLogin" : (session.getWhoami().equals("guru") ? "guruLogin" : null);
            JSONArray jsonArray = jsonObject.getJSONArray(param);

            if( jsonArray.length() > 0 )
            {
                for(int i = 0 ; i < jsonArray.length(); i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if(object.getBoolean("res") == true)
                    {
                        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                        //true
                        session.setLogin(true);
                        if(session.getWhoami().equals("siswa") )
                        {
                            session.setNIS(object.getString("nis"));
                        }else if(session.getWhoami().equals("guru"))
                        {
                            session.setNIK(object.getString("nik"));
                        }

                        LoginActivity.this.finish();
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));

                    }else{
                        System.out.println("Login[error]");
                        Toast.makeText(this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private void logic() {
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