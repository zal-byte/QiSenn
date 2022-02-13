package handler;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    Camera mCamera;
    SurfaceHolder mHolder;



    public CameraPreview(Activity activity, Camera mCamera)
    {
        super(activity);
        this.mCamera = mCamera;
        this.mHolder = getHolder();

        this.mHolder.addCallback(this);
        this.mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //this.mCamera.setDisplayOrientation(90);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        try {
            if( mCamera != null ){
                mCamera.setPreviewDisplay(this.mHolder);
                mCamera.startPreview();
            }else{
                System.out.println("no_camera");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(this.mHolder.getSurface() == null)
        {
            return;
        }

        try {
            if( mCamera != null ){
                mCamera.stopPreview();

                mCamera.setPreviewDisplay(this.mHolder);
                mCamera.startPreview();

            }else{
                System.out.println("no_camera");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }
}
