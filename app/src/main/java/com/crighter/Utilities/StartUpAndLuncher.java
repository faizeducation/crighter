package com.crighter.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.crighter.Preferences.SPref;

/**
 * Created by gold on 9/12/2018.
 */

public class StartUpAndLuncher {

    public static void checkingDeviceForOPPO(final Activity context)
    {
        if (Build.MANUFACTURER.equalsIgnoreCase("oppo") || Build.MANUFACTURER.equalsIgnoreCase("vivo")
                || Build.MANUFACTURER.equalsIgnoreCase("lenovo")
                || Build.MANUFACTURER.equalsIgnoreCase("mi")
                || Build.MANUFACTURER.equalsIgnoreCase("xiamoi")) {

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("Alert");
            alert.setMessage("For Oppo devices press the recent task button from bottom of device, and swipe down the Crighter app to make a lock on it. This will prevent device to stop running");
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = context.getSharedPreferences(SPref.PREF_NAME_FOR_ALERT, 0);
                    SPref.puttingDialogShown(sharedPreferences, true);
                }
            });
            alert.show();
        }
    }

    public static void autoStartForOPPO(Context context)
    {
        if (Build.MANUFACTURER.equalsIgnoreCase("oppo")) {
            try {
                Intent intent = new Intent();
                intent.setClassName("com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity");
                context.startActivity(intent);
            } catch (Exception e) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.oppo.safe",
                            "com.oppo.safe.permission.startup.StartupAppListActivity");
                    context.startActivity(intent);

                } catch (Exception ex) {
                    try {
                        Intent intent = new Intent();
                        intent.setClassName("com.coloros.safecenter",
                                "com.coloros.safecenter.startupapp.StartupAppListActivity");
                        context.startActivity(intent);
                    } catch (Exception exx) {

                    }
                }
            }
        }
    }



    public static void autoLaunchVivo(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                context.startActivity(intent);
            } catch (Exception ex) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                    context.startActivity(intent);
                } catch (Exception exx) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
