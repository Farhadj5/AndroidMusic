package com.example.farhad.softwaregroup;

/**
 * Created by farhad on 4/24/2017.
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmActivity extends Activity {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView;

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
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


    }

    //handles the toggle button code.
    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");
            setAlarmText("Alarm is ON");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent myIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, 0);
            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            setAlarmText("Alarm is OFF");
            Log.d("MyActivity", "Alarm Off");
        }
    }

    //sets the text underneath the alarm
    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    //What to do when the back button is pressed. Sends to the main screen
    public void onBackPressed(){
        Intent prevActivityIntent = new Intent(AlarmActivity.this, MainActivity.class);
        startActivity(prevActivityIntent);
    }

}