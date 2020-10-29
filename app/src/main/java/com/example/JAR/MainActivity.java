package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.JAR.RAW.Reddit;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Executor;

// Emilio comment
// Magd comment
// Murray Comment
// Steve Comment
public class MainActivity extends AppCompatActivity {
    public TextView testView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 27/10/20 remove this policy  https://developer.android.com/reference/android/os/NetworkOnMainThreadException
        // https://stackoverflow.com/a/9289190
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        testView = findViewById(R.id.test_text);
        //TODO: make our own Reddit API Wrapper
//      outText += "\nsub: " + response.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("subreddit");
//
        testView.setText("please wait");

        DebugThread dt = new DebugThread();
        dt.start(); // delegate the task to the background









    }

    public class DebugThread extends Thread {
        @Override
        public void run() {
            Reddit reddit = new Reddit(getApplicationContext(),getApplicationContext().getPackageName()+":v0.0.1 (by /u/JARForReddit)");
            String response = reddit.getFrontpage();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)findViewById(R.id.test_text)).setText(string);
                }

                private String string;

                public Runnable init(String string) {
                    this.string = string;
                    return this;
                }
            }.init(response));
        }
    }

    void debug() {

    }
}
