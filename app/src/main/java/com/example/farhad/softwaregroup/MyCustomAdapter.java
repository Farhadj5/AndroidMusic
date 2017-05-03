package com.example.farhad.softwaregroup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.id;
import static android.R.id.toggle;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Farhad on 5/2/2017.
 */

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private AlarmManager alarmManager = null;
    private ArrayList<Calendar> calendarArray = new ArrayList<Calendar>();
    private static boolean togglecheck = true;

    public MyCustomAdapter(ArrayList<String> list, Context context,AlarmManager alarmmanager, ArrayList<Calendar> calendar) {
        this.list = list;
        this.context = context;
        this.alarmManager = alarmmanager;
        this.calendarArray = calendar;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarmlist, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));


        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        ToggleButton toggleBtn = (ToggleButton)view.findViewById(R.id.toggle_btn);
        if (togglecheck)
            toggleBtn.setChecked(true);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("DELETE" + position);
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, position, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent.cancel();
                alarmManager.cancel(pendingIntent);
                calendarArray.remove(position);
                list.remove(position);
                notifyDataSetChanged();

                //do something
                //list.remove(position); //or some other task
                //notifyDataSetChanged();
            }
        });
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, "Alarm On!",
                            Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, position, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.set(AlarmManager.RTC, calendarArray.get(position).getTimeInMillis(), pendingIntent);
                } else {
                    togglecheck = false;
                    Toast.makeText(context, "Alarm Off!",
                            Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(context, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, position, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    alarmManager.cancel(pendingIntent);
                }
            }
        });

        return view;
    }
}