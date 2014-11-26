package com.knattarna.androidapp.diabetesappdev.app;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ListFragment;

import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;



public class MainActivity extends ActionBarActivity {

    //this should later be the week object global to the scope
    private static Day today = new Day();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if there is no previously saved instance of the app
        //add a fragment to the layout
        if (savedInstanceState == null) {
            //add an initial fragment to main window
            DayFragment day = new DayFragment(today.getDayActs());
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


    /**
     * activity fragment
     * =============================================================================================
     */
    public static class ActivityFragment extends Fragment {

        private Button pickTime = null;
        private Button saveAct = null;
        private TextView displayTime = null;
        private Activity curr_act = new Activity("Frukost", 13, 37);
        static final int TIME_DIALOG_ID = 0;

        public ActivityFragment(Activity act) {
            super();

            if(act != null)
                this.curr_act = act;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            if (savedInstanceState == null) {
                super.onActivityCreated(savedInstanceState);

                pickTime = (Button) getActivity().findViewById(R.id.buttonChangeTIme);
                saveAct = (Button) getActivity().findViewById(R.id.buttonSave);
                displayTime = (TextView) getActivity().findViewById(R.id.textViewTimeDisplay);

                // Listener for click event of the button
                pickTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().showDialog(TIME_DIALOG_ID);
                    }
                });

                saveAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DayFragment day = new DayFragment(today.getDayActs());
                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
                        fragTrans.replace(R.id.container, day);
                        fragTrans.addToBackStack(null);
                        // Commit the transaction
                        fragTrans.commit();
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
         * Add padding to numbers less than ten
         */
        private static String pad(int c) {
            if (c >= 10)
                return String.valueOf(c);
            else
                return "0" + String.valueOf(c);
        }

        /**
         * Updates the time in the TextView
         */
        private void updateDisplay() {
            displayTime.setText(
                    new StringBuilder()
                            .append(pad(curr_act.getHour())).append(":")
                            .append(pad(curr_act.getMin())));

        }
        /** Create a new dialog for time picker */

        /**
         * Displays a notification when the time is updated
         */
        private void displayToast() {
            Toast.makeText(getActivity(), new StringBuilder().append("Time choosen is ").append(displayTime.getText()), Toast.LENGTH_SHORT).show();

        }


        /**
         * Callback received when the user "picks" a time in the dialog
         */
        private TimePickerDialog.OnTimeSetListener mTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        curr_act.setTime(hourOfDay, minute);
                        updateDisplay();
                        displayToast();
                    }
                };

        protected Dialog onCreateDialog(int id) {
            switch (id) {
                case TIME_DIALOG_ID:
                    return new TimePickerDialog(getActivity(),
                            mTimeSetListener, curr_act.getHour(), curr_act.getMin(), true);
            }
            return null;
        }

    }

    /**
     * the day window fragment
     * =============================================================================================
     */

    public static class DayFragment extends ListFragment {

        private ArrayList<Activity> activities = null;
        private ArrayList<String> activity_names = new ArrayList<String>(){};

        public DayFragment(ArrayList<Activity> acts) {
            super();

            this.activities = acts;
            //initialize the array of activities
            for(int i = 0; i < activities.size(); ++i)
            {
                activity_names.add(activities.get(i).getName());
            }
                //this.activity_names.add(activities.get(i).getName().toString());

        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    R.layout.list_item_meal,
                    R.id.list_item_meal,
                    activity_names));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            makeText(getActivity(), "Clicked me!", Toast.LENGTH_SHORT).show();
            ActivityFragment act = new ActivityFragment(activities.get(position));// Create new fragment and transaction

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
            fragTrans.replace(R.id.container, act);
            fragTrans.addToBackStack(null);
            // Commit the transaction
            fragTrans.commit();
        }
    }
}
