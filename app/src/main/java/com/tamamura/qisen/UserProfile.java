package com.tamamura.qisen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Client.UserAction;
import UserSession.Session;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    ImageButton btn_back_profile;
    Session session;
    UserAction userAction;


    //Some widget


    TextView text_identifier, text_class, user_identifier, user_nama, user_tanggal_lahir, user_tempat_lahir, user_alamat, user_jenis_kelamin, user_kelas, user_agama;
    CircleImageView user_img;

    //End-of Some widget


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        classInit();
        viewInit();
        logic();
    }

    private void classInit() {

        session = new Session(this);
        userAction = new UserAction(this);

    }

    private void viewInit() {
        btn_back_profile = findViewById(R.id.btn_back_profile);
        text_identifier = findViewById(R.id.text_identifier);
        text_class = findViewById(R.id.text_class);

        user_nama = findViewById(R.id.user_nama);
        user_tanggal_lahir = findViewById(R.id.user_tanggal_lahir);
        user_tempat_lahir = findViewById(R.id.user_tempat_lahir);
        user_alamat = findViewById(R.id.user_alamat);
        user_jenis_kelamin = findViewById(R.id.user_jenis_kelamin);
        user_kelas = findViewById(R.id.user_kelas);
        user_agama = findViewById(R.id.user_agama);
        user_img = findViewById(R.id.user_img);
        user_identifier = findViewById(R.id.user_identifier);

    }

    private void logic() {

        if (session.getWhoami().equals("siswa")) {
            text_identifier.setText("NIS");
            text_class.setText("Kelas");
        } else if (session.getWhoami().equals("guru")) {
            text_class.setText("Walikelas");
            text_identifier.setText("NIK");
        } else if (session.getWhoami().equals("admin")) {
            user_kelas.setVisibility(View.GONE);
            text_class.setVisibility(View.GONE);
            text_identifier.setText("NIK");
        }


        btn_back_profile.setOnClickListener(View -> {
            UserProfile.this.finish();
        });

        userProfile();
    }


    private void userProfileParse(String result) throws JSONException {
        System.out.println("userProfile: " + result);
        if (result.isEmpty()) {
            Toast.makeText(UserProfile.this, "NO_DATA", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = null;
            if (getIntent().getStringExtra("view_as").equals("another")) {
                jsonArray = jsonObject.getJSONArray(getIntent().getStringExtra("user"));
            } else {
                jsonArray = jsonObject.getJSONArray(session.getWhoami());
            }
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    if (object.getBoolean("res")) {

                        user_nama.setText(object.getString("nama"));
                        user_tanggal_lahir.setText(object.getString("tanggal_lahir"));
                        user_tempat_lahir.setText(object.getString("tempat_lahir"));
                        user_alamat.setText(object.getString("alamat"));
                        user_jenis_kelamin.setText(object.getString("jenis_kelamin"));
                        user_agama.setText(object.getString("agama"));
                        if(getIntent().getStringExtra("view_as").equals("me"))
                        {
                            if (session.getWhoami().equals("siswa")) {
                                user_identifier.setText(object.getString("NIS"));
                                user_kelas.setText(object.getString("kelas"));
                            } else if (session.getWhoami().equals("guru")) {
                                user_identifier.setText(object.getString("NIK"));
                                user_kelas.setText(object.getString("walikelas"));
                            }
                        }else if(getIntent().getStringExtra("view_as").equals("another"))
                        {
                            if(getIntent().getStringExtra("user").equals("siswa"))
                            {
                                user_identifier.setText(object.getString("NIS"));
                                user_kelas.setText(object.getString("kelas"));
                            }else if(getIntent().getStringExtra("user").equals("guru"))
                            {
                                user_identifier.setText(object.getString("NIK"));
                                user_kelas.setText(object.getString("kelas"));
                            }
                        }

                        Picasso.get().load(userAction.img_api + object.getString("foto")).placeholder(R.drawable.ic_img_black_24).memoryPolicy(MemoryPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_STORE).into(user_img);
                    } else {
                        Toast.makeText(UserProfile.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(UserProfile.this, "No-data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private HashMap<String, String> getIdentifier() {
        String intent = getIntent().getStringExtra("view_as");
        HashMap<String, String> map = new HashMap<>();


        if (intent.equals("me")) {
            if (session.getWhoami().equals("siswa")) {
                map.put("user", "siswa");
                map.put("identifier", session.getNIS());
            } else if (session.getWhoami().equals("guru")) {
                map.put("user", "guru");
                map.put("identifier", session.getNIK());
            } else if (session.getWhoami().equals("admin")) {
                map.put("user", "admin");
                map.put("identifier", session.getNIK());
            }
        } else if (intent.equals("another")) {
            map.put("user", getIntent().getStringExtra("user"));
            map.put("identifier", getIntent().getStringExtra("identifier"));
        }

        return map;
    }

    @SuppressLint("StaticFieldLeak")
    private void userProfile() {

        String param = "&user=" + getIdentifier().get("user");
        String params = getIdentifier().get("user").equals("siswa") ? "&NIS=" : (getIdentifier().get("user").equals("guru") ? "&NIK=" : (getIdentifier().get("user").equals("admin") ? "&NIK=" : null));
        System.out.println(param + params + getIdentifier().get("identifier"));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, userAction.api + "?request=userProfile" + param + params + getIdentifier().get("identifier"), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    userProfileParse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfile.this, "VolleyError : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);


    }
}