package com.example.farhad.softwaregroup;

/**
 * Created by Luis on 5/2/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsFeed extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        WebView w1=(WebView)findViewById(R.id.webView);
        w1.loadUrl("https://twitter.com/phoenixradioxxx");
    }
    //What to do when the back button is pressed. Sends to the main screen
    public void onBackPressed(){
        Intent prevActivityIntent = new Intent(NewsFeed.this, MainActivity.class);
        startActivity(prevActivityIntent);
    }
}
