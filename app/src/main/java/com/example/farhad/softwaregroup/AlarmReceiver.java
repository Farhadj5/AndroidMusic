package com.example.farhad.softwaregroup;

/**
 * Created by farhad on 4/24/2017.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;


import java.util.concurrent.TimeUnit;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    //called when alarm is scheduled to go off.
    public void onReceive(final Context context, Intent intent) {

        //this will sound the alarm tone
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        MainActivity.alarmBool = true;
        //THIS IS HOW WE OPEN THE MAIN ACTIVITY WHEN THE ALARM GOES OFF

        Intent i = new Intent();
        i.setClassName("com.example.farhad.softwaregroup", "com.example.farhad.softwaregroup.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

        //THIS JUST PLAYS RINGTONE. COMMENT OUT FOR STREAM MUSIC
        //ringtone.play();

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);

        //another little notification to be more annoying
        Toast toast = Toast.makeText(context, "ALARM!!!", Toast.LENGTH_LONG);
        toast.show();

        //lets the ringtone go about 3 times before stopping
        try{
            TimeUnit.SECONDS.sleep(6);
        }catch(InterruptedException e){
            Log.d("couldnt run","couldnt sleep");
        }

        //ends ringtone
        //ringtone.stop();
    }
}