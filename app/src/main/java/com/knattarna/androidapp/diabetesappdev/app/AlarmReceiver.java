package com.knattarna.androidapp.diabetesappdev.app;

import android.app.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import java.util.Calendar;

/**
 * Created by mrbent on 11/30/14.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(k1,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(k1,1,intent,0);

        String title = "App";
        String info  = "App wants your attention!";

        Bundle extras = k2.getExtras();
        if(extras != null)
        {
            title = extras.getString("Title");
            info  = extras.getString("Info");
        }

        NotificationManager notificationManager =
                (NotificationManager) k1.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(k1)
                                    .setContentTitle((title))
                                    .setContentText(info)
                                    .setSmallIcon(R.drawable.logo_launcher)
                                    .setContentIntent(pIntent)
                                    .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;



        Vibrator vibrator = (Vibrator)  k1
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        notificationManager.notify(0,notification);
    }

}