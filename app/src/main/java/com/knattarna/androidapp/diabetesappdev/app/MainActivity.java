package com.knattarna.androidapp.diabetesappdev.app;

import android.app.AlarmManager;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;

import android.app.TimePickerDialog;

import android.view.inputmethod.EditorInfo;
import android.support.v7.app.ActionBarActivity;

import android.app.Fragment;
import android.os.Bundle;

import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;

import android.content.Intent;
import android.content.Context;
import android.widget.CheckBox;


import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import static android.widget.Toast.makeText;


public class MainActivity extends ActionBarActivity {

    //tags for the fragments
    private static final String TAG_DAY = "DAY";

    //this should later be the week object global to the scope
    private static Day CURRENT_DAY      = new Day();
    private static Activity CURRENT_ACT = new Activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if there is no previously saved instance of the app
        //add a fragment to the layout
        if (savedInstanceState == null) {
            //add an initial fragment to main window
            DayFragment day = new DayFragment();
            getFragmentManager().beginTransaction().add(R.id.container, day).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        //this works buuut normal behaviour isn't there
        getFragmentManager().popBackStack();
    }


    /**
     * Helper functions
     */


    //Add padding to numbers less than ten
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    /**
     * activity fragment
     * =============================================================================================
     */
    public static class ActivityFragment extends Fragment {


        private Button saveAct          = null;
        private TextView displayTime    = null;
        private TextView name           = null;
        private EditText description    = null;
        private EditText blood          = null;

        //get the alarm manager
        private AlarmManager ALARM = null;
        //the current activity only changes on save
        //private static Activity curr_act = null;
        //the temporal activity changes withing the activity fragment
        private static Activity temp_act = null;
        //static final int TIME_DIALOG_ID = 0;

        public ActivityFragment() {

            //making a copy
            this.temp_act = new Activity(
                    CURRENT_ACT.getName(),
                    CURRENT_ACT.getHour(),
                    CURRENT_ACT.getMin(),
                    CURRENT_ACT.getInfo(),
                    CURRENT_ACT.getBloodSLevel(),false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                super.onActivityCreated(savedInstanceState);

                saveAct     = (Button)   getActivity().findViewById(R.id.buttonSave);
                displayTime = (TextView) getActivity().findViewById(R.id.textViewTimeDisplay);
                description = (EditText) getActivity().findViewById(R.id.editText);
                name        = (TextView) getActivity().findViewById(R.id.textViewName);
                blood       = (EditText) getActivity().findViewById(R.id.editText2);
                // Listener for events within the activity fragment

                //set time
                displayTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       TimeDialog dia = new TimeDialog();
                       dia.show(getFragmentManager(), "timePicker");
                       updateDisplay();
                    }
                });

                //edit text
                description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            temp_act.setInfo(description.getText().toString());
                        }
                        return false;
                    }
                });

                //edit text
                blood.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                           try {
                               temp_act.setBloodSLevel(Integer.parseInt(blood.getText().toString()));
                           }catch (NumberFormatException e)
                            {
                                return false;
                            }
                          }
                        return false;
                    }
                });

                //save changes
                saveAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //have we changed the time of the object ?
                        Boolean test = (CURRENT_ACT.getHour() == temp_act.getHour()) && (CURRENT_ACT.getMin() == temp_act.getMin());

                        //get the time offset for changed time
                        Calendar offset = Calendar.getInstance();

                        offset.set(Calendar.HOUR_OF_DAY,(temp_act.getHour() - CURRENT_ACT.getTime().get(Calendar.HOUR_OF_DAY)));
                        offset.set(Calendar.MINUTE, (temp_act.getMin() - CURRENT_ACT.getTime().get(Calendar.MINUTE)));

                        //java is weird cannot do CURRENT_ACT = temp_act
                        CURRENT_ACT.setInfo(temp_act.getInfo());
                        CURRENT_ACT.setName(temp_act.getName());
                        CURRENT_ACT.setTime(temp_act.getHour(),temp_act.getMin());
                        CURRENT_ACT.setBloodSLevel(temp_act.getBloodSLevel());


                        //reset an alarm for this activity (should identify the same PendingIntent
                        ALARM = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                        CURRENT_ACT.setAlarmIntent(getActivity().getBaseContext(),new Intent(getActivity(),AlarmReceiver.class));

                        ALARM.set(AlarmManager.RTC_WAKEUP, CURRENT_ACT.getTime().getTimeInMillis(),
                                CURRENT_ACT.getAlarmIntent());


                        //only update if act is not done and time has changed value
                        if( !temp_act.getDone() && !test) {
                            CURRENT_DAY.updateDay(CURRENT_DAY.getDayActs().indexOf(CURRENT_ACT), offset);
                        }

                        //OMG i found the answer
                        getFragmentManager().popBackStack();
                        /*DayFragment day = new DayFragment();
                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
                        fragTrans.replace(R.id.container,day);
                        fragTrans.addToBackStack(null);
                        // Commit the transaction
                        fragTrans.commit();*/
                    }
                });

            }
            updateDisplay();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview = null;
            rootview = (View) inflater.inflate(R.layout.activity_activity_window, container, false);
            return rootview;
        }

        /**
         * Updates the time in the TextView
         */
        private void updateDisplay() {
            displayTime.setText(
                    new StringBuilder()
                            .append(pad(temp_act.getHour())).append(":")
                            .append(pad(temp_act.getMin())));
            description.setText(temp_act.getInfo());
            name.setText(temp_act.getName());
            blood.setText((String.valueOf(temp_act.getBloodSLevel())));
        }

        /**
         * Displays a notification when the time is updated
         */
        private void displayToast() {
            Toast.makeText(getActivity(), new StringBuilder().append("Time choosen is ").append(displayTime.getText()), Toast.LENGTH_SHORT).show();

        }

        //says it should be static, however the Outerclass.this.function() doesnt work in static context
        public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener
        {
            public TimeDialog()
            {
                super();
            }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState)
            {
                return new TimePickerDialog(getActivity(),this,
                        temp_act.getHour(),
                        temp_act.getMin(),
                        DateFormat.is24HourFormat(getActivity()));
            }

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,int minute)
            {
                temp_act.setTime(hourOfDay,minute);
                ActivityFragment.this.updateDisplay();
                ActivityFragment.this.displayToast();
            }

        }

    }

    /**
     * the day window fragment
     * =============================================================================================
     */

    public static class DayFragment extends ListFragment {


        private ArrayList<String> activity_names = new ArrayList<String>(){};

        public DayFragment()
        {
            super();

            for(int i = 0; i < CURRENT_DAY.getDayActs().size(); ++i)
            {
                activity_names.add(CURRENT_DAY.getDayActs().get(i).getName());
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new DayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_meal,
                    R.id.list_item_meal,
                    activity_names));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {

            CURRENT_ACT = CURRENT_DAY.getDayActs().get(position);
            ActivityFragment act = new ActivityFragment();// Create new fragment and transaction

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
            fragTrans.replace(R.id.container, act, TAG_DAY);
            fragTrans.addToBackStack(null);
            // Commit the transaction
            fragTrans.commit();
        }
    }

    //View editing within the rows of the DayFragment class is done via the ArrayAdapter getView method
    public static class DayAdapter<String> extends ArrayAdapter<String>
    {

        private Context context = null;
        public DayAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
        {
            super(context, resource, textViewResourceId, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
           //return super.getView(position, convertView, parent); View row = convertView;

           View row = convertView;

           if(row==null)
           {
               LayoutInflater inflater = LayoutInflater.from(context);
               row=inflater.inflate(R.layout.list_item_meal, parent, false);
           }

           TextView text = (TextView) row.findViewById(R.id.list_item_meal);
           TextView time = (TextView) row.findViewById(R.id.list_item_time);
           CheckBox box = (CheckBox) row.findViewById(R.id.checkBoxMeal);

           text.setText(CURRENT_DAY.getDayActs().get(position).getName());
           time.setText(
                   new StringBuilder()
                           .append(pad(CURRENT_DAY.getDayActs().get(position).getHour())).append(":")
                           .append(pad(CURRENT_DAY.getDayActs().get(position).getMin())));

            box.setChecked(CURRENT_DAY.getDayActs().get(position).getDone());

           return  row;
        }
    }
}
