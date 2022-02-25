package com.tamamura.qisen.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.tamamura.qisen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Client.UserAction;
import UserSession.Session;
import de.hdodenhof.circleimageview.CircleImageView;
import handler.appHandler;

public class UbahUser extends AppCompatActivity {


    UserAction userAction;
    Session session;

    appHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_user);
        init();
        classInit();
        logic();
    }


    CircleImageView user_img;
    TextInputEditText identifier, nama, tanggal_lahir, tempat_lahir, alamat, jenis_kelamin, agama, kelas, kata_sandi;

    MaterialButton btn_save;

    private String getIdentifier() {
        return getIntent().getStringExtra("identifier");
    }

    private String who() {
        return getIntent().getStringExtra("user");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                user_img.setImageBitmap(bitmap);

                param.put("imageData", handler.imgToBase64(bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void logic() {

        user_img.setOnClickListener(View -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        btn_save.setOnClickListener(View -> {

            if (who().equals("siswa")) {
                param.put("request", "editSiswa");
                param.put("NIS", get(identifier));
                param.put("kelas", get(kelas));
            } else if (who().equals("guru")) {
                param.put("request", "editGuru");
                param.put("walikelas", get(kelas));
                param.put("NIK", get(identifier));
            }

            param.put("nama", get(nama));
            param.put("tanggal_lahir", get(tanggal_lahir));
            param.put("tempat_lahir", get(tempat_lahir));
            param.put("alamat", get(alamat));
            param.put("jenis_kelamin", get(jenis_kelamin));
            param.put("agama", get(agama));
            param.put("password", get(kata_sandi));


            send();


        });
        fetchUserInfo();
    }

    String get(TextInputEditText name) {
        return name.getText().toString();
    }

    void send() {

        StringRequest sr = new StringRequest(Request.Method.POST, userAction.api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    Toast.makeText(UbahUser.this, "Tidak ada respon dari server", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String name = "";
                        if (who().equals("siswa")) {
                            name = "editSiswa";
                        } else if (who().equals("guru")) {
                            name = "editGuru";
                        }
                        JSONArray jsonArray = jsonObject.getJSONArray(name);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (object.getBoolean("res") != false) {
                                    Toast.makeText(UbahUser.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UbahUser.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(UbahUser.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return param;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);


    }

    void classInit() {
        this.userAction = new UserAction(this);
        this.session = new Session(this);
        this.handler = new appHandler(this);
    }

    HashMap<String, String> param = new HashMap<String, String>();

    void fetchUserInfo() {


        String param = "";
        if (who().equals("siswa")) {
            param = "?request=userProfile&user=siswa&NIS=" + getIdentifier();
        } else if (who().equals("guru")) {
            param = "?request=userProfile&user=guru&NIK=" + getIdentifier();
        }

        StringRequest sr = new StringRequest(Request.Method.GET, userAction.api + param, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("WADUH: " + response);
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

    void parse(String result) throws JSONException {
        if (result.isEmpty()) {
            Toast.makeText(this, "Tidak ada respon dari server", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject(result);
            String name = "";
            if (who().equals("siswa")) {
                name = "siswa";
            } else if (who().equals("guru")) {
                name = "guru";
            }
            JSONArray jsonArray = jsonObject.getJSONArray(name);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("res") != false) {
                        if (who().equals("siswa")) {
                            identifier.setText(object.getString("NIS"));
                            kelas.setText(object.getString("kelas"));
                        } else if (who().equals("guru")) {
                            kelas.setText(object.getString("walikelas"));
                            identifier.setText(object.getString("NIK"));
                        }
                        nama.setText(object.getString("nama"));
                        tanggal_lahir.setText(object.getString("tanggal_lahir"));
                        tempat_lahir.setText(object.getString("tempat_lahir"));
                        alamat.setText(object.getString("alamat"));
                        agama.setText(object.getString("agama"));
                        jenis_kelamin.setText(object.getString("jenis_kelamin"));
                        kata_sandi.setText(object.getString("kata_sandi"));

                        Picasso.get().load(userAction.img_api + object.getString("foto")).into(user_img);
                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView(), object.getString("msg"), Snackbar.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void init() {
        user_img = findViewById(R.id.user_img);

        identifier = findViewById(R.id.identifier);
        nama = findViewById(R.id.nama);
        tanggal_lahir = findViewById(R.id.tanggal_lahir);
        tempat_lahir = findViewById(R.id.tempat_lahir);
        alamat = findViewById(R.id.alamat);
        jenis_kelamin = findViewById(R.id.jenis_kelamin);
        agama = findViewById(R.id.agama);
        kelas = findViewById(R.id.kelas);
        kata_sandi = findViewById(R.id.kata_sandi);

        btn_save = findViewById(R.id.btn_save);


    }


}