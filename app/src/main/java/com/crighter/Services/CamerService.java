package com.crighter.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.StrictMode;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.crighter.Preferences.SPref;

public class CamerService extends Service {

    private SurfaceHolder sHolder;
    private Camera mCamera;
    private Parameters parameters;
    int takePictureCounter = 5;

    public CamerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d("CAM", "start");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);};
        Thread myThread = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Camera.getNumberOfCameras() >= 2) {

            mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK); }

        if (Camera.getNumberOfCameras() < 2) {

            mCamera = Camera.open(); }
        SurfaceView sv = new SurfaceView(getApplicationContext());


        try {
            mCamera.setPreviewDisplay(sv.getHolder());
            parameters = mCamera.getParameters();
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            mCamera.takePicture(null, null, mCall);
        } catch (IOException e) { e.printStackTrace(); }

        sHolder = sv.getHolder();
       // sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return super.onStartCommand(intent, flags, startId);
    }


    Camera.PictureCallback mCall = new Camera.PictureCallback()
    {

        public void onPictureTaken(final byte[] data, Camera camera)
        {
            FileOutputStream outStream = null;
            try{

                String sd = getStorage(getApplicationContext());
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String tar = (sdf.format(cal.getTime()));
                outStream = new FileOutputStream(sd + tar + takePictureCounter + ".jpg");
                outStream.write(data);
                outStream.close();
                Log.e("CAM", data.length + " byte written to:" + sd + tar + takePictureCounter+".jpg");
                String imagePath =  sd+tar+takePictureCounter+".jpg";
                SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_IMAGS, 0);
                if (takePictureCounter==5){
                    SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH5, imagePath);
                }
                if (takePictureCounter==4){
                    SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH4, imagePath);
                }
                if (takePictureCounter==3){
                    SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH3, imagePath);
                }
                if (takePictureCounter==2){
                    SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH2, imagePath);
                }
                if (takePictureCounter==1){
                    SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH, imagePath);
                }

                if (takePictureCounter>0) {
                    mCamera.takePicture(null, null, mCall);
                    takePictureCounter = takePictureCounter-1;
                }
                else {
                    camkapa(sHolder);

                }


            } catch (FileNotFoundException e){
                Log.d("CAM", e.getMessage());
            } catch (IOException e){
                Log.d("CAM", e.getMessage());
            }}
    };


    public void camkapa(SurfaceHolder sHolder) {

        if (null == mCamera)
            return;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        stopSelf();
        Log.i("CAM", " closed");
    }

    private String getStorage(Context context) {
        String fileName = "";
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            fileName = context.getExternalCacheDir().getAbsolutePath() + "/image";
        } else {
            fileName = context.getFilesDir() + "/image";
        }
        File dir = new File(fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return fileName;
    }
}