package com.example.JAR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public class MainActivity extends SubredditActivity {
//    public TextView testView;
//    public MenuItem item;
//    public SearchView search;
//    private DefaultPaginator<Submission> page;
//    private boolean loadingPosts = false;
//    public ActivityMainBinding activityMainBinding;
//    static final ExecutorService testExecutor = Executors.newFixedThreadPool(4); // TODO: 16/11/20 How executors work?

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO: 27/10/20 remove this policy  https://developer.android.com/reference/android/os/NetworkOnMainThreadException
//        // https://stackoverflow.com/a/9289190
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////        StrictMode.setThreadPolicy(policy);
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        search = (SearchView) findViewById(R.id.searchView1);
////        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
////        search.setQueryHint(getResources().getString(R.string.search_hint));
//
//        testView = findViewById(R.id.test_text);
//        testView.setText("please wait");
//        Log.d("JAR for Reddit", "Lets get STARTED");
//
//        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        List<Submission> allPosts = Listing.empty();
//        RecyclerView postList = findViewById(R.id.postList);
//        PostAdapter postAdapter = new PostAdapter(allPosts);
//        postList.setAdapter(postAdapter);
//        postList.setLayoutManager(new LinearLayoutManager(this));
//
//        testExecutor.execute(()->{
//            loadingPosts =true;
//            RedditClient reddit =  JRAW.getInstance(this); // Gets the client
//            page = reddit.frontPage().build(); // Gets The Front Page
//            List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
//            allPosts.addAll(posts);
//            loadingPosts=false;
//            Log.d("Jar","added posts");
//            runOnUiThread(()->{
//                postAdapter.notifyDataSetChanged();
//
//            });
//        });
//
//        postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (!recyclerView.canScrollVertically(1)&!loadingPosts) {
//                    testExecutor.execute(()->{
//                        loadingPosts=true;
//                        List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
//                        allPosts.addAll(posts);
//                        loadingPosts=false;
//                        Log.d("Jar","added posts");
//                        runOnUiThread(()->{
//                            postAdapter.notifyDataSetChanged();
//                        });
//                    });
//                }
//            }
//        });
//
//        //TODO: remove this test code
//        //Settings s = new Settings();
//        try {
//            defaultSettings();
//            Log.d("Settings:", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    //TODO: Figure out why same method doesn't work in settings class
//    public void defaultSettings() throws IOException{
//        try {
//
//            //TODO: This is test code - remove
//            FileOutputStream fos = null;
//            try {
//                fos = openFileOutput("settings.txt", MODE_PRIVATE);
//                //Example default settings
//                String l1 = "testing testing 1 2 3";
//                fos.write(l1.getBytes());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } finally{
//                if(fos != null){
//                    fos.close();
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater mI = getMenuInflater();
//        mI.inflate(R.menu.manu_main, menu);
//        item = menu.findItem(R.id.action_search);
//        search = (SearchView) item.getActionView();
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
//        search.setQueryHint(getResources().getString(R.string.search_hint));
//        return super.onCreateOptionsMenu(menu);
//    }

}
