package com.knattarna.androidapp.diabetesappdev.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by mrbent on 12/3/14.
 */
public class Week
{

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

        //Calendar cal = Calendar.getInstance();

        for(int i = 0; i < 7; ++i)
        {
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.set(Calendar.DAY_OF_YEAR, tmpCal.get(Calendar.DAY_OF_YEAR)+i);
            //cal.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK)+1);
            days.add(new Day(tmpCal));
        }

    }

    public void sync_days()
    {
    }

    public Day today() {return days.get(0);}
}
