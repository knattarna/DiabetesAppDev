package com.knattarna.androidapp.diabetesappdev.app;

import java.util.ArrayList;

/**
 * Created by mrbent on 12/3/14.
 */
public class Week {

    private ArrayList<Day> days = new ArrayList<Day>();

    public Week()
    {
        days.add(new Day());
        days.add(new Day());
    }

    public ArrayList<Day> getDays() {return days;}

    public Day today() {return days.get(0);}
}
