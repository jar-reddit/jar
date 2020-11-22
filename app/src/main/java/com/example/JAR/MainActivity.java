package com.example.JAR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Emilio comment
// Magd comment
// Murray Comment
// Steve Comment
public class MainActivity extends AppCompatActivity {
    public TextView testView;
    public SearchView search;
    private DefaultPaginator<Submission> page;
    private boolean loadingPosts = false;
    public ActivityMainBinding activityMainBinding;
    static final ExecutorService testExecutor = Executors.newFixedThreadPool(4); // TODO: 16/11/20 How executors work?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 27/10/20 remove this policy  https://developer.android.com/reference/android/os/NetworkOnMainThreadException
        // https://stackoverflow.com/a/9289190
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (SearchView) findViewById(R.id.searchView1);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        search.setQueryHint(getResources().getString(R.string.search_hint));

        testView = findViewById(R.id.test_text);
        testView.setText("please wait");
        Log.d("JAR for Reddit", "Lets get STARTED");

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        List<Submission> allPosts = Listing.empty();
        RecyclerView postList = findViewById(R.id.postList);
        PostAdapter postAdapter = new PostAdapter(allPosts);
        postList.setAdapter(postAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));
        testExecutor.execute(()->{
            loadingPosts =true;
            RedditClient reddit =  JRAW.getInstance(this); // Gets the client
            page = reddit.frontPage().build(); // Gets The Front Page
            List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
            allPosts.addAll(posts);
            loadingPosts=false;
            Log.d("Jar","added posts");
            runOnUiThread(()->{
                postAdapter.notifyDataSetChanged();

            });
        });

        postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)&!loadingPosts) {
                    testExecutor.execute(()->{
                        loadingPosts=true;
                        List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
                        allPosts.addAll(posts);
                        loadingPosts=false;
                        Log.d("Jar","added posts");
                        runOnUiThread(()->{
                            postAdapter.notifyDataSetChanged();
                        });
                    });
                }
            }
        });
    }
}
