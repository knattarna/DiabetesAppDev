package com.knattarna.androidapp.diabetesappdev.app;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mrbent on 12/3/14.
 */
public class Week {

    private ArrayList<Day> days = new ArrayList<Day>();
    private String week_number = null;

    public Week()
    {
        createWeek();
    }

    public ArrayList<Day> getDays() {return days;}

    public void createWeek()
    {
        //there is no data in database
        //create new Days

        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(cal.MONDAY);

        for(int i = 0; i < 7; ++i)
        {
            days.add(new Day(cal));
            cal.set(Calendar.DAY_OF_WEEK,i);
        }

    }

    public void sync_days()
    {

    }

    public Day today() {return days.get(0);}
}
