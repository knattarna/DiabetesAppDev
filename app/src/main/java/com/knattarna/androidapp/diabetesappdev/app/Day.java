package com.knattarna.androidapp.diabetesappdev.app;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jacob on 2014-11-21.
 * Class with members for the MainActivity Window.
 */
public class Day {


    private ArrayList<Activity> DayActs;
    private String date;
    private String DayOfTheWeek;

        //Construktor som sätter standard dag.
        public Day() {

            final ArrayList<String> meals = new ArrayList<String>() {
                {
                    add("Breakfast");
                    add("Brunch");
                    add("Lunch");
                    add("Snack");
                    add("Dinner");
                }};
            final Calendar cal = Calendar.getInstance();
            date = cal.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.SHORT, Locale.US);
            DayOfTheWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

            DayActs = new ArrayList<Activity>() {
                {
                    int htemp = 7;
                    for(int i = 0; i < meals.size(); i++) {

                        Activity temp = new Activity(meals.get(i), htemp, 0);
                        add(temp);
                        htemp = htemp + 4;
                    }
                }};
        }

    public String getDate(){ return date;}

    public String getDayOfTheWeek(){return DayOfTheWeek;}

    public ArrayList<Activity> getDayActs(){return DayActs;}

    public void setDate(String date){this.date = date;}

    public void setDayOfTheWeek(String dayOfTheWeek) {
        DayOfTheWeek = dayOfTheWeek;
    }
}