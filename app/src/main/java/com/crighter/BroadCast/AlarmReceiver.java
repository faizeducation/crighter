package com.crighter.BroadCast;

/**
 * Created by Arain on 9/18/2018.
 */

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.Log;

import com.crighter.Services.LockService;
import com.crighter.Utilities.Initializer;

/**
 * Created by gold on 9/18/2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    static Ringtone ringtone;
    AlarmManager alarmManager;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Initializer.isSerivceRunning){
        int alarmId = intent.getExtras().getInt("alarmId");
        Log.e("TAG", "alarmId: " + alarmId);

        Initializer initializer = new Initializer();
        //if (initializer.isMyServiceRunning(LockService.class, context)){

        ScreenActionReceiver screenActionReceiver = new ScreenActionReceiver();
       // context.unregisterReceiver(screenActionReceiver);
        context.stopService(new Intent(context, LockService.class));
       // }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, LockService.class));
        }
        else {
            context.startService(new Intent(context, LockService.class));
        }
       /* alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent morningIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, morningIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent);
*/
        Log.e("TAG", "Alaram restarted and service is also restarted");

    }
    }

}
