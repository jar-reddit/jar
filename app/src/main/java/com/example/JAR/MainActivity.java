package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.JAR.RAW.ListingThing;
import com.example.JAR.RAW.Reddit;
import com.example.JAR.RAW.Submission;
import com.example.JAR.RAW.Thing;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
        Log.d("JAR for Reddit", "Lets get STARTED");

        DebugThread dt = new DebugThread(this);
        dt.start(); // delegate the task to the background









    }

    public class DebugThread extends Thread {
        private Activity activity;
        public DebugThread(Activity activity) {
            this.activity=activity;
        }

        @Override
        public void run() {
            Reddit reddit = new Reddit(getApplicationContext(),getApplicationContext().getPackageName()+":v0.0.1 (by /u/JARForReddit)");
//            String response = reddit.getFrontpage();
            ListingThing listings = reddit.getFrontpage();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout subBuilder = (LinearLayout) findViewById(R.id.submissionBuilder);

                    for (Thing submission:listings.getData().getChildren()) {
                        Log.d("JAR",((Submission)submission).toString());
                        if (submission instanceof Submission) {
                            TextView submissionView = new TextView(MainActivity.this);
                            submissionView.setBackgroundColor(Color.GREEN);
                            submissionView.setText(submission.toString());
                            submissionView.setLayoutParams(subBuilder.getLayoutParams());
                            View empty = new View(MainActivity.this);
                            empty.setBackgroundColor(Color.BLACK);
                            empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
                            subBuilder.addView(empty);
                            subBuilder.addView(submissionView);


                        }
                        Log.d("JAR for Reddit","added a view?");
                    }
                    Log.d("JAR for Reddit", "I think I'm done");
                    ((TextView)findViewById(R.id.test_text)).setText("done");
                }

                private String string;
                private ListingThing listings;
                private Activity activity;

                public Runnable init(String string) {
                    this.string = string;
                    return this;
                }

                public Runnable init(ListingThing listings, Activity activity) {
                    this.listings =listings;
                    this.activity=activity;
                    return this;
                }
            }.init(listings,activity));
        }
    }

    void debug() {

    }
}
