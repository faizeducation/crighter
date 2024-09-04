package com.crighter.BroadCast;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.crighter.Database.CDatabase;
import com.crighter.Database.DbHelper;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by gold on 9/25/2018.
 */

public class ScreenActionReceiver extends BroadcastReceiver {

    private String TAG = "ScreenActionReceiver";
    public static boolean wasScreenOn = true;
    private int timer = 5;
    Context mContext;
    private Handler mHandler = new Handler();

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    public static int counter = 1;



    @Override
    public void onReceive(Context context, Intent intent) {

        // buildGoogleApiClient();

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
                SharedPreferences sharedPreferencesUser = context.getSharedPreferences(SPref.PREF_USER_CRED, 0);
                String phone = SPref.getStringPref(sharedPreferencesUser, SPref.AUTH_PHONE);
                String name = SPref.getStringPref(sharedPreferencesUser, SPref.USER_FULLNAME);
                String authName = SPref.getStringPref(sharedPreferencesUser, SPref.AUTH_FULLNAME);

                GPSTracker gpsTracker = new GPSTracker(context);
                String lat = String.valueOf(gpsTracker.latitude);
                String lng = String.valueOf(gpsTracker.longitude);
                if (lat.equals("0.0") || lat.length()<3){
                    SharedPreferences sharedPreferences1 = context.getSharedPreferences(SPref.PREF_USER_CRED, 0);
                    lat  = SPref.gettingValues(sharedPreferences1, SPref.locationLat);
                    lng  = SPref.gettingValues(sharedPreferences1, SPref.locationLng);
                }

                //sending SMS
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if (context.checkSelfPermission(Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED){
                        sendingSms(phone, name, authName, context, lat, lng);
                    }
                }else {
                    sendingSms(phone, name, authName, context, lat, lng);
                }

                context.startService(new Intent(context, CamerService2.class));
                context.startService(new Intent(context, RecordingAudio.class));

                Log.e("LOB", "here power button is pressed");
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

                           /* CDatabase db = new CDatabase(mContext);
                            DbHelper helper = new DbHelper();
                            helper.setStatus("1");
                            helper.setUserid(userID);
                            helper.setAudiopath(audioPath);
                            helper.setImage1(ImagePath);
                            helper.setImage2(ImagePath2);
                            helper.setImage3(ImagePath3);
                            helper.setImage4(ImagePath4);
                            helper.setImage5(ImagePath5);
                            helper.setLat(lat);
                            helper.setLng(lng);
                            long inserted = db.insertDatatoDb(helper);
                            if (inserted!=-1)
                            {
                                Log.e("TAG", "data inserted to db");
                            }*/
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
                    if (lat==null || lng==null){
                        lat = "32.332342";
                        lng = "74.3434435";
                    }
                }

                gpsTracker.remvoeLocationUpdate();

                if(isNetworkAvailable(mContext)) {
                    uploadingDataToServer(mContext, userId, audioPath, imagePath, imagePath2, imagePath3, imagePath4, imagePath5, lat, lng);
                }else {

                    CDatabase db = new CDatabase(mContext);
                    DbHelper helper = new DbHelper();
                    helper.setStatus("1");
                    helper.setUserid(userId);
                    helper.setAudiopath(audioPath);
                    helper.setImage1(imagePath);
                    helper.setImage2(imagePath2);
                    helper.setImage3(imagePath3);
                    helper.setImage4(imagePath4);
                    helper.setImage5(imagePath5);
                    helper.setLat(lat);
                    helper.setLng(lng);
                    long inserted = db.insertDatatoDb(helper);
                    if (inserted!=-1)
                    {
                        Log.e("TAG", "data inserted to db");
                    }
                }

            }
        }, 18000);
    }

    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }


    public IntentFilter getFilter(){
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        return filter;
    }

    public void sendSMS(String phoneNo, String msg, Context context) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            smsManager. sendMultipartTextMessage(phoneNo, null, parts,
                    null, null);
            //smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Log.e("TAH", "SMS SENT");
        } catch (Exception ex) {
            Toast.makeText(context, ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    public static boolean sendSMS2(Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        String name;

        try {
            if (simID == 0) {
                name = "isms";
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
            } else {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
            }

            return true;
        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("apipas", "Exception:" + e.getMessage());
        }
        return false;
    }

    public static boolean sendMultipartTextSMS(Context ctx, int simID, String toNum, String centerNum, ArrayList<String> smsTextlist, ArrayList<PendingIntent> sentIntentList, ArrayList<PendingIntent> deliveryIntentList) {
        String name;
        try {
            if (simID == 0) {
                name = "isms";
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendMultipartText", String.class, String.class, List.class, List.class, List.class);
                method.invoke(stubObj, toNum, centerNum, smsTextlist, sentIntentList, deliveryIntentList);
            } else {
                method = stubObj.getClass().getMethod("sendMultipartText", String.class, String.class, String.class, List.class, List.class, List.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsTextlist, sentIntentList, deliveryIntentList);
            }
            return true;
        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("apipas", "Exception:" + e.getMessage());
        }
        return false;
    }

    private boolean isNetworkAvailable(Context context) {
        boolean networkAvailablity  = false;
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected())
        {
            networkAvailablity = true;
        }
        return networkAvailablity;

    }

    private void sendingSms(final String phoneNum, final String name, final String authName, final Context context, final String lat, String lng)
    {
        String textSMS =  " Your Related " + name + " Is in Danger, Track https://maps.google.com/maps?q="+lat.toString()+","+lng.toString()+"+(Danger+Location)&z=14&ll="+lat+","+lng.toString() + " Check your email for more details it may take a while";
        //sending Sms
        sendSMS(phoneNum, authName + textSMS, context);

        int simID = 0;//0:sim_1,   1:sim_2
        ArrayList<String> messageList = SmsManager.getDefault().divideMessage(textSMS);
        if (messageList.size() > 1) {
            sendMultipartTextSMS(context, simID, phoneNum, null, messageList, null, null);
            sendMultipartTextSMS(context, 1, phoneNum, null, messageList, null, null);
        } else {
            sendSMS2(context,1,phoneNum,null," Your Related " + name + " Is in Danger, Track https://maps.google.com/maps?q="+lat.toString()+","+lng.toString()+"+(Danger+Location)&z=14&ll="+lat+","+lng.toString() + " Check your email for more details it may take a while",null,null);
        }
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