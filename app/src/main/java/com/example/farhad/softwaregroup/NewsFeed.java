package com.example.farhad.softwaregroup;

/**
 * Created by Luis on 5/2/17.
 */

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsFeed extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        WebView w1=(WebView)findViewById(R.id.webView);
        w1.loadUrl("https://twitter.com/phoenixradio");
    }
}
