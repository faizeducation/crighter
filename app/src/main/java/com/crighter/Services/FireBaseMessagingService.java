package com.crighter.Services;

/**
 * Created by gold on 8/11/2018.
 */

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by gold on 7/12/2018.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "xxx";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.e("TAg", "the message from fire base is: " + remoteMessage.toString());
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try
            {

                String driverArrived = remoteMessage.getData().get("DriverArrived");
                Log.e("TAG", "the driver arrived status is: " + driverArrived);
                if (driverArrived!=null){


                }else {

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(remoteMessage.getNotification() != null)
        {

            Log.e("TAg", "the notification is: " + remoteMessage.getNotification());


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
