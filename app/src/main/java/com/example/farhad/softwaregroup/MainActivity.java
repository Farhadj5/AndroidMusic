package com.example.farhad.softwaregroup;

import android.provider.Settings.Secure;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.Settings;
import android.view.MotionEvent;
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
import android.widget.RatingBar;
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
        private static final String RATING_URL = "http://162.243.192.229/rating.php";
        private String artist;
        private String title;
        public static boolean mute = false;
        public static boolean called = false;
        private boolean tracked = false;
        public static boolean alarmBool;
        private ToggleButton toggleButton1, toggleButton2;
        private Button btnDisplay;
        private Button btnSubmit;
        private static int savedVol;
        public RatingBar ratingBar;
        private float userRating;
        private String stringRating;
        public static final String KEY_RATING = "rating";
        private MediaPlayer player = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmBool = false;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource("http://162.243.192.229:8080/example1.ogg");
            player.setOnPreparedListener(this);
            player.prepareAsync();
        }catch(Exception e){

        }

        ImageButton btn = (ImageButton) findViewById(R.id.alarmButton);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AlarmActivity.class));
            }
        });
        Button b2 = (Button) findViewById(R.id.newsfeed);
        b2.setOnClickListener(new View.OnClickListener() {
                                  public void onClick(View v) {
                                      startActivity(new Intent(MainActivity.this, NewsFeed.class));
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
        addListenerOnSubmitButton();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volSlider(curVolume);

        addListenerOnRatingBar();

    }
    public void onResume(){
        super.onResume();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mute = false;
        if(alarmBool){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        }
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);


        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                userRating = ratingBar.getRating();
                if(userRating == 1){
                    stringRating = "1";
                } else if(userRating == 2){
                    stringRating = "2";
                } else if(userRating == 3){
                    stringRating = "3";
                } else if(userRating == 4){
                    stringRating = "4";
                } else if(userRating == 5){
                    stringRating = "5";
                }
            }
        });
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

    public void addListenerOnSubmitButton() {
        btnSubmit = (Button) findViewById(R.id.submit);

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rateSong();
            }
        });
    }

    public void rateSong(){
        final String rating = stringRating;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RATING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,"Rating Saved!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Please select a star before pressing submit!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_RATING,rating);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                        //Toast.makeText(MainActivity.this,"Display Info", Toast.LENGTH_LONG).show();
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
                            title = title.trim();
                            Toast.makeText(MainActivity.this,"Artist: " + artist + "\nTitle: " + title, Toast.LENGTH_LONG).show();

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
        moveTaskToBack(true);
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



}
