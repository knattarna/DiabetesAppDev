package com.knattarna.androidapp.diabetesappdev.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Vibrator;

/**
 * Created by mrbent on 11/30/14.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context k1, Intent k2) {
        // TODO Auto-generated method stub
        Toast.makeText(k1, "Alarm received!", Toast.LENGTH_LONG).show();


        Vibrator vibrator = (Vibrator)  k1
                .getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }

}