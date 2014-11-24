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
//Few constructors
    public Activity() {
        this.name = "Frukost";
        this.hour = 13;
        this.min = 37;
        this.info = null;
        BloodSLevel = 0;
    }

    public Activity(String name, int hour, int min) {
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.info = null;
        BloodSLevel = 0;
    }

    public Activity(String name, int hour, int min, String info) {
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.info = info;
        BloodSLevel = Double.parseDouble(null);
    }
    public Activity(String name, int hour, int min, String info, double bloodSLevel) {
        this.name = name;
        this.hour = hour;
        this.min = min;
        this.info = info;
        BloodSLevel = bloodSLevel;
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
}
