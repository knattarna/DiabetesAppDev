package com.knattarna.androidapp.diabetesappdev.app;


import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Jacob on 2014-11-21.
 * Class with members for the MainActivity Window.
 */
public class Day
{
    private ArrayList<SHELLActivity> dayActs = null;

    private Calendar date = null;
    private Context ctex = null;

    private final ArrayList<String> ACTIVITIES = new ArrayList<String>() {
        {
            add("Breakfast");
            add("Second Breakfast");
            add("Elevenses");
            add("Luncheon");
            add("Afternoon Tea");
            add("Dinner");
            add("Supper");
        }};

    public Day(Context ctex)
    {
        this.ctex = ctex;
        date = Calendar.getInstance();

        dayActs= new ArrayList<SHELLActivity>();
        //fillDay();
    }

    //create days from a set calendar
    public Day(Context ctex, Calendar cal)
    {
        this.ctex = ctex;
        dayActs= new ArrayList<SHELLActivity>();

        date = cal;
        //fill day with standard activities
        fillDay();
    }

    public Day(Context ctex, Calendar cal, ArrayList<SHELLActivity> acts)
    {
        this.ctex = ctex;
        this.date = cal;
        dayActs = acts;
    }

    //returns string representation of the current weekday
    public String getDayOfTheWeek()
    {
        return this.date.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,Locale.US);
    }

    public Calendar getDate()
    {
        return this.date;
    }

    public ArrayList<SHELLActivity> getDayActs(){return dayActs;}

    public void addActivity(SHELLActivity act)
    {
        dayActs.add(act);
        sortActs();
    }

    public void fillDay()
    {
        //initialize the standard day of activities
        dayActs = new ArrayList<SHELLActivity>() {
            {
                int htemp = 7;

                for(int i = 0; i < ACTIVITIES.size(); ++i){
                    if(htemp >= 24)
                        break;

                    SHELLActivity temp = new SHELLActivity(ctex, ACTIVITIES.get(i), htemp, 0, date.get(Calendar.DAY_OF_YEAR));
                    add(temp);
                    htemp = htemp + 2;
                }
            }};

        sortActs();
    }

    //updates every activity after this position with values dependent on this positions
    public void updateDay(int position, int offset_h, int offset_m)
    {
        for(++position;position < dayActs.size();++position) {

            //move the activity forward
            dayActs.get(position).offsetTime(offset_h,offset_m);
            //reset the alarm
            dayActs.get(position).setAlarm();

            System.out.println(dayActs.get(position).getTime().get(Calendar.DAY_OF_YEAR));
            System.out.println(date.get(Calendar.DAY_OF_YEAR));
            if(dayActs.get(position).getTime().get(Calendar.DAY_OF_YEAR) != date.get(Calendar.DAY_OF_YEAR))
            {
                dayActs.remove(position);
            }
        }
        sortActs();
    }

    public void sortActs()
    {
        Collections.sort(dayActs);
    }

    public void setAlarms()
    {
        for (int i = 0; i < dayActs.size(); ++i)
        {
            dayActs.get(i).setAlarm();
        }
    }

}