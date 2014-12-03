package com.knattarna.androidapp.diabetesappdev.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

import java.util.Calendar;

/**
 * Created by Jacob on 2014-11-20.
 * Activity class med lite set och get funktioner för acitvity window
 */
public class SHELLActivity
{

    private double BloodSLevel;
    private boolean done;

    private String name         = null;
    private Calendar time       = Calendar.getInstance();
    private String info         = null;

    //every activity holds a unique Intent that controls alarm on/off and reschedules etc
    private PendingIntent alarmIntent = null;

//Few constructors

    public SHELLActivity() {
        this.name = "TesT";

        this.time.set(Calendar.HOUR_OF_DAY,13);
        this.time.set(Calendar.MINUTE,37);

        this.info = null;
        BloodSLevel = 0;
        isDone();
    }
    /*
    public Activity(Activity act)
    {
        this.name = act.getName();
        this.hour = act.getHour();
        this.min = act.getMin();
        this.info = act.getInfo();
        this.BloodSLevel = act.getBloodSLevel();
        this.done = act.isDone();
    }*/

    public SHELLActivity(String name, int hour, int min) {
        this.name = name;

        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);

        this.info = null;
        BloodSLevel = 0;
        isDone();
    }

    public SHELLActivity(String name, int hour, int min, String info) {
        this.name = name;


        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);

        this.info = info;
        BloodSLevel = Double.parseDouble(null);
        isDone();
    }


    public SHELLActivity(String name, int hour, int min, String info, double bloodSLevel, boolean done) {
        this.name = name;

        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);

        this.info = info;
        BloodSLevel = bloodSLevel;
        isDone();
    }


    //get functions
    public String getName()
    {
        return this.name;
    }

    public int getHour()
    {
        return this.time.get(Calendar.HOUR_OF_DAY);
    }

    public int getMin() {
        return this.time.get(Calendar.MINUTE);
    }

    public Calendar getTime() {return this.time;}

    public String getInfo() {
        return info;
    }

    public double getBloodSLevel() {return  BloodSLevel;}

    public boolean getDone() {
        return done;
    }

    public PendingIntent getAlarmIntent() { return alarmIntent; }

    //set functions
    public void setBloodSLevel(double bloodSLevel) {
        BloodSLevel = bloodSLevel;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int hour, int min)
    {
        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);

        isDone();
    }

    public void setTime(Calendar offset)
    {

        int hour = getHour()+offset.get(Calendar.HOUR_OF_DAY);
        int min = getMin()+offset.get(Calendar.MINUTE);
        this.setTime(hour,min);
    }

    public void isDone()
    {
        this.done = this.time.getTimeInMillis() < System.currentTimeMillis();
    }


    //sets the Intent to broadcast to the alarm receiver.. or something
    public void setAlarmIntent(Context context, Intent intent)
    {
        this.alarmIntent = PendingIntent.getBroadcast(context,
                                            (getHour()+getMin()+this.time.DAY_OF_MONTH),
                                            intent,0);
    }

}
