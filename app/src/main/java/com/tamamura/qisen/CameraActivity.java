package com.tamamura.qisen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraFilter;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.CameraView;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import MetaData.ImageMetaData;
import handler.CameraPreview;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview cPreview;
    ImageButton camera_back_button;
    PreviewView previewView;
    MaterialButton btn_take_picture;
    SwitchMaterial change_camera;
    private ImageMetaData imd;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        classInit();
        viewInit();
        logic();
    }

    private void classInit() {


//        cPreview = new CameraPreview(this, mCamera);

    }

    private void viewInit() {
        previewView = findViewById(R.id.camera_preview);
        camera_back_button = findViewById(R.id.camera_back_button);
        btn_take_picture = findViewById(R.id.btn_take_picture);
        change_camera = findViewById(R.id.change_camera);

    }

    private void logic() {
//        mCamera = getCameraInstance();
        camera_back_button.setOnClickListener(View -> {
            CameraActivity.this.finish();
        });

//        camera_preview.addView(cPreview);

        final Camera.PictureCallback pCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {

                Toast.makeText(CameraActivity.this, "Foto sedang diambil", Toast.LENGTH_SHORT).show();
//                try {
//                    meta_process(bytes);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        };

        change_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {

                }else{

                }
            }
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
        startCameras();
    }

    private void meta_process(byte[] data) throws Exception {
        File outputdir = this.getCacheDir();
        File outputfile = File.createTempFile("absen",".jpg", outputdir);

        FileOutputStream fos = new FileOutputStream(outputfile);
        fos.write(data);
        fos.close();

        imd = new ImageMetaData(outputfile);
        Toast.makeText(CameraActivity.this, imd.getImageDate(), Toast.LENGTH_SHORT).show();

    }







    private Executor executor = Executors.newSingleThreadExecutor();

    public void startCameras(){
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
    }

    private void bindView(ProcessCameraProvider provider)
    {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        if(hdrImageCaptureExtender.isExtensionAvailable(cameraSelector))
        {
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = builder.setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        androidx.camera.core.Camera camera = provider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);



        File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");


        btn_take_picture.setOnClickListener( View -> {
            ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();



            imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                @Override
                public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                    try{
                        imd = new ImageMetaData(file);
                        System.out.println("Date : " + imd.getImageDate() + "\nTime : " + imd.getImageTime());
                    }catch (Exception e)
                    {
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


}