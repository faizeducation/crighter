package com.crighter.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.crighter.Database.CDatabase;
import com.crighter.Database.DbHelper;
import com.crighter.URLS.APIURLS;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by gold on 10/2/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    ArrayList<HashMap<String, String>> datList = new ArrayList<>();
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {
            // Do something
            Log.d("Network Available ", "Flag No 1");
            CDatabase db = new CDatabase(context);
            int count = db.getCount();
            if (count>0)
            {
                ArrayList<DbHelper> helpers = db.getOrderByStatus("1");
                if (helpers.size()>0)
                {
                    for (DbHelper dbHelper:helpers)
                    {
                        String tableID = dbHelper.getId();
                        String status = dbHelper.getStatus();
                        String userID = dbHelper.getUserid();
                        String audioPath = dbHelper.getAudiopath();
                        String image1 = dbHelper.getImage1();
                        String image2 = dbHelper.getImage2();
                        String image3 = dbHelper.getImage3();
                        String image4 = dbHelper.getImage4();
                        String image5 = dbHelper.getImage5();
                        String lat = dbHelper.getLat();
                        String lng = dbHelper.getLng();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("tableID", tableID);
                        map.put("status", status);
                        map.put("userID", userID);
                        map.put("audioPath", audioPath);
                        map.put("image1", image1);
                        map.put("image2", image2);
                        map.put("image3", image3);
                        map.put("image4", image4);
                        map.put("image5", image5);
                        map.put("lat", lat);
                        map.put("lng", lng);
                        datList.add(map);
                    }
                }
            }
            for (int i=0;i<datList.size();i++)
            {
                String tableID = datList.get(i).get("tableID");
                String status = datList.get(i).get("status");
                String userID = datList.get(i).get("userID");
                String audioPath = datList.get(i).get("audioPath");
                String image1 = datList.get(i).get("image1");
                String image2 = datList.get(i).get("image2");
                String image3 = datList.get(i).get("image3");
                String image4 = datList.get(i).get("image4");
                String image5 = datList.get(i).get("image5");
                String lat = datList.get(i).get("lat");
                String lng = datList.get(i).get("v");
                uploadingDataToServer(context, tableID, userID, audioPath, image1, image2, image3, image4, image5, lat, lng);
            }
        }//end for netowrk available
    }

    private void uploadingDataToServer(Context context,String tableID,  String userID, String audioPath, String ImagePath,
                                       String ImagePath2, String ImagePath3, String ImagePath4, String ImagePath5, String lat, String lng)
    {
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
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
                          /*  CDatabase db = new CDatabase(context);
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
                            CDatabase db = new CDatabase(context);
                            db.deleteFromTable(Long.valueOf(tableID));
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

    public IntentFilter getFilter(){
        final IntentFilter filter = new IntentFilter();
        filter.addAction(CONNECTIVITY_ACTION);
        return filter;
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