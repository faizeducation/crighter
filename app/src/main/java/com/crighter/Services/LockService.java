package com.crighter.Services;

/**
 * Created by gold on 8/11/2018.
 */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.crighter.BroadCast.AlarmReceiver;
import com.crighter.BroadCast.NetworkChangeReceiver;
import com.crighter.BroadCast.PowerButtonBroadCast;
import com.crighter.BroadCast.Restarter;
import com.crighter.BroadCast.ScreenActionReceiver;
import com.crighter.Preferences.SPref;
import com.crighter.R;
import com.crighter.Utilities.StartUpAndLuncher;

public class LockService extends Service {
    BroadcastReceiver mReceiver;
    ScreenActionReceiver screenactionreceiver;
    NetworkChangeReceiver ntr;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        screenactionreceiver = new ScreenActionReceiver();
        ntr = new NetworkChangeReceiver();
        createNotificationChannel();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        registerReceiver(screenactionreceiver, screenactionreceiver.getFilter());


        Log.e("TAg", "the service is started");
        SharedPreferences sp = getSharedPreferences("re", 0);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("regi", true);
        e.commit();

        registerReceiver(ntr,ntr.getFilter());

        return START_STICKY;
    }
    public class LocalBinder extends Binder {
        LockService getService() {
            return LockService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDestroy() {
        Log.e("TAg", "stop service called");

        //GlobReciver.unRegisterReciver(getApplicationContext());

        SharedPreferences sharedPreferences = getSharedPreferences(SPref.PREF_SERVICE, 0);
        boolean intensiveStop = SPref.getServiceStatus(sharedPreferences);
        /*if (intensiveStop) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("restartservice");
            broadcastIntent.setClass(this, Restarter.class);
            this.sendBroadcast(broadcastIntent);
        }*/

        if (isAlarmAlreadyRunning()){
            Log.e("TAG", "alarm is running");
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent morningIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, morningIntent, 0);
            alarmManager.cancel(pendingIntent);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent morningIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, morningIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);

        unregisterReceiver(screenactionreceiver);
        unregisterReceiver(ntr);

        //removing gps update
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        gpsTracker.stopUsingGPS();
        gpsTracker.remvoeLocationUpdate();
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        final BroadcastReceiver mReceiver = new PowerButtonBroadCast();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        //  GlobReciver.unRegisterReciver(getApplicationContext());
        //Code here
        //stopSelf();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        // this.sendBroadcast(broadcastIntent);
        StartUpAndLuncher.autoStartForOPPO(getApplicationContext());

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent morningIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, morningIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);

        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        gpsTracker.stopUsingGPS();
        gpsTracker.remvoeLocationUpdate();
    }

    public boolean isAlarmAlreadyRunning()
    {
        Intent morningIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 0,
                morningIntent, PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custome_push);
            CharSequence name = "Crither";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("NOTIFICATION_CHANNEL", name, importance);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, "NOTIFICATION_CHANNEL")
                    .setContent(contentView).setSound(null)
                    .build();

            startForeground(1001, notification);
        }
    }



}
