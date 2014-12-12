package com.knattarna.androidapp.diabetesappdev.app;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Week
{

    private ArrayList<Day>  DAYS    = new ArrayList<Day>();
    private Context         context = null;
    public Week(Context context)
    {
        this.context = context;
        createWeek();
    }

    public ArrayList<Day> getDays() {return DAYS;}

    public void createWeek()
    {
        for(int i = 0; i < 7; ++i)
        {
            Calendar tmpCal = Calendar.getInstance();
            tmpCal.set(Calendar.DAY_OF_YEAR, tmpCal.get(Calendar.DAY_OF_YEAR)+i);
            //cal.set(Calendar.DAY_OF_WEEK, cal.get(Calendar.DAY_OF_WEEK)+1);
            DAYS.add(new Day(context,tmpCal));
        }

    }

    public Day today()
    {
        if( ! DAYS.isEmpty() )
            return DAYS.get(0);
        else
        {
            //creates a new Day Object that is today
            DAYS.add(new Day(context));
            return  DAYS.get(0);
        }
    }
}
