package com.knattarna.androidapp.diabetesappdev.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import java.util.ArrayList;
import java.util.TooManyListenersException;


public class MainActivity extends ActionBarActivity {

    private Day ToDaysActivitys = new Day();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if there is no previously saved instance of the app
        //add a fragment to the layout
        if (savedInstanceState == null)
        {


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
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
    *   Extending the ArrayAdapter to override getView() method
    *   for custom views.
    */

    //todo change List<String> to Objects[] objects for Activity Objects or smthing


    public static class ActivityAdapter extends ArrayAdapter {

        private Context context;
        private int resourceid;
        private int layoutid;
        private List<String> objects;

        public ActivityAdapter (Context ctext, int layoutid, int resourceid, List<String> objects) {
            super(ctext, layoutid, resourceid, objects);

            this.context = ctext;
            this.resourceid = resourceid;
            this.layoutid = layoutid;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder;
            View row = convertView; //current list-item maybe Iduntknow

            LayoutInflater inflater = ((android.app.Activity) this.context).getLayoutInflater();

            if (row == null)
            {
                //inflates the xml view list
                row = inflater.inflate(this.layoutid, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView)row.findViewById(R.id.list_item_meal);

                row.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)row.getTag();
            }

            //set the values of the inner views
            holder.text.setText(objects.get(position));

            //add onClick to the textView
            holder.text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //holder.text.setText("You bastard, you clicked me!");
                    Toast.makeText(context, "Clicked me!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ActivityWindow.class);
                                                context.startActivity(intent);

                }
               });




            return row;
        }

        /**
         *  Inner class that holds the views within the layout.
         *  This means we don't need to call findViewById more than once
         *  every inflate and we can easily modify fields of the inner views
         */
        private static class ViewHolder {
            public TextView text;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
            //super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ArrayList<String> meals = new ArrayList<String>() {{
                add("Breakfast");
                add("Brunch");
                add("Lunch");
                add("Snack");
                add("Dinner");
                add("Breakfast");
                add("Brunch");
                add("Lunch");
                add("Snack");
                add("Dinner");
                add("Breakfast");
                add("Brunch");
                add("Lunch");
                add("Snack");
                add("Dinner");
            }};

            ActivityAdapter adapter = new ActivityAdapter(
                    getActivity(),
                    R.layout.list_item_meal,
                    R.id.list_item_meal,
                    meals);

            ListView list = (ListView) rootView.findViewById(R.id.listView_meals);
            list.setAdapter(adapter);
            return rootView;
        }
    }
}
