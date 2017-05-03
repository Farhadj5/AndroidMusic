package com.example.farhad.softwaregroup;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener{
        //implements NavigationView.OnNavigationItemSelectedListener {
        private static final String PARSE_URL = "http://162.243.192.229/parse.php";
        private String artist;
        private String title;
        private boolean mute = false;
        private boolean called = false;
        private boolean tracked = false;
        private ToggleButton toggleButton1, toggleButton2;
        private Button btnDisplay;
        private int savedVol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            MediaPlayer player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource("http://162.243.192.229:8080/example1.ogg");
            player.setOnPreparedListener(this);
            player.prepareAsync();
        }catch(Exception e){

        }
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/



        ImageButton btn = (ImageButton) findViewById(R.id.alarmButton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
            }
        });

        Button infoButton = (Button) findViewById(R.id.info);

        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showInfo();
            }
        });

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        addListenerOnButton();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volSlider(curVolume);

    }
    public void volSlider(int passedVol) {
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        SeekBar volControl = (SeekBar) findViewById(R.id.volbar);
        volControl.setMax(maxVolume);

        if(called && !mute){
            volControl.setProgress(0);
            called = false;
        } else if(tracked){
            volControl.setProgress(savedVol);
        } else {
            volControl.setProgress(passedVol);
        }

        volControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                savedVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                tracked = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                mute = false;
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, arg1, 0);
            }
        });
    }
    public void addListenerOnButton() {

        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

                called = true;
                volSlider(curVolume);
                StringBuffer result = new StringBuffer();

                int maxVol = 0;
               // Toast.makeText(MainActivity.this, result.toString(),Toast.LENGTH_SHORT).show();
                if(mute){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(maxVol), 0);
                    mute = false;
                } else {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    mute = true;
                }
            }

        });
    }

    public void showInfo(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, PARSE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Display Info", Toast.LENGTH_LONG).show();
                        String JSON_ARRAY = "result";
                        artist = "";
                        title = "";
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("info");
                            JSONObject songArtist = result.getJSONObject(0);
                            JSONObject songTitle = result.getJSONObject(1);
                            artist = songArtist.getString("artist");
                            title = songTitle.getString("title");

                            Toast.makeText(MainActivity.this,"Artist: " + artist + " Title: " + title, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //modified what back button does
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //if (drawer.isDrawerOpen(GravityCompat.START)) {
        //    drawer.closeDrawer(GravityCompat.START);
        //} else {
            Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginActivityIntent);
        //}
    }

    //This creates the top right menu, 3 dots
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void onPrepared(MediaPlayer player){
        player.start();
    }



/*
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

}
