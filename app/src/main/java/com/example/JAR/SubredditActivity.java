package com.example.JAR;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JAR.databinding.ActivitySubredditBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.List;

/**
 * Super class for any subreddit activity
 */
public class SubredditActivity extends AppCompatActivity {
    private DefaultPaginator<Submission> page; // subreddit
    private boolean loadingPosts = false;
    public ActivitySubredditBinding binding;
    private List<Submission> allPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivitySubredditBinding.inflate(getLayoutInflater()); // Joining views to Java
        if (allPosts==null) {
            allPosts = Listing.empty(); // initialise an empty list
        }
        RecyclerView postList = binding.postList;
        PostAdapter postAdapter = new PostAdapter(allPosts);
        postList.setAdapter(postAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));
        setContentView(binding.getRoot()); // set the layout
        Background.execute(()->{
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
                    Background.execute(()->{
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mI = getMenuInflater();
        mI.inflate(R.menu.manu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView search = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        search.setQueryHint(getResources().getString(R.string.search_hint));
        return super.onCreateOptionsMenu(menu);
    }
}
