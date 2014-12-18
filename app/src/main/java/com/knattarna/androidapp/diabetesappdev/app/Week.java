package com.knattarna.androidapp.diabetesappdev.app;

import android.content.Context;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Week
{

    private ArrayList<Day>  DAYS    = new ArrayList<Day>();
    private Context         context = null;
    private DatabaseHelper  db      = null;


    public Week(Context context)
    {
        this.context = context;

        this.db = new DatabaseHelper(this.context);

        createWeek();
    }

    public ArrayList<Day> getDays() {return DAYS;}

    public void createWeek()
    {
        for(int i = 0; i < 7; ++i)
        {
            Calendar tmpCal = Calendar.getInstance();

            tmpCal.set(Calendar.DAY_OF_YEAR, tmpCal.get(Calendar.DAY_OF_YEAR)+i);

            DAYS.add(new Day(context,tmpCal));
        }

    }

    public Day today()
    {
        //Calendar now = Calendar.getInstance();

        if( ! DAYS.isEmpty() )
            return DAYS.get(0);
        else
        {
            //creates a new Day Object that is today
            DAYS.add(new Day(context));
            return  DAYS.get(0);
        }
    }

    /*
        DATABASE FUNCTIONS
     */
    public void writeDaysToDatabase()
    {
        this.db.clearTable();
        //just test to write a day to database
        for(int i = 0; i < getDays().size(); ++i) {
            this.db.writeDay(getDays().get(i));
        }
    }



    public void writeTodayToDatabase()
    {
        this.db.clearTable();
        //just test to write a day to database
            this.db.writeDay(today());
    }

    public void getDayFromDatabase()
    {
        ArrayList<SHELLActivity> shells = this.db.getAllActivities();

        if(shells.isEmpty()) {
            System.out.println("happens");
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(shells.get(0).getTime().getTimeInMillis());

        if(cal.get(Calendar.DAY_OF_YEAR) != Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
            return;

        if( !DAYS.isEmpty() ) {
            DAYS.set(0,new Day(context, cal, shells));
        }
    }

    public void getAllDaysFromDatabase()
    {

    }
}
