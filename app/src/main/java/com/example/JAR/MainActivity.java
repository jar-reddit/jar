package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.JAR.databinding.ActivityMainBinding;
import com.example.JAR.databinding.ViewPostBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.List;

// Emilio comment
// Magd comment
// Murray Comment
// Steve Comment
public class MainActivity extends AppCompatActivity {
    public TextView testView;
    public ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 27/10/20 remove this policy  https://developer.android.com/reference/android/os/NetworkOnMainThreadException
        // https://stackoverflow.com/a/9289190
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testView = findViewById(R.id.test_text);
        testView.setText("please wait");
        Log.d("JAR for Reddit", "Lets get STARTED");

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        //activityMainBinding.postList.addView();

        DebugThread dt = new DebugThread();
        dt.start(); // delegate the task to the background

    }

    public class DebugThread extends Thread {
        public DebugThread() {
        }

        @Override
        public void run() {
            RedditClient reddit =  JRAW.init(MainActivity.this.getApplicationContext());
            DefaultPaginator<Submission>  page = reddit.frontPage().build();
            List<Submission> posts = page.next().getChildren();
            runOnUiThread(()-> {
                LinearLayout subBuilder = findViewById(R.id.submissionBuilder);
                for(Submission post: posts) {
                        PostView postView = new PostView(MainActivity.this);
                        postView.setPost(post);

                        subBuilder.addView(postView);

                    }

                testView.setText("Done");
            });
        }
    }

    void debug() {

    }
}
