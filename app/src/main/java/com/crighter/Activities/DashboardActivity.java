package com.crighter.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.crighter.BroadCast.ScreenActionReceiver;
import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.crighter.URLS.APIURLS;
import com.crighter.Utilities.Initializer;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

public class DashboardActivity extends BaseActvitvityForDrawer implements LocationListener, SurfaceHolder.Callback {
    private String TAG = getClass().getSimpleName();
    TextView tv_text_start;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Typeface custom_font;
    ImageView iv_menu_button,ivEmergency;
    RelativeLayout tv_start;
    RelativeLayout rl_how_it_work;


    ///new
    private MediaRecorder mediaRecorder = null;
    private String fileName = null;
    ScreenActionReceiver screenactionreceiver;

    private String provider_info = "";
    private Float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10F; // 10 meters


    // The minimum time between updates in milliseconds
    private long MIN_TIME_BW_UPDATES = (long) (1000 * 60);


    // flag for GPS Tracking is enabled
    boolean isGPSTrackingEnabled = false;

    Location location = null;
    double latitude = 0.0;
    double longitude = 0.0;

    //camera
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int imagesCount = 0;
    private String imagePath1 = "";
    private String imagePath2 = "";
    private String imagePath3 = "";
    private String imagePath4 = "";
    private String imagePath5 = "";
    boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_dashboard, null, false);
        mDrawerLayout.addView(contentView, 0);

        fetchCurrentLocation();
        init();
        startButton();
        openingMenu();
//        onTouchStartStop();
        onHowitWorkClickHandler();

    }

    private void init() {

        fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/Crighter";
        File dir = new File(fileName);
        Log.e(TAG, "init: " + dir.exists());
        if (!dir.exists()) {
            dir.mkdirs();


        }
        Log.e(TAG, "init3: " + dir.exists());

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/neuropol.ttf");

        tv_start = (RelativeLayout) findViewById(R.id.tv_start);
        tv_text_start = (TextView) findViewById(R.id.tv_text_start);
        iv_menu_button = (ImageView) findViewById(R.id.iv_menu_button);
        ivEmergency = (ImageView) findViewById(R.id.iv_emergency);
        tv_start = (RelativeLayout) findViewById(R.id.tv_start);
        rl_how_it_work = (RelativeLayout) findViewById(R.id.rl_how_it_work);


        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);


        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
        boolean isServiceRunning = SPref.getServiceStatus(sharedPreferences);

        if (isServiceRunning) {
            tv_text_start.setText("Stop");
        } else {
            tv_text_start.setText("Start");
        }

        tv_text_start.setTypeface(custom_font);

    }


    private void startButton() {

        ivEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,EmergencyActivity.class));
            }
        });

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_text_start.getText().toString().equals("Start")) {
//                    startService(new Intent(getApplicationContext(), LockService.class));
                    startEverything();
                    Log.e("TAg", "Service Started");
                    tv_text_start.setText("Stop");
                    SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
                    SPref.storingServiceStatus(sharedPreferences, true);

//                    finish();
                    Initializer.isSerivceRunning = true;
                } else if (tv_text_start.getText().toString().equals("Stop")) {

                    Initializer.isSerivceRunning = false;

//                    stopService(new Intent(getApplicationContext(), LockService.class));
                    stopEverything();
                    tv_text_start.setText("Start");
                    Log.e("TAg", "Service stop");

                    SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
                    SPref.storingServiceStatus(sharedPreferences, false);

                    SharedPreferences sp = getSharedPreferences("re", 0);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.putBoolean("regi", false);
                    e.commit();

                    //canceling alarm loop
//                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                    Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                            getApplicationContext(), 0, myIntent,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmManager.cancel(pendingIntent);
//                    if (isAlarmAlreadyRunning()){
//                        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
//                        Intent morningIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
//                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getApplicationContext(), 0, morningIntent, 0);
//                        alarmManager1.cancel(pendingIntent1);
//                    }

                }
            }
        });
    }


    private void startEverything() {

        startRecording(getStorage(this));
        final Handler handler = new Handler();
        final int delay = 2000; // 1000 milliseconds == 1 second
        handler.postDelayed(new Runnable() {
            public void run() {
                if (imagesCount < 6) {
                    File pictureFile = null;
                    if (imagesCount == 1) {
                        pictureFile = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Crighter",
                                "IMG_" + new Date().getTime() + ".jpg");
                        imagePath1 = pictureFile.getPath();
                    } else if (imagesCount == 2) {
                        pictureFile = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Crighter",
                                "IMG_" + new Date().getTime() + ".jpg");
                        imagePath2 = pictureFile.getPath();
                    } else if (imagesCount == 3) {
                        pictureFile = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Crighter",
                                "IMG_" + new Date().getTime() + ".jpg");
                        imagePath3 = pictureFile.getPath();
                    } else if (imagesCount == 4) {
                        pictureFile = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Crighter",
                                "IMG_" + new Date().getTime() + ".jpg");
                        imagePath4 = pictureFile.getPath();
                    } else {
                        pictureFile = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Crighter",
                                "IMG_" + new Date().getTime() + ".jpg");
                        imagePath5 = pictureFile.getPath();
                    }
                    captureImage(pictureFile);
                    imagesCount = imagesCount + 1;
                    Log.e(TAG, "run: images captured: " + imagesCount);
                }
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    private void stopEverything() {
        if (mediaRecorder != null) {
            if (isRecording) {
                mediaRecorder.stop();
            }
            mediaRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
            mediaRecorder.release(); // Now the object cannot be reused
            isRecording = false;
        }
        SharedPreferences sharedPreferencesUser = getSharedPreferences(SPref.PREF_USER_CRED, 0);

        String userId = SPref.getStringPref(sharedPreferencesUser, SPref.USER_ID);

        sendSMS();
        Toast.makeText(DashboardActivity.this, "Uploading Started ", Toast.LENGTH_SHORT).show();

        uploadData(userId, new File(fileName), new File(imagePath1), new File(imagePath2),
                new File(imagePath3), new File(imagePath4), new File(imagePath5), String.valueOf(latitude), String.valueOf(longitude));
//        uploadingDataToServer(this,userId,fileName,fileName,fileName,fileName,fileName,fileName,"0.0","0.0");
    }

    private void captureImage(File file) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();
                    Toast.makeText(DashboardActivity.this, "Images Captured: " + (imagesCount), Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    Log.e(TAG, "onPictureTaken: Exception" + e.getMessage());
                    Toast.makeText(DashboardActivity.this, "Not captured: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                camera.startPreview();
            }
        });
    }

    private void startRecording(String file) {

        if (mediaRecorder != null) {
            try {
                mediaRecorder.reset();
                mediaRecorder.release();
            } catch (IllegalStateException e) {

            }
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        } else {
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            mediaRecorder.setAudioEncodingBitRate(48000);
        } else {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setAudioEncodingBitRate(64000);
        }
        mediaRecorder.setAudioSamplingRate(16000);
        mediaRecorder.setOutputFile(file);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            Log.e("giftlist", "io problems while preparing [" +
                    file + "]: " + e.getMessage());
        }
    }

    private String getStorage(Context context) {

        fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Crighter";
        File dir = new File(fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        fileName = fileName + "/" + System.currentTimeMillis() + ".3gp";
        return fileName;
    }


    private void uploadData(String userID, File audioPath, File imagePath,
                            File imagePath2, File imagePath3, File imagePath4, File imagePath5, String lat, String lng) {
        AndroidNetworking.upload(APIURLS.BACKGROUND_DATA_URL)
                .addMultipartFile("img1", imagePath)
                .addMultipartFile("img2", imagePath2)
                .addMultipartFile("img3", imagePath3)
                .addMultipartFile("img4", imagePath4)
                .addMultipartFile("img5", imagePath5)
                .addMultipartFile("audio", audioPath)
                .addMultipartParameter("post_lat", lat) //Adding text parameter to the request
                .addMultipartParameter("post_lng", lng)
                .addMultipartParameter("user_id", userID)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        int progress = (int) (((float) bytesUploaded / totalBytes) * 100);
                        Toast.makeText(DashboardActivity.this, "Progress " + progress, Toast.LENGTH_SHORT).show();

                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: Success");
                        // do anything with response
                        if (response != null) {
                            try {
                                boolean error = response.getBoolean("error");
                                if (!error) {
                                    Toast.makeText(DashboardActivity.this, "Uploading Done ", Toast.LENGTH_SHORT).show();

                                    clearCache(imagePath);
                                    clearCache(imagePath2);
                                    clearCache(imagePath3);
                                    clearCache(imagePath4);
                                    clearCache(imagePath5);
                                    clearCache(audioPath);
                                } else {
                                    String errorMsg = response.getString("msg");
                                    Log.e(TAG, "onResponse: Error: " + errorMsg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, "onError: " + error.getErrorDetail());
                        Toast.makeText(DashboardActivity.this, "Error!" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendSMS() {
        SharedPreferences sharedPreferencesUser = getSharedPreferences(SPref.PREF_USER_CRED, 0);
        String phone = SPref.getStringPref(sharedPreferencesUser, SPref.AUTH_PHONE);
        String name = SPref.getStringPref(sharedPreferencesUser, SPref.USER_FULLNAME);
        String authName = SPref.getStringPref(sharedPreferencesUser, SPref.AUTH_FULLNAME);

//        String msg = " Your Related " + name + " Is in Danger, Track https://maps.google.com/maps?q=" + latitude + "," + longitude + "+(Danger+Location)&z=14&ll=" + latitude + "," + longitude;

        String userLocation = "https://www.google.com/maps/?q=" + latitude + "," + longitude;
        String msg = "Your Related " + name + " is in Danger, Track " + userLocation+" Check your email for more details, It may take a while";
        try {
            Log.e(TAG, "sendSMS: " + phone);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    private void uploadingDataToServer(Context context, String userID, String audioPath, String ImagePath,
                                       String ImagePath2, String ImagePath3, String ImagePath4, String ImagePath5, String lat, String lng) {
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            Log.e("TAG", "the preff data from audio path " + audioPath);
            Log.e("TAG", "the preff data from image path 1 " + ImagePath);
            Log.e("TAG", "the preff data from image path 2 " + ImagePath2);
            Log.e("TAG", "the preff data from image path 3 " + ImagePath3);
            Log.e("TAG", "the preff data from image path 4 " + ImagePath4);
            Log.e("TAG", "the preff data from image path 5 " + ImagePath5);


            //Creating a multi part request
            new MultipartUploadRequest(context, uploadId, APIURLS.BACKGROUND_DATA_URL)
                    .addFileToUpload(ImagePath, "img1") //Adding image file
                    .addFileToUpload(ImagePath2, "img2") //Adding image file
                    .addFileToUpload(ImagePath3, "img3") //Adding image file
                    .addFileToUpload(ImagePath4, "img4") //Adding image file
                    .addFileToUpload(ImagePath5, "img5") //Adding image file
                    .addFileToUpload(audioPath, "audio") //Adding audio file
                    .addParameter("post_lat", lat) //Adding text parameter to the request
                    .addParameter("post_lng", lng)
                    .addParameter("user_id", userID)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)

                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e(TAG, "onError: " + serverResponse.getBodyAsString());
                        }

                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            String responseFromServer = serverResponse.getBodyAsString();
                            Log.e(TAG, "the response from server for upload image: " + serverResponse.getBodyAsString());
                            if (responseFromServer != null) {
                                try {
                                    JSONObject jObj = new JSONObject(responseFromServer);
                                    boolean error = jObj.getBoolean("error");
                                    if (!error) {

//                                        clearCache(ImagePath);
//                                        clearCache(ImagePath2);
//                                        clearCache(ImagePath3);
//                                        clearCache(ImagePath4);
//                                        clearCache(ImagePath5);
//                                        clearCache(audioPath);
                                    } else {
                                        String errorMsg = jObj.getString("msg");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e(TAG, "onCancelled: " + uploadInfo.getUploadId());
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
        }
    }

    private void clearCache(File fdelete) {
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("TAG", "the file deleted " + fdelete.getPath());
            } else {
                Log.e("TAG", "error deleting path " + fdelete.getPath());
            }
        }
    }

    void fetchCurrentLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                isGPSTrackingEnabled = true;
                Log.d(TAG, "Application use GPS Service");

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */
                provider_info = LocationManager.GPS_PROVIDER;
            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                isGPSTrackingEnabled = true;
                Log.d(TAG, "Application use Network State to get GPS coordinates");

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
                provider_info = LocationManager.NETWORK_PROVIDER;
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            // Application can use GPS or Network Provider
            if (!provider_info.isEmpty()) {
                locationManager.requestLocationUpdates(
                        provider_info,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this::onLocationChanged
                );
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider_info);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e(TAG, "Impossible to connect to LocationManager", e);
        }
    }


    private void openingMenu() {
        iv_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }


    private void onHowitWorkClickHandler() {
        rl_how_it_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashboardActivity.this, HowItWorks.class));
            }
        });
    }

    public boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        Log.e(TAG, "surfaceCreated: ");
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "surfaceCreated: Exception: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


}

