package com.knattarna.androidapp.diabetesappdev.app;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jacob on 2014-11-21.
 * Class with members for the MainActivity Window.
 */
public class Day {


    private ArrayList<Activity> dayActs;
    private String date;
    private String dayOfTheWeek;

    private final ArrayList<String> ACTIVITIES = new ArrayList<String>() {
    {
        add("Breakfast");
        add("Brunch");
        add("Lunch");
        add("Snack");
        add("Dinner");
    }};

    public Day() {

        final Calendar cal = Calendar.getInstance();
        date = cal.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.SHORT, Locale.US);
        dayOfTheWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

        //initialize the standard day of activities
        dayActs = new ArrayList<Activity>() {
             {
                int htemp = 7;
                for(int i = 0; i < ACTIVITIES.size(); ++i){
                    Activity temp = new Activity(ACTIVITIES.get(i), htemp, 0);
                    add(temp);
                    htemp = htemp + 4;
                }
             }};
    }

    public String getDate(){ return date;}

    public String getDayOfTheWeek(){return dayOfTheWeek;}

    public ArrayList<Activity> getDayActs(){return dayActs;}

    public void setDate(String date){this.date = date;}

    public void setDayOfTheWeek(String dayOfTheWeek) {
        dayOfTheWeek = dayOfTheWeek;
    }
}