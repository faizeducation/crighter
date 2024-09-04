package com.crighter.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.crighter.Services.LockService;
import com.crighter.Utilities.Initializer;

/**
 * Created by gold on 9/12/2018.
 */

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop");
       // Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();

        if (Initializer.isSerivceRunning) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LockService.class));
            } else {
                context.startService(new Intent(context, LockService.class));
            }
        }
    }
}
