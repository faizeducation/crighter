package com.crighter.Services;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.crighter.Preferences.SPref;
import com.crighter.R;

import java.io.File;

/**
 * Created by gold on 8/11/2018.
 */

public class ImageCaptureService extends HiddenCameraService {

    int takePictureCounter = 5;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .build();

                startCamera(cameraConfig);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAg", "capture images");
                        takePicture();
                    }
                }, 1000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        Log.e("TAG", "the image file path is 1 " + imageFile);

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_IMAGS, 0);
        SPref.storingIMAGEFilePath(sharedPreferences, imageFile.toString());
        takePicture();
        if (takePictureCounter==5){
            SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH5, imageFile.toString());
        }
       /* if (takePictureCounter==4){
            SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH4, imageFile.toString());
        }
        if (takePictureCounter==3){
            SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH3, imageFile.toString());
        }
        if (takePictureCounter==2){
            SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH2, imageFile.toString());
        }
        if (takePictureCounter==1){
            SPref.storingIMAGEFilePathWithKey(sharedPreferences, SPref.IMAGE_PATH, imageFile.toString()    );
        }*/
        if (takePictureCounter == 0){
            stopCamera();
            // Do something with the image...
            Intent service = new Intent(getApplicationContext(), ImageCaptureService2.class);
            //startService(service);
            stopSelf();
        }

        takePictureCounter = takePictureCounter-1;
    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Log.e("TAg", "the error is " + R.string.error_cannot_open);
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Log.e("TAg", "the error is " + R.string.error_cannot_write);
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Log.e("TAg", "the error is " + R.string.error_cannot_get_permission);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Log.e("TAg", "the error is " + R.string.error_not_having_camera);
                break;
        }

        stopSelf();
    }
}