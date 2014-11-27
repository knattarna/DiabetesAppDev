package com.knattarna.androidapp.diabetesappdev.app;

/**
 * Created by Jacob on 2014-11-20.
 * Activity class med lite set och get funktioner för acitvity window
 */
public class Activity
{
    private String name;
    //Kan göras till ett TIME objekt senare om vi vill
    private int hour;
    private int min;
    private String info;
    private double BloodSLevel;
    private boolean done;

//Few constructors
    public Activity() {
        this.name = "Test";
        this.hour = 13;
        this.min = 37;
        this.info = null;
        BloodSLevel = 0;
        this.done = false;
    }
    public Activity(Activity act)
    {
        this.name = act.getName();
        this.hour = act.getHour();
        this.min = act.getMin();
        this.info = act.getInfo();
        this.BloodSLevel = act.getBloodSLevel();
    }

    public Activity(String name, int hour, int min) {
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.info = null;
        BloodSLevel = 0;
        this.done = false;
    }

    public Activity(String name, int hour, int min, String info) {
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.info = info;
        BloodSLevel = Double.parseDouble(null);
        this.done = false;
    }
    public Activity(String name, int hour, int min, String info, double bloodSLevel, boolean done) {
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.info = info;
        BloodSLevel = bloodSLevel;
        this.done = done;
    }
    //Lite Getfunktioner
    public String getName()
    {
        return this.name;
    }

    public int getHour()
    {
        return this.hour;
    }

    public int getMin() {
        return min;
    }

    public String getInfo() {
        return info;
    }

    public double getBloodSLevel() {return  BloodSLevel;}
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
        this.hour = hour;
        this.min = min;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
