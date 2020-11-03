package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.JAR.RAW.ListingThing;
import com.example.JAR.RAW.Reddit;
import com.example.JAR.RAW.Submission;
import com.example.JAR.RAW.Thing;

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

        DebugThread dt = new DebugThread();
        dt.start(); // delegate the task to the background









    }

    public class DebugThread extends Thread {
        public DebugThread() {
        }

        @Override
        public void run() {
            // this is run when the start function is called
            Reddit reddit = new Reddit(getApplicationContext(),getApplicationContext().getPackageName()+":v0.0.1 (by /u/JARForReddit)");

            ListingThing listings = reddit.getFrontpage(); // have a look at reddit.com/.json if you don't understand
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    LinearLayout subBuilder = (LinearLayout) findViewById(R.id.submissionBuilder);
                    // load all submissions on the main page
                    for (Thing submission:listings.getData().getChildren()) {
                        Log.d("JAR",((Submission)submission).toString()); // LOGCAT
                        if (submission instanceof Submission) {
                            TextView submissionView = new TextView(MainActivity.this);
                                submissionView.setBackgroundColor(Color.GREEN);
                                submissionView.setText(submission.toString());
                                submissionView.setLayoutParams(subBuilder.getLayoutParams());

                            View empty = new View(MainActivity.this); // black line
                                empty.setBackgroundColor(Color.BLACK);
                                empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));

                            subBuilder.addView(empty);
                            subBuilder.addView(submissionView);


                        }
                        Log.d("JAR for Reddit","added a view?");
                    }
                    Log.d("JAR for Reddit", "I think I'm done");
                    ((TextView)findViewById(R.id.test_text)).setText("done"); // debug view
                }

                private ListingThing listings;

                // created another method to get variables from outer class
                public Runnable init(ListingThing listings) {
                    this.listings =listings;
                    return this;
                }
            }.init(listings));
        }
    }

    void debug() {

    }
}
