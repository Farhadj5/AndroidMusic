package com.example.farhad.softwaregroup;

/**
 * Created by farhad on 4/24/2017.
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import 	android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;


import java.util.ArrayList;
import java.util.Calendar;

import static com.example.farhad.softwaregroup.R.id.alarmDatePicker;
import static com.example.farhad.softwaregroup.R.id.listView;

public class AlarmActivity extends Activity {
    private int alarmcount = 0;
    AlarmManager alarmManager;
    private ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
    private TimePicker alarmTimePicker;
    private DatePicker alarmDatePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView;
    private ViewFlipper viewFlipper;

    //alarm list
    private Calendar calendar = Calendar.getInstance();
    private int year,month,day,hour,minute;
    private final ArrayList<String> alarms = new ArrayList<String>();
    ListView listview;

    //no idea
    public static AlarmActivity instance() {
        return inst;
    }

    //no idea
    public void onStart() {
        super.onStart();
        inst = this;
    }

    //sets variables.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmDatePicker = (DatePicker) findViewById(R.id.alarmDatePicker);
        Button alarmSet = (Button) findViewById(R.id.alarmSet);
        Button create = (Button) findViewById(R.id.createAlarm);
        Button dateSet = (Button) findViewById(R.id.alarmDateSet);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //alarm list
        listview = (ListView)findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, alarms);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // ListView Clicked item index
                int itemPosition     = position;


                // ListView Clicked item value
                String  itemValue    = (String) listview.getItemAtPosition(position);


                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_SHORT)
                        .show();

            }

        });
    }


    //handles the toggle button code.
    public void onSetClicked(View view) {
            year = alarmDatePicker.getYear();
            month = alarmDatePicker.getMonth();
            day = alarmDatePicker.getDayOfMonth();
            hour = alarmTimePicker.getCurrentHour();
            minute = alarmTimePicker.getCurrentMinute();
            calendar.set(year,month,day,hour,minute);
            //System.out.println("millis: " + calendar.getTime());
            Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            if (alarms.isEmpty())
                alarmcount = 0;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, alarmcount, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            intentArray.add(pendingIntent);
            alarmcount++;
            viewFlipper.showNext();
            Toast.makeText(getApplicationContext(), "Alarm is Set!",
                Toast.LENGTH_SHORT).show();
            alarms.add(calendar.getTime().toString());
            //TURN ALARM OFF
            //alarmManager.cancel(pendingIntent);


    }
    public void onCreateClicked(View view) {
        //System.out.println("CREATING ALARM");
        viewFlipper.showNext();}
    public void onDateClicked(View view){
        day = alarmDatePicker.getDayOfMonth();
        month = alarmDatePicker.getMonth();
       // System.out.println("DAY OF MONTH: " + day + "month " + month);
        viewFlipper.showNext();
        }


    //What to do when the back button is pressed. Sends to the main screen
    public void onBackPressed(){
        Intent prevActivityIntent = new Intent(AlarmActivity.this, MainActivity.class);
        startActivity(prevActivityIntent);
    }

}