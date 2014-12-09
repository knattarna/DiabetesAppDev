package com.knattarna.androidapp.diabetesappdev.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jacob on 2014-11-20.
 * Activity class med lite set och get funktioner för acitvity window
 */
public class SHELLActivity implements Comparable<SHELLActivity>
{

    private double BloodSLevel;
    private boolean done;

    private String name         = null;
    private Calendar time       = null;
    private String info         = null;

    //every activity holds a unique Intent that controls alarm on/off and reschedules etc
    private PendingIntent alarmIntent = null;

//Few constructors

    public SHELLActivity() {
        this.name = "Ny Aktivitet";

        if(time == null)
            time = Calendar.getInstance();

        this.time.set(Calendar.HOUR_OF_DAY,23);
        this.time.set(Calendar.MINUTE,59);

        this.info = "";
        BloodSLevel = 0;

        isDone();
    }

    public SHELLActivity(String name, int hour, int min, int day) {
        this.name = name;

        if(time == null)
            time = Calendar.getInstance();

        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);
        this.time.set(Calendar.DAY_OF_YEAR,day);

        this.info = null;
        BloodSLevel = 0;

        isDone();
    }

    public SHELLActivity(String name, int hour, int min, String info, double bloodSLevel, int day, boolean done) {
        this.name = name;

        if(time == null)
            time = Calendar.getInstance();

        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);
        this.time.set(Calendar.DAY_OF_YEAR,day);

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

    public int getDay() { return this.time.get(Calendar.DAY_OF_YEAR); }

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
        //cannot change time on passed events or change to passed event
        Calendar tmp = Calendar.getInstance();
        tmp.set(Calendar.HOUR_OF_DAY,hour);
        tmp.set(Calendar.MINUTE,min);

        if (getDone() || tmp.getTimeInMillis() < System.currentTimeMillis() )
            return;

        this.time.set(Calendar.HOUR_OF_DAY, hour);
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
        Calendar tmpCal = Calendar.getInstance();

        if(this.time.get(Calendar.DAY_OF_YEAR) == tmpCal.get(Calendar.DAY_OF_YEAR))
            this.done = this.time.getTimeInMillis() < tmpCal.getTimeInMillis();
        else
            this.done = this.time.get(Calendar.DAY_OF_YEAR) < tmpCal.get(Calendar.DAY_OF_YEAR);

    }


    //sets the Intent to broadcast to the alarm receiver.. or something
    public void setAlarmIntent(Context context, Intent intent)
    {
        this.alarmIntent = PendingIntent.getBroadcast(context,
                                            (getHour()+getMin()+this.time.DAY_OF_MONTH),
                                            intent,0);
    }

    @Override
    public int compareTo(SHELLActivity another) {
        return (int) ((this.getTime().getTimeInMillis()) - (another.getTime().getTimeInMillis()));
    }
}
