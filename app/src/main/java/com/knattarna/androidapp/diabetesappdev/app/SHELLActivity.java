package com.knattarna.androidapp.diabetesappdev.app;

import android.app.AlarmManager;
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
    private Context ctex        = null;

    //used to uniquely identify a PendingIntent
    private int uniqueID        = 0;

    //every activity holds a unique Intent that controls alarm on/off and reschedules etc
    private PendingIntent alarmIntent = null;

    //An activity is dependent on which day that holds it therefore should always be
    //constructed with a calendar instantiated within that day .. preferably
    public SHELLActivity(Context ctx,Calendar cal) {

        this.ctex = ctx;

        this.name = "Ny Aktivitet";
        time = cal;

        this.info = "";
        BloodSLevel = 0;

        isDone();
        generateUniqueID();
        //setAlarm();
    }

    public SHELLActivity(Context ctx, String name, int hour, int min, int day) {

        this.ctex = ctx;
        this.name = name;

        time = Calendar.getInstance();

        this.time.set(Calendar.HOUR_OF_DAY,hour);
        this.time.set(Calendar.MINUTE,min);
        this.time.set(Calendar.DAY_OF_YEAR,day);

        this.info = null;
        BloodSLevel = 0;

        isDone();
        generateUniqueID();
        //setAlarm();
    }

    //constructor used for instantiating objects from database
    public SHELLActivity(Context ctx, String name, String info, int hour, int min, int day, int uniqueID) {

        this.ctex = ctx;
        this.name = name;

        time = Calendar.getInstance();

        setTime(hour,min);
        setDate(day);

        setInfo(info);
        setUniqueID(uniqueID);

        BloodSLevel = 0;

        isDone();
        //setAlarm();
    }

    //constructor used for instantiating objects from database
    public SHELLActivity(Context ctx, String name, String info, int done, long timeInMili, int uniqueID) {

        this.ctex = ctx;
        this.name = name;

        this.time = Calendar.getInstance();
        this.time.setTimeInMillis(timeInMili);

        setInfo(info);
        setUniqueID(uniqueID);

        BloodSLevel = 0;

        this.done = done == 1;

        //setAlarm();
    }



    public SHELLActivity(Context ctx, String name, String info, int hour, int min, int day,  int uniqueID, double bloodSLevel) {

        this.ctex = ctx;
        this.name = name;

        time = Calendar.getInstance();

        setTime(hour,min);
        setDate(day);

        setInfo(info);
        setUniqueID(uniqueID);

        setBloodSLevel(bloodSLevel);

        isDone();
        //setAlarm();
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

    public Calendar getTime()
    {
        return this.time;
    }

    public String getInfo() {
        return info;
    }

    public double getBloodSLevel() {
        return  BloodSLevel;
    }

    public boolean getDone() {
        return done;
    }

    public PendingIntent getAlarmIntent() {
        return alarmIntent;
    }

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
        this.time.set(Calendar.HOUR_OF_DAY, hour);
        this.time.set(Calendar.MINUTE,min);

        //reset the alarm when time changes
        //setAlarm();
        //check whether act is done
        isDone();
    }

    public void offsetTime(int offset_h, int offset_m)
    {
        int hour = getHour()+offset_h;
        int min = getMin()+offset_m;
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

    public void setDate(int dayOfYear)
    {
        this.time.set(Calendar.DAY_OF_YEAR,dayOfYear);
    }

    public int getDate() { return this.time.get(Calendar.DAY_OF_YEAR); }

    //functions for generating unique alarms
    public int getUniqueID()
    {
        return this.uniqueID;
    }

    public void setUniqueID(int uniqueID)
    {
        this.uniqueID = uniqueID;
    }

    private void generateUniqueID()
    {
        //didn't put to much effort on this.. hopefully its ok..
        this.uniqueID =  (int) (getDate()+getMin()+getHour());
    }

    //sets the Intent to broadcast to the alarm receiver.. or something
    public void setAlarmIntent(Context context, Intent intent)
    {
        //need to uniquely identify the activity, using data that doesn't change..
        //this does not do that properly, but it's a start
        this.alarmIntent = PendingIntent.getBroadcast(context,getUniqueID(),intent,0);
    }

    public void setAlarm()
    {
        if(getDone())
            return;

        AlarmManager ALARM = (AlarmManager) ctex.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(ctex, AlarmReceiver.class);

        intent.putExtra("Title", getName());
        intent.putExtra("Info", getInfo());

        setAlarmIntent(ctex, intent);
        ALARM.set(AlarmManager.RTC_WAKEUP, getTime().getTimeInMillis(),
                getAlarmIntent());
    }


    //Activities can be sorted
    @Override
    public int compareTo(SHELLActivity another) {
        if(this.getDate() == another.getDate())
            return (int) ((this.getTime().getTimeInMillis()) - (another.getTime().getTimeInMillis()));
        else
            return this.getDate() - another.getDate();
    }


}