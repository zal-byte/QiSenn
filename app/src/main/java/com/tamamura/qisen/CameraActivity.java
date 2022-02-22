package com.tamamura.qisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import Client.UserAction;
import MetaData.ImageMetaData;
import UserSession.Session;
import handler.appHandler;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    ImageButton camera_back_button;
    PreviewView previewView;
    MaterialButton btn_take_picture, btn_back_camera;
    private ImageMetaData imd;
    public static final int MEDIA_TYPE_IMAGE = 1;
    appHandler handler;
    UserAction userAction;
    Session session;


    CardView dangerous_hint, camera_card_view_1, camera_card_view_2;
    ImageView img_absen_response;
    TextView txt_absen_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        classInit();
        viewInit();
        logic();
    }

    private void classInit() {

        session = new Session(this);
        handler = new appHandler(this);

    }

    private void viewInit() {
        previewView = findViewById(R.id.camera_preview);
        camera_back_button = findViewById(R.id.camera_back_button);
        btn_take_picture = findViewById(R.id.btn_take_picture);

        dangerous_hint = findViewById(R.id.dangerous_hint);
        camera_card_view_1 = findViewById(R.id.camera_card_view_1);
        btn_back_camera = findViewById(R.id.btn_back_camera);
        camera_card_view_2 = findViewById(R.id.camera_card_view_2);

        img_absen_response = findViewById(R.id.img_absen_response);
        txt_absen_response = findViewById(R.id.txt_absen_response);
    }

    private void logic() {
        camera_back_button.setOnClickListener(View -> {
            CameraActivity.this.finish();
        });


//        btn_take_picture.setOnClickListener(View -> {
//            File output = this.getCacheDir();
//            File mImageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QiSen");
//
//            if (!mImageDir.exists()) {
//                mImageDir.mkdirs();
//            }
//
//            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/QiSen", "temp.jpg");
//
//
//
//        });

        btn_back_camera.setOnClickListener(View -> {
            CameraActivity.this.finish();
        });

        startCameras();
    }

    private void parse(String result) throws JSONException {

        if (result != null) {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("Absens");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.getBoolean("res")) {
                        String pesan = object.getString("msg");
                        if (pesan.toLowerCase().equals("absen berhasil ditambahkan.")) {
                            dangerous_hint.setVisibility(View.GONE);
                            camera_card_view_1.setVisibility(View.GONE);
                            camera_card_view_2.setVisibility(View.VISIBLE);

                            btn_take_picture.setVisibility(View.GONE);
                            btn_back_camera.setVisibility(View.VISIBLE);
                            img_absen_response.setImageDrawable(getResources().getDrawable(R.drawable.berhasil_absen));
                            txt_absen_response.setText("Kamu berhasil absen hari ini");
                        }
                    } else {
                        if (object.getString("msg").toLowerCase().equals("kamu telat")) {
                            dangerous_hint.setVisibility(View.GONE);
                            camera_card_view_1.setVisibility(View.GONE);
                            camera_card_view_2.setVisibility(View.VISIBLE);

                            btn_take_picture.setVisibility(View.GONE);
                            btn_back_camera.setVisibility(View.VISIBLE);
                            img_absen_response.setImageDrawable(getResources().getDrawable(R.drawable.gagal_absen));
                            txt_absen_response.setText("Kamu telat !");
                        }else if(object.getString("msg").toLowerCase().equals("kamu sudah absen"))
                        {
                            dangerous_hint.setVisibility(View.GONE);
                            camera_card_view_1.setVisibility(View.GONE);
                            camera_card_view_2.setVisibility(View.VISIBLE);

                            btn_take_picture.setVisibility(View.GONE);
                            btn_back_camera.setVisibility(View.VISIBLE);
                            img_absen_response.setImageDrawable(getResources().getDrawable(R.drawable.berhasil_absen));
                            txt_absen_response.setText("Kamu sudah absen");
                        }
                    }
                }
            } else {
                Toast.makeText(CameraActivity.this, "no_data_", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CameraActivity.this, "null", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    void o() {
        final UserAction userA = new UserAction(CameraActivity.this);
        new AsyncTask<Void, Void, String>() {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CameraActivity.this, "Mengirim", "Mengirim data absen", false, false);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                System.out.println("Dataaa: " + result);
                loading.dismiss();
                try {
                    parse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                String res = "";

                try {
                    if (map == null) {
                        System.out.println("Map is null");
                    } else {
                        System.out.println("Map is not null");
                    }
                    res = userA.siswaAbsen(map);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return res;
            }
        }.execute();
    }

    //Camera instance
    private Executor executor = Executors.newSingleThreadExecutor();
    HashMap<String, String> map;

    public void startCameras() {

        ListenableFuture cameraProviderFuture = ProcessCameraProvider.getInstance(this);


        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {


                try {
                    ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();

                    bindView(processCameraProvider);


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, ContextCompat.getMainExecutor(this));


        File file = new File(this.getCacheDir() + "_" + System.currentTimeMillis() + ".jpg");


        btn_take_picture.setOnClickListener(View -> {
            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();

            map = new HashMap<>();
            map.put("request", "addAbsen");
            map.put("NIS", session.getNIS());
            map.put("kelas", session.getKelas());

            System.out.println("Kelas: " + session.getKelas());

            imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    try {


                        CameraActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CameraActivity.this, "Sedang mengambil gambar", Toast.LENGTH_SHORT).show();
                            }
                        });
                        imd = new ImageMetaData(file);


                        map.put("img_date", imd.getImageDate());
                        map.put("img_time", imd.getImageTime());


                        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(file));

                        map.put("imageData", handler.imgToBase64(bitmap));


                        CameraActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                o();
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@NonNull ImageCaptureException exception) {
                    exception.printStackTrace();
                }
            });

        });
    }

    ImageCapture imageCapture;

    private void bindView(ProcessCameraProvider provider) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        imageCapture = builder.setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        androidx.camera.core.Camera camera = provider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);


    }


}