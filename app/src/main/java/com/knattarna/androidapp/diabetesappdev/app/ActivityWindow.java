package com.knattarna.androidapp.diabetesappdev.app;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;



public class ActivityWindow extends ActionBarActivity {

    /** Private members of the class */
    private TextView displayTime;

    private Activity act = new Activity("Frukost", 13,37);
    //private int pHour;
    //private int pMinute;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;

    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    act.setTime(hourOfDay, minute);
                    updateDisplay();
                    displayToast();
                }
            };

    /** Updates the time in the TextView */
    private void updateDisplay() {
        displayTime.setText(
                new StringBuilder()
                        .append(pad(act.getHour())).append(":")
                        .append(pad(act.getMin())));
    }

    /** Displays a notification when the time is updated */
    private void displayToast() {
        Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(displayTime.getText()), Toast.LENGTH_SHORT).show();

    }

    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_window);

        /** Capture our View elements */
        displayTime = (TextView) findViewById(R.id.textViewTimeDisplay);
        Button pickTime = (Button) findViewById(R.id.buttonChangeTIme);
        Button SaveAct = (Button) findViewById(R.id.buttonSave);
        /** Listener for click event of the button */

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        SaveAct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i= new Intent(ActivityWindow.this, MainActivity.class);

                startActivity(i);
            }
        });


        /** Display the current time in the TextView */
        updateDisplay();
    }

    /** Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, act.getHour(), act.getMin(), true);
        }
        return null;
    }

}
