package com.knattarna.androidapp.diabetesappdev.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;
import android.os.Vibrator;

/**
 * Created by mrbent on 11/30/14.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(k1,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(k1,1,intent,0);

        NotificationManager notificationManager =
                (NotificationManager) k1.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(k1)
                                    .setContentTitle("shitty app")
                                    .setContentText("Shitty app wants your attention")
                                    .setSmallIcon(R.drawable.icon)
                                    .setContentIntent(pIntent)
                                    .build();




        Vibrator vibrator = (Vibrator)  k1
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        notificationManager.notify(0,notification);
    }

}