package com.example.JAR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.JAR.databinding.ActivityMainBinding;
import com.example.JAR.databinding.ViewPostBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
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
            HashMap<String, Drawable> thumbs = new HashMap<String, Drawable>(){};
            for(Submission post: posts) {
                if (post.hasThumbnail()) {
                    if (post.getThumbnail().length()>10) { // filter out tags like self and nsfw
                        Drawable d = null;
                        Log.d("Jar: GET",post.getThumbnail());
                        try {

                            d = Drawable.createFromStream((InputStream) new URL(post.getUrl()).getContent(), "src");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        thumbs.put(post.getThumbnail(), d);
                    }
                }
            }
            runOnUiThread(()-> {

                LinearLayout subBuilder = findViewById(R.id.submissionBuilder);
                for(Submission post: posts) {
                    PostView postView = new PostView(MainActivity.this);
                    postView.setPost(post);
                    if (post.hasThumbnail()) {
                        postView.setThumbnail(thumbs.get(post.getThumbnail()));
                    }
                    subBuilder.addView(postView);

                }

                // load more button
                Button btnLoadMore =new Button(MainActivity.this);
                btnLoadMore.setText(R.string.load_more);
                subBuilder.addView(btnLoadMore);

                testView.setText("Done");
            });
        }
    }

    void debug() {

    }
}
