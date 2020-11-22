package com.example.JAR;
// TODO: EE 22/11/2020 handling no results, limiting instances, returning failed dialog to user, adding subreddit specific functionality, superclass, commenting code
import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JAR.databinding.ActivityMainBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSearchResult;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.JAR.MainActivity.testExecutor;

public class SearchActivity extends AppCompatActivity {

    List<SubredditSearchResult> results;
    public String query;
    public TextView testView;
    public SearchView search;
    public ActivityMainBinding activityMainBinding;
    static final ExecutorService testExecutor = Executors.newFixedThreadPool(4); // TODO: 16/11/20 How executors work?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 27/10/20 remove this policy  https://developer.android.com/reference/android/os/NetworkOnMainThreadException
        // https://stackoverflow.com/a/9289190
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (SearchView) findViewById(R.id.searchView1);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        search.setQueryHint(getResources().getString(R.string.search_hint));

        testView = findViewById(R.id.test_text);
        testView.setText("please wait");
        Log.d("JAR for Reddit", "Lets get STARTED");

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        {
        testExecutor.execute(()->{

           RedditClient reddit =  JRAW.init(SearchActivity.this.getApplicationContext()); // Gets the client
                DefaultPaginator<Submission> page = reddit.subreddit(checkSubreddit(query).get(0).getName()).posts().build();
                List<Submission> posts = page.next().getChildren(); // This retrieves all the posts

            runOnUiThread(()->{
                RecyclerView postList = findViewById(R.id.postList);
                postList.setAdapter(new PostAdapter(posts));
                postList.setLayoutManager(new LinearLayoutManager(this));
            });
        });

        DebugThread dt = new DebugThread();
//        dt.start(); // delegate the task to the background

    }
}
    public class DebugThread extends Thread {
        public DebugThread() {
        }

        @Override
        public void run() {
            RedditClient reddit =  JRAW.init(SearchActivity.this.getApplicationContext());
            DefaultPaginator<Submission>  page = reddit.frontPage().build();
            List<Submission> posts = page.next().getChildren();


//            runOnUiThread(()-> {
//
//                LinearLayout subBuilder = findViewById(R.id.submissionBuilder);
//                for(Submission post: posts) {
//                    PostView postView = new PostView(MainActivity.this);
//                    postView.setPost(post);
//                    subBuilder.addView(postView);
//
//                }
//
//                // load more button
//                Button btnLoadMore =new Button(MainActivity.this);
//                btnLoadMore.setText(R.string.load_more);
//                btnLoadMore.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // TODO: 16/11/20 Find a better way then nested threads
//                        subBuilder.removeView(btnLoadMore);
//                        subBuilder.removeView(testView);
//                        testView.setText("Please wait");
//                        subBuilder.addView(testView);
//                        MainActivity.testExecutor.execute(()->{
//                            List<Submission> morePosts = page.next().getChildren();
//                            runOnUiThread(()->{
//                                subBuilder.removeView(testView);
//                                for(Submission post: morePosts) {
//                                    PostView postView = new PostView(MainActivity.this);
//                                    postView.setPost(post);
//                                    subBuilder.addView(postView);
//                                }
//                                subBuilder.addView(btnLoadMore);
//                            });
//
//                        });
//                    }
//                });
//                subBuilder.addView(btnLoadMore);
//
//                testView.setText("Done");
//                subBuilder.removeView(testView);
//            });
        }
    }

    void debug() {

    }

    public List<SubredditSearchResult> checkSubreddit(String query)
    {
       RedditClient subSearch = JRAW.getInstance(getApplicationContext());
       results = subSearch.searchSubredditsByName(query);
       return results;
    }
}
































































































































   /* public String query;
    public TextView x;

    List<SubredditSearchResult> results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        *//*testExecutor.execute(()->{*//*

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            x = findViewById(R.id.queryResults);
            x.setText(doMySearch(query).size());
        }
//        });
    }

    public List<SubredditSearchResult> doMySearch(String query)
    {
       RedditClient subSearch = JRAW.getInstance(getApplicationContext());
       results = subSearch.searchSubredditsByName(query);
       return results;
    }*/


//}