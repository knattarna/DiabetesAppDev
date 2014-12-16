package com.knattarna.androidapp.diabetesappdev.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.FragmentManager;

import android.content.DialogInterface;

import android.app.Activity;
import android.os.Bundle;

import android.text.Layout;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.Context;

public class MainActivity extends Activity {

    private static FragmentManager fragMan = null;

    //this should later be the week object global to the scope
    private static Week CURRENT_WEEK            = null;
    private static Day CURRENT_DAY              = null;
    private static SHELLActivity CURRENT_ACT    = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.CURRENT_WEEK = new Week(this);

        //read today from database
        this.CURRENT_WEEK.getDayFromDatabase();


        this.fragMan = getFragmentManager();
        this.CURRENT_DAY = this.CURRENT_WEEK.today();

        //set alarms only for today and only once every time the apps is started
        //because it's more manageable at the moment
        this.CURRENT_DAY.setAlarms();

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
        super.onBackPressed();
    }

    @Override
    public void onStop()
    {
        CURRENT_WEEK.writeDayToDatabase();
        super.onStop();
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
        private Button cancelAct        = null;

        //the current activity only changes on save
        //private static Activity curr_act = null;
        //the temporal activity changes withing the activity fragment
        private SHELLActivity temp_act = null;
        //static final int TIME_DIALOG_ID = 0;


        public ActivityFragment() {}

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                super.onActivityCreated(savedInstanceState);

                //making a copy
                this.temp_act = new SHELLActivity(
                        getActivity(),
                        CURRENT_ACT.getName(),
                        CURRENT_ACT.getInfo(),
                        CURRENT_ACT.getHour(),
                        CURRENT_ACT.getMin(),
                        CURRENT_ACT.getDate(),
                        CURRENT_ACT.getUniqueID(),
                        CURRENT_ACT.getBloodSLevel());

                saveAct     = (Button)   getActivity().findViewById(R.id.buttonSave);
                cancelAct   = (Button)   getActivity().findViewById(R.id.buttonCancel);
                displayTime = (TextView) getActivity().findViewById(R.id.textViewTimeDisplay);
                description = (EditText) getActivity().findViewById(R.id.editText);
                name        = (TextView) getActivity().findViewById(R.id.textViewName);
                blood       = (EditText) getActivity().findViewById(R.id.editText2);



                update();
                // Listener for events within the activity fragment
                name.setOnClickListener(new View.OnClickListener() {



                    public void onClick(View v) {
                        final EditText edtext = (EditText) getActivity().getLayoutInflater().inflate(R.layout.edit_text_dialog,null);
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Rename")
                                .setView(edtext)
                                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        temp_act.setName(edtext.getText().toString());
                                        update();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                    @Override
                                public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .show();
                    }
                });

                //set time
                displayTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       TimeDialog dia = new TimeDialog();
                       dia.show(getFragmentManager(), "timePicker");
                       update();
                    }
                });

                //edit text
                description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
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

                //cancel

                cancelAct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragMan.popBackStack();
                    }

                });

                //save changes
                saveAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //have we changed the time of the object ?
                        Boolean changed = (CURRENT_ACT.getHour() == temp_act.getHour()) && (CURRENT_ACT.getMin() == temp_act.getMin());

                        //get the time offset for changed time
                        Calendar offset = Calendar.getInstance();
                        int offset_hour = (temp_act.getHour() - CURRENT_ACT.getTime().get(Calendar.HOUR_OF_DAY));
                        int offset_min  = (temp_act.getMin() - CURRENT_ACT.getTime().get(Calendar.MINUTE));

                        //first set the values changed within ACTIVITY WINDOW

                        CURRENT_ACT.setName(temp_act.getName());
                        CURRENT_ACT.setInfo(temp_act.getInfo());
                        CURRENT_ACT.setTime(temp_act.getHour(),temp_act.getMin());
                        CURRENT_ACT.setDate(temp_act.getDate());
                        CURRENT_ACT.setUniqueID(temp_act.getUniqueID());
                        CURRENT_ACT.setBloodSLevel(temp_act.getBloodSLevel());


                        // set/reset the alarm for the changed or added activity
                        CURRENT_ACT.setAlarm();
                        //add activity if the current activity isn't a part of the day
                        //e.g clicked the add new activity button
                        if (!CURRENT_DAY.getDayActs().contains(CURRENT_ACT))
                        {
                            SHELLActivity tmp = new SHELLActivity(
                                    getActivity(),
                                    CURRENT_ACT.getName(),
                                    CURRENT_ACT.getInfo(),
                                    CURRENT_ACT.getHour(),
                                    CURRENT_ACT.getMin(),
                                    CURRENT_ACT.getDate(),
                                    CURRENT_ACT.getUniqueID(),
                                    CURRENT_ACT.getBloodSLevel());

                            CURRENT_DAY.addActivity(tmp);

                            CURRENT_DAY.sortActs();
                            fragMan.popBackStack();
                            return;
                        }

                        //only update if act is not done and time has changed value
                        if( !temp_act.getDone() && !changed) {
                            CURRENT_DAY.updateDay(CURRENT_DAY.getDayActs().indexOf(CURRENT_ACT), offset_hour, offset_min);
                        }

                        //sort the activities
                        CURRENT_DAY.sortActs();
                        //return to DAY WINDOW
                        fragMan.popBackStack();
                    }
                });

            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview = null;
            rootview = (View) inflater.inflate(R.layout.activity_window, container, false);
            return rootview;
        }

        /**
         * Updates the time in the TextView
         */
        private void update() {
            displayTime.setText(
                    new StringBuilder()
                            .append(pad(temp_act.getHour())).append(":")
                            .append(pad(temp_act.getMin())));
            description.setText(temp_act.getInfo());
            name.setText(temp_act.getName());
            blood.setText((String.valueOf(temp_act.getBloodSLevel())));
            CURRENT_ACT.isDone();
        }

        /**
         * Displays a notification when the time is updated
         */
        private void displayToast() {
            Toast.makeText(getActivity(), new StringBuilder().append("Time set to ").append(displayTime.getText()), Toast.LENGTH_SHORT).show();

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
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                cal.set(Calendar.MINUTE,minute);

                //if we edit in a day in the future anything goes
                if (cal.get(Calendar.DAY_OF_YEAR) < CURRENT_ACT.getDate())
                {
                    temp_act.setTime(hourOfDay, minute);
                }
                //cannot change to a time that has passed
                else if(System.currentTimeMillis() < cal.getTimeInMillis())
                {
                    temp_act.setTime(hourOfDay, minute);
                }

                ActivityFragment.this.update();
                ActivityFragment.this.displayToast();
            }

        }

    }

    /**
     * the day window fragment
     * =============================================================================================
     */

    public static class DayFragment extends Fragment {

        private ArrayList<String> activity_names = new ArrayList<String>() {};

        //get the alarm manager
        private AlarmManager ALARM = null;

        private ListView act_list   = null;
        private Button ret_today    = null;
        private Button ret_week     = null;
        private Button add_act      = null;
        private TextView day_name   = null;

        public DayFragment() {
            super();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            update();



            ret_week.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeekFragment week = new WeekFragment();// Create new fragment and transaction
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.container, week);
                    fragTrans.addToBackStack(null);
                    // Commit the transaction
                    fragTrans.commit();
                }
            });

            ret_today.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENT_DAY = CURRENT_WEEK.today();
                    update();
                }
            });

            add_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CURRENT_ACT = new SHELLActivity(getActivity(),CURRENT_DAY.getDate());
                    update();

                    ActivityFragment act = new ActivityFragment();
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.container,act);
                    fragTrans.addToBackStack(null);
                    fragTrans.commit();
                }
            });
        }

        public void update()
        {
            act_list    = (ListView) getActivity().findViewById(R.id.actList);
            day_name    = (TextView) getActivity().findViewById(R.id.dayName);
            ret_week    = (Button) getActivity().findViewById(R.id.buttonWeek);
            ret_today   = (Button) getActivity().findViewById(R.id.returnToday);
            add_act     = (Button) getActivity().findViewById(R.id.addAct);

            activity_names.clear();
            CURRENT_DAY.sortActs();
            for (int i = 0; i < CURRENT_DAY.getDayActs().size(); ++i) {
                //re add the sorted names the the list
                activity_names.add(CURRENT_DAY.getDayActs().get(i).getName());
                //update the activities to see if they are completed
                CURRENT_DAY.getDayActs().get(i).isDone();
            }

            act_list.setAdapter(new DayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_act,
                    R.id.act_name,
                    activity_names));

            day_name.setText(CURRENT_DAY.getDayOfTheWeek());

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview = null;
            rootview = (View) inflater.inflate(R.layout.day_window, container, false);
            return rootview;
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
        public View getView(final int position, View convertView, ViewGroup parent)
        {
           //return super.getView(position, convertView, parent); View row = convertView;

           View row = convertView;

           if(row==null)
           {
               LayoutInflater inflater = LayoutInflater.from(context);
               row=inflater.inflate(R.layout.list_item_act, parent, false);
           }

           TextView text = (TextView) row.findViewById(R.id.act_name);
           TextView time = (TextView) row.findViewById(R.id.act_time);
           CheckBox box = (CheckBox) row.findViewById(R.id.checkBoxMeal);

           text.setText(CURRENT_DAY.getDayActs().get(position).getName());
           time.setText(
                   new StringBuilder()
                           .append(pad(CURRENT_DAY.getDayActs().get(position).getHour())).append(":")
                           .append(pad(CURRENT_DAY.getDayActs().get(position).getMin())));

            box.setChecked(CURRENT_DAY.getDayActs().get(position).getDone());

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CURRENT_ACT = CURRENT_DAY.getDayActs().get(position);
                    ActivityFragment act = new ActivityFragment();// Create new fragment and transaction
                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    FragmentTransaction fragTrans = fragMan.beginTransaction();
                    fragTrans.replace(R.id.container, act);
                    fragTrans.addToBackStack(null);
                    // Commit the transaction
                    fragTrans.commit();
                }
            });

           return  row;
        }

    }

    /**
     * the week window fragment
     * =============================================================================================
     */

    public static class WeekFragment extends Fragment {

        private ListView days           = null;
        private TextView week_number    = null;

        private ArrayList<String> day_names = new ArrayList<String>(){};

        public WeekFragment()
        {
            super();
            for ( int i = 0; i < CURRENT_WEEK.getDays().size(); ++i)
            {
                day_names.add(CURRENT_WEEK.getDays().get(i).getDayOfTheWeek());
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);


            week_number = (TextView) getActivity().findViewById(R.id.weekNumber);
            days        = (ListView) getActivity().findViewById(R.id.day_list);

            Calendar cal = Calendar.getInstance();
            week_number.setText(Integer.toString(cal.get(Calendar.WEEK_OF_YEAR)));

            days.setAdapter(new WeekAdapter<String>(
                    getActivity(),
                    R.layout.list_item_day,
                    R.id.day_name,
                    day_names));
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview = null;
            rootview = (View) inflater.inflate(R.layout.week_window, container, false);
            return rootview;
        }
    }

    //View editing within the rows of the DayFragment class is done via the ArrayAdapter getView method
    public static class WeekAdapter<String> extends ArrayAdapter<String>
    {

        private Context context = null;
        public WeekAdapter(Context context, int resource, int textViewResourceId, List<String> objects)
        {
            super(context, resource, textViewResourceId, objects);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            //return super.getView(position, convertView, parent); View row = convertView;

            View row = convertView;

            if(row == null)
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                row=inflater.inflate(R.layout.list_item_day, parent, false);
            }

            TextView text = (TextView) row.findViewById(R.id.day_name);
            TextView date = (TextView) row.findViewById(R.id.day_date);

            text.setText(CURRENT_WEEK.getDays().get(position).getDayOfTheWeek());
            date.setText(CURRENT_WEEK.getDays().get(position).getDate().getDisplayName(Calendar.DATE,Calendar.LONG, Locale.US));

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENT_DAY = CURRENT_WEEK.getDays().get(position);
                    fragMan.popBackStack();
                }
            });

            return  row;
        }
    }
}
