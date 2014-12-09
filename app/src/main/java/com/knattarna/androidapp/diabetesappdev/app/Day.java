package com.knattarna.androidapp.diabetesappdev.app;


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


    private ArrayList<SHELLActivity> dayActs;

    private String date;
    private String dayOfTheWeek;
    private Calendar day = null;

    private final ArrayList<String> ACTIVITIES = new ArrayList<String>() {
    {
        add("Frukost");
        add("Mellanmål");
        add("Lunch");
        add("Mellanmål");
        add("Middag");
        add("Kvällsmål");

    }};

    public Day() {

        if(day == null)
            day = Calendar.getInstance();

        date = day.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.SHORT, Locale.US);
        dayOfTheWeek = day.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        //fill day with standard activities
        fillDay();
    }

    //create days from a set calendar
    public Day(Calendar cal)
    {

        day = cal;
        date = day.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.SHORT, Locale.US);
        dayOfTheWeek = day.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);

        //fill day with standard activities
        fillDay();
    }

    public String getDate(){ return date;}

    public String getDayOfTheWeek(){return dayOfTheWeek;}

    public ArrayList<SHELLActivity> getDayActs(){return dayActs;}

    public void setDate(String date){this.date = date;}

    public void setDayOfTheWeek(String dayOfTheWeek) {
        dayOfTheWeek = dayOfTheWeek;
    }

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
                int htemp = 8;

                for(int i = 0; i < ACTIVITIES.size(); ++i){
                    if(htemp >= 24)
                        break;

                    SHELLActivity temp = new SHELLActivity(ACTIVITIES.get(i), htemp, 0, day.get(Calendar.DAY_OF_YEAR));
                    add(temp);
                    htemp = htemp + 2;
                }
            }};

        sortActs();
    }

    //updates every activity after this position with values dependent on this positions
    public void updateDay(int position, Calendar offset)
    {
        for(++position;position < dayActs.size();++position) {
            dayActs.get(position).setTime(offset);
            //does not work
            if(dayActs.get(position).getTime().get(Calendar.DAY_OF_YEAR) != day.get(Calendar.DAY_OF_YEAR))
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

}