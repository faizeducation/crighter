package com.crighter.BroadCast;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.crighter.Services.CamerService2;
import com.crighter.Services.GPSTracker;
import com.crighter.Services.RecordingAudio;
import com.crighter.URLS.APIURLS;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

/**
 * Created by gold on 8/11/2018.
 */

public class PowerButtonBroadCast extends BroadcastReceiver implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static boolean wasScreenOn = true;
    private int timer = 5;
    Context mContext;
    private Handler mHandler = new Handler();

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    public static int counter = 1;


    @Override
    public void onReceive(final Context context, final Intent intent) {
        int count = 0;
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            wasScreenOn = false;
            SharedPreferences sharedPreferences = context.getSharedPreferences("time", 0);

            long firsTme = System.currentTimeMillis();
            long secondtime = System.currentTimeMillis();

            long preTime = SPref.getTingFirstTime(sharedPreferences,SPref.FIRST_TIME);
            long diff = firsTme-preTime;
            Log.e("TAG", "the difference is " + diff);

            if (20<diff && 6999>diff){
                diff = 20000;
                Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(400);
                //mHandler = new Handler();
                // useHandler(context);

                //context.startService(new Intent(context, ImageCaptureService.class));
                //context.startService(new Intent(context, CamerService.class));

               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, CamerService2.class));
                } else {
                    context.startService(new Intent(context, CamerService2.class));
                }*/

               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(new Intent(context, RecordingAudio.class));
                } else {
                    context.startService(new Intent(context, RecordingAudio.class));
                }*/
                context.startService(new Intent(context, CamerService2.class));
                context.startService(new Intent(context, RecordingAudio.class));
                Log.e("LOB", "here power button is pressed");
             /*   GPSTracker gpsTracker = new GPSTracker(context);
                Log.e("TAG", "the latitude are " + gpsTracker.latitude);
                Log.e("TAG", "the longitude are " + gpsTracker.longitude);*/
                callingHandlerTimer(context);
            }
            SPref.storingFirstTimeAndsecondTime(sharedPreferences, firsTme, secondtime);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;

        }else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.e("LOB","userpresent");
            Log.e("LOB","wasScreenOn"+wasScreenOn);
        }
    }


    private void uploadingDataToServer(Context context, String userID, String audioPath, String ImagePath,
                                       String ImagePath2, String ImagePath3, String ImagePath4, String ImagePath5, String lat, String lng)
    {
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            Log.e("TAG", "the preff data from audio path "+ audioPath);
            Log.e("TAG", "the preff data from image path 1 "+ ImagePath);
            Log.e("TAG", "the preff data from image path 2 "+ ImagePath2);
            Log.e("TAG", "the preff data from image path 3 "+ ImagePath3);
            Log.e("TAG", "the preff data from image path 4 "+ ImagePath4);
            Log.e("TAG", "the preff data from image path 5 "+ ImagePath5);

            String imageUrl = getURLForResource(R.drawable.logo);

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
                        }

                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            String responseFromServer = serverResponse.getBodyAsString();
                            Log.e("TAG", "the response from server for upload image: " + serverResponse.getBodyAsString());
                            if (responseFromServer!=null) {
                                try {
                                    JSONObject jObj = new JSONObject(responseFromServer);
                                    boolean error = jObj.getBoolean("error");
                                    if (!error) {

                                        clearCache(ImagePath);
                                        clearCache(ImagePath2);
                                        clearCache(ImagePath3);
                                        clearCache(ImagePath4);
                                        clearCache(ImagePath5);
                                        clearCache(audioPath);

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
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
        }
    }

    private void callingHandlerTimer(final Context mContext)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferencesUser = mContext.getSharedPreferences(SPref.PREF_USER_CRED, 0);
                String userId = SPref.getStringPref(sharedPreferencesUser, SPref.USER_ID);
                SharedPreferences sharedPreferencesAudioPath = mContext.getSharedPreferences(SPref.PRE_AUDIO, 0);
                String audioPath = SPref.getStringPref(sharedPreferencesAudioPath, SPref.AUDIO_FILE_PATH);
                SharedPreferences sharedPreferencesVideoPath = mContext.getSharedPreferences(SPref.PREF_IMAGS, 0);
                String imagePath = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH);
                String imagePath2 = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH2);
                String imagePath3 = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH3);
                String imagePath4 = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH4);
                String imagePath5 = SPref.getStringPref(sharedPreferencesVideoPath, SPref.IMAGE_PATH5);
                 GPSTracker gpsTracker = new GPSTracker(mContext);
                Log.e("TAG", "the preff data from userId "+ userId);
                Log.e("TAG", "the preff data from audioPath "+ audioPath);

                Log.e("TAG", "the preff data from latitude "+ gpsTracker.latitude);
                Log.e("TAG", "the preff data from longitude "+ gpsTracker.longitude);

               /* if (imagePath2.length()>3){imagePath2 = imagePath;}
                if (imagePath3.length()>3){imagePath3 = imagePath;}
                if (imagePath4.length()>3){imagePath4 = imagePath;}
                if (imagePath5.length()>3){imagePath5 = imagePath;}*/

                String lat = String.valueOf(gpsTracker.latitude);
                String lng = String.valueOf(gpsTracker.longitude);
                if (lat.equals("0.0") || lat.length()<3){
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(SPref.PREF_USER_CRED, 0);
                 lat  = SPref.gettingValues(sharedPreferences, SPref.locationLat);
                  lng  = SPref.gettingValues(sharedPreferences, SPref.locationLng);
                }
                uploadingDataToServer(mContext, userId, audioPath, imagePath, imagePath2, imagePath3, imagePath4, imagePath5, lat, lng);

            }
        }, 18000);
    }

    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            mMap.setMyLocationEnabled(true);
            return;
        }

        mGoogleApiClient.connect();
        ///

    }


    ///

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.e("TAG", "the Current Location Lat are: " + latitude);
            Log.e("TAG", "the Current Location Lng are: " + longitude);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000); //1 seconds
        mLocationRequest.setFastestInterval(1000); //1 seconds
        mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(mGoogleApiClient.isConnected()){

            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    private void clearCache(String path)
    {
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Log.e("TAG", "the file deleted " + path);
            } else {
                Log.e("TAG", "error deleting path " + path);
            }
        }
    }
}