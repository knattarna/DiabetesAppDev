package com.knattarna.androidapp.diabetesappdev.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.app.FragmentManager;

import android.content.DialogInterface;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;

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

import android.content.Intent;
import android.content.Context;




public class MainActivity extends Activity {

    private static FragmentManager fragMan = null;

    //this should later be the week object global to the scope
    private static Week CURRENT_WEEK    = new Week();
    private static Day CURRENT_DAY      = null;
    private static SHELLActivity CURRENT_ACT = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.fragMan = getFragmentManager();
        this.CURRENT_DAY = this.CURRENT_WEEK.today();

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
        private SHELLActivity temp_act = null;
        //static final int TIME_DIALOG_ID = 0;

        public ActivityFragment() {

            //making a copy
            this.temp_act = new SHELLActivity(
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

                update();
                // Listener for events within the activity fragment
                name.setOnClickListener(new View.OnClickListener() {

                    final EditText edtext = new EditText(getActivity());

                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Byt namn")
                                .setView(edtext)
                                .setPositiveButton("Ändra", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int whichButton) {
                                        temp_act.setName(edtext.getText().toString());
                                        update();
                                    }
                                })
                                .setNegativeButton("Avbryt", new DialogInterface.OnClickListener(){
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
                        //if time has changed on a completed activity sort the day
                        CURRENT_DAY.sortActs();
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

        private ArrayList<String> activity_names = new ArrayList<String>() {
        };
        private ListView act_list   = null;
        private Button ret_today    = null;
        private Button day_name     = null;
        private Button add_act      = null;

        public DayFragment() {
            super();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            this.update();

            day_name.setOnClickListener(new View.OnClickListener() {
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
                    DayFragment.this.update();
                }
            });

            add_act.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENT_ACT = new SHELLActivity();
                    CURRENT_DAY.addActivity(CURRENT_ACT);

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
            day_name    = (Button) getActivity().findViewById(R.id.dayName);
            ret_today   = (Button) getActivity().findViewById(R.id.returnToday);
            add_act     = (Button) getActivity().findViewById(R.id.addAct);

            activity_names.clear();
            CURRENT_DAY.sortActs();
            for (int i = 0; i < CURRENT_DAY.getDayActs().size(); ++i) {
                activity_names.add(CURRENT_DAY.getDayActs().get(i).getName());
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

            if(row==null)
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                row=inflater.inflate(R.layout.list_item_day, parent, false);
            }

            TextView text = (TextView) row.findViewById(R.id.day_name);

            text.setText(CURRENT_WEEK.getDays().get(position).getDayOfTheWeek());

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
