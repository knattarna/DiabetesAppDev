package com.knattarna.androidapp.diabetesappdev.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by mrbent on 12/10/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String TABLE_NAME               = "ACTIVITIES";

    public static final String COLUMN_NAME_ENTRY_ID     = "id";                 //1
    public static final String COLUMN_NAME_TITLE        = "name";               //2
    public static final String COLUMN_NAME_INFO         = "info";               //3
    public static final String COLUMN_NAME_DONE         = "actionDone";         //4
    public static final String COLUMN_NAME_TIME         = "datetime";           //5

    private static final String TEXT_TYPE   = " TEXT";
    private static final String INT_TYPE    = " INTEGER";
    private static final String COMMA_SEP   = ",";



    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    _ID + " INTEGER PRIMARY KEY," +                             //0
                    COLUMN_NAME_ENTRY_ID + INT_TYPE + COMMA_SEP +               //1
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +                 //2
                    COLUMN_NAME_INFO + TEXT_TYPE + COMMA_SEP +                  //3
                    COLUMN_NAME_DONE + INT_TYPE + COMMA_SEP +                   //4
                    COLUMN_NAME_TIME + INT_TYPE +                               //5
                    " );";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;



    /*
        ============================================================================
     */
    private Context context = null;
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "KnattarnasDatabasOnYourPhone.db";


    public DatabaseHelper (Context context)
    {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //write all current activities to the database
    public void writeActivity(SHELLActivity act)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME_ENTRY_ID,act.getUniqueID());
        contentValues.put(COLUMN_NAME_TITLE,act.getName());
        contentValues.put(COLUMN_NAME_INFO,act.getInfo());
        contentValues.put(COLUMN_NAME_DONE,act.getDone()?1:0);
        contentValues.put(COLUMN_NAME_TIME,act.getTime().getTimeInMillis());

       /* System.out.println("writing to db \n"
                + act.getName() + '\n'
                + act.getInfo() + '\n'
                + act.getHour() + " : "
                + act.getMin() + '\n'
                + act.getDate() + '\n'
                + act.getUniqueID() + '\n'
                + "=====================");
        */
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }


    //all fetch and write operations on the database

    public ArrayList<SHELLActivity> getAllActivities()
    {
        ArrayList<SHELLActivity> activities = new ArrayList<SHELLActivity>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SHELLActivity act = new SHELLActivity(
                        context,
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getLong(5),
                        cursor.getInt(1)
                );
                activities.add(act);
                System.out.println("reading from db " + cursor.getInt(5));
        /*
                System.out.println("reading from db \n"
                        + act.getName() + '\n'
                        + act.getInfo() + '\n'
                        + act.getHour() + " : "
                        + act.getMin() + '\n'
                        + act.getDate() + '\n'
                        + act.getUniqueID() + '\n'
                        + "=====================");
          */
            } while (cursor.moveToNext());
        }

        return activities;
    }



    public void clearTable()
    {
        String deleteQuery = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(deleteQuery);
    }

}
