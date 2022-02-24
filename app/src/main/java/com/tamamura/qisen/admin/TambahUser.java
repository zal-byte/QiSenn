package com.tamamura.qisen.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.tamamura.qisen.R;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Client.UserAction;
import de.hdodenhof.circleimageview.CircleImageView;
import handler.appHandler;

public class TambahUser extends AppCompatActivity {

    ImageButton btn_kembali_tambahsiswa;
    TextView txt_user;

    MaterialButton btn_save;


    TextInputEditText identifier;
    TextInputEditText nama, tanggal_lahir, tempat_lahir, alamat, jenis_kelamin, agama, kelas, kata_sandi;

    CircleImageView user_img;


    UserAction userAction;
    appHandler handler;
    HashMap<String, String> param = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_user);


        userAction = new UserAction(this);
        handler = new appHandler(this);


        btn_kembali_tambahsiswa = findViewById(R.id.btn_kembali_tambahsiswa);
        txt_user = findViewById(R.id.txt_user);
        identifier = findViewById(R.id.identifier);

        btn_save = findViewById(R.id.btn_save);


        nama = findViewById(R.id.nama);
        tanggal_lahir = findViewById(R.id.tanggal_lahir);
        tempat_lahir = findViewById(R.id.tempat_lahir);
        alamat = findViewById(R.id.alamat);
        jenis_kelamin = findViewById(R.id.jenis_kelamin);
        agama = findViewById(R.id.agama);
        kelas = findViewById(R.id.kelas);
        kata_sandi = findViewById(R.id.kata_sandi);

        user_img = findViewById(R.id.user_img);

        //lgic

        btn_kembali_tambahsiswa.setOnClickListener(View -> {
            TambahUser.this.finish();
        });

        user_img.setOnClickListener(View -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        String intent = getIntent().getStringExtra("user");
        if (intent.equals("siswa")) {
            param.put("request", "addSiswa");
            identifier.setHint("NIS");
            kelas.setHint("Kelas");
        } else if (intent.equals("guru")) {
            param.put("request", "addGuru");
            identifier.setHint("NIK");
            kelas.setHint("Walikelas");
        }


        btn_save.setOnClickListener(View -> {
            if (param.get("imageData") == null) {
                Toast.makeText(this, "Masukan gambar dengan benar", Toast.LENGTH_SHORT).show();
            } else {
                if (get(nama).length() <= 0) {
                    Toast.makeText(this, "Masukan nama dengan benar", Toast.LENGTH_SHORT).show();
                } else {
                    if (get(tanggal_lahir).length() <= 0) {
                        Toast.makeText(this, "Masukan tanggal lahir dengan benar", Toast.LENGTH_SHORT).show();
                    } else {
                        if (get(tempat_lahir).length() <= 0) {
                            Toast.makeText(this, "Masukan tempat lahir dengan benar", Toast.LENGTH_SHORT).show();
                        } else {
                            if (get(alamat).length() <= 0) {
                                Toast.makeText(this, "Masukan alamat dengan benar", Toast.LENGTH_SHORT).show();
                            } else {
                                if (get(jenis_kelamin).length() <= 0) {
                                    Toast.makeText(this, "Masukan jenis kelamin dengan benar", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (get(agama).length() <= 0) {
                                        Toast.makeText(this, "Masukan agama dengan benar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (get(kelas).length() <= 0) {
                                            Toast.makeText(this, "Masukan kelas dengan benar", Toast.LENGTH_SHORT).show();
                                        } else {

                                            if( get(kata_sandi).length() <= 0)
                                            {
                                                Toast.makeText(this, "Masukan kata sandi dengan benar", Toast.LENGTH_SHORT).show();
                                            }else
                                            {

                                                if(getIntent().getStringExtra("user").equals("siswa"))
                                                {
                                                    param.put("NIS", get(identifier));
                                                }else if(getIntent().getStringExtra("user").equals("guru"))
                                                {
                                                    param.put("NIK", get(identifier));
                                                }

                                                param.put("kelas", get(kelas));
                                                param.put("nama", get(nama));
                                                param.put("tanggal_lahir", get(tanggal_lahir));
                                                param.put("tempat_lahir", get(tempat_lahir));
                                                param.put("alamat", get(alamat));
                                                param.put("jenis_kelamin", get(jenis_kelamin));
                                                param.put("agama", get(agama));
                                                param.put("password", get(kata_sandi));
                                                send();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                user_img.setImageBitmap(bitmap);

                param.put("imageData", handler.imgToBase64(bitmap));


                System.out.println("DATA : " +param.get("imageData"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void send() {



        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, userAction.api, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("TambahUser: " + response );

                try {
                    parseResponse( response );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TambahUser.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected HashMap<String, String> getParams() {
                return param;
            }
        };

        queue.add(sr);
    }
    private void parseResponse( String response ) throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahUser.this);

        if( response.isEmpty())
        {
            builder.setMessage("Tidak ada respon dari server");
        }else
        {
            JSONObject jsonObject = new JSONObject(response);
            String name = "";
            if( getIntent().getStringExtra("user").equals("siswa"))
            {
                name = "addSiswa";
            }else if(getIntent().getStringExtra("user").equals("guru"))
            {
                name = "addGuru";
            }
            JSONArray jsonArray = jsonObject.getJSONArray(name);

            if( jsonArray.length() > 0 )
            {
                for(int i = 0; i < jsonArray.length();i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if( object.getBoolean("res") != false )
                    {
                        if(object.getString("msg").toLowerCase().equals("siswa_berhasil_ditambahkan")){
                            builder.setMessage("Data siswa berhasil ditambahkan");
                            clearField();
                        }else if(object.getString("msg").toLowerCase().equals("guru_berhasil_ditambahkan"))
                        {
                            builder.setMessage("Data guru berhasil ditambahkan");
                            clearField();
                        }
                    }else
                    {
                        if( object.getString("msg").toLowerCase().equals("siswa_sudah_ada"))
                        {
                            builder.setMessage("Siswa dengan NIS ( " + identifier.getText().toString() + " ) Sudah ada");
                        }else if (object.getString("msg").toLowerCase().equals("gagal_mengupload_gambar"))
                        {
                            builder.setMessage("Gagal mengupload gambar pengguna");
                        }else if( object.getString("msg").toLowerCase().equals("eksekusi_gagal"))
                        {
                            builder.setMessage("Gagal mengeksekusi data");
                        }else if( object.getString("msg").toLowerCase().equals("guru_sudah_ada"))
                        {
                            builder.setMessage("Guru dengan NIK ( " + identifier.getText().toString() + " ) Sudah ada");
                        }
                    }
                }
            }else
            {
                builder.setMessage("Tidak ada data");
            }
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void clearField()
    {
        param.clear();
        user_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_img_black_24));
        identifier.setText("");
        nama.setText("");
        tanggal_lahir.setText("");
        tempat_lahir.setText("");
        alamat.setText("");
        jenis_kelamin.setText("");
        agama.setText("");
        kelas.setText("");
        kata_sandi.setText("");
    }


    private String get(TextInputEditText widget) {
        return widget.getText().toString();
    }

}