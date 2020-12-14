package com.example.JAR;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JAR.databinding.ActivitySubredditBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSearchResult;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Super class for any subreddit activity
 */
public class SubredditActivity extends AppCompatActivity {
    private DefaultPaginator<Submission> page; // subreddit
    private boolean loadingPosts = false;
    public ActivitySubredditBinding binding;
    private List<Submission> allPosts;
    private boolean frontpage = true;
    private boolean isUri = false;
    private SearchView search;
    private Settings s;
    private ListView searchSuggestions;
    RecyclerView postList;
    List<String> suggestionList = new ArrayList<>();
    ArrayAdapter<String> suggestionAdapter;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        s = new Settings(this);
        binding = ActivitySubredditBinding.inflate(getLayoutInflater()); // Joining views to Java
        if (allPosts == null) {
            allPosts = Listing.empty(); // initialise an empty list
        }
        postList = binding.postList;
        postAdapter = new PostAdapter(allPosts);
        postList.setAdapter(postAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));
        setContentView(binding.getRoot()); // set the layout

        searchSuggestions = (ListView) findViewById(R.id.listview);


        suggestionAdapter = new ArrayAdapter(SubredditActivity.this, android.R.layout.simple_list_item_1, suggestionList);
        searchSuggestions.setAdapter(suggestionAdapter);

        Intent intent = getIntent();
        String query;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            frontpage = false;
            query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("JAR", query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
//            frontpage=false;
//            isUri = true;
            NavigationHandler.openUri(intent.getData(),this);
            return;
        }

        Background.execute(() -> {
            loadingPosts = true;
            RedditClient reddit = JRAW.getInstance(this); // Gets the client
            if (frontpage) {
                page = reddit.frontPage().build(); // Gets The Front Page

                List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
                allPosts.addAll(posts);
                loadingPosts = false;
                Log.d("Jar", "added posts");
                runOnUiThread(() -> {
                    postAdapter.notifyDataSetChanged();

                });
            } else if (intent.getStringExtra(SearchManager.QUERY) != null) {
                List<SubredditSearchResult> sub = checkSubreddit(intent.getStringExtra(SearchManager.QUERY));
                if (sub.size() > 0) {
                    page = reddit.subreddit(sub.get(0).getName()).posts().build();
                    List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
                    allPosts.addAll(posts);
                    loadingPosts = false;
                    Log.d("Jar", "added posts");
                    runOnUiThread(() -> {
                        postAdapter.notifyDataSetChanged();
                    });
                } else {
                    finish();
                }
            }
        });

        postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) & !loadingPosts) {
                    Background.execute(() -> {
                        loadingPosts = true;
                        List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
                        allPosts.addAll(posts);
                        loadingPosts = false;
                        Log.d("Jar", "added posts");
                        runOnUiThread(() -> {
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
        mI.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        search = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, this.getClass())));
        search.setQueryHint(getResources().getString(R.string.search_hint));
        search.setIconifiedByDefault(false);
        search.requestFocus();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                suggestionAdapter = new ArrayAdapter(SubredditActivity.this, android.R.layout.simple_list_item_1, suggestionList);
                searchSuggestions.setAdapter(suggestionAdapter);
                postList.setVisibility(View.INVISIBLE);
                searchSuggestions.setVisibility(View.VISIBLE);
                {
                    Background.execute(() ->
                    {
                        RedditClient subSub = JRAW.getInstance(getApplicationContext());
                        if (query.length() > 0) {
                            List<SubredditSearchResult> subSuggestList = subSub.searchSubredditsByName(query);
                            suggestionList.clear();
                            if (subSuggestList.size() > 0 ) {
                                for (int i = 0; i < subSuggestList.size(); i++) {
                                    suggestionList.add(subSuggestList.get(i).getName());
                                }
                            }
                            searchSuggestions.setTextFilterEnabled(true);
                            Looper lx = getMainLooper();
                            if (Looper.myLooper() == null) {
                                lx.prepare();
                            }
                        } else {
                            runOnUiThread(() -> {
                                suggestionList.clear();
                                searchSuggestions.setVisibility(View.INVISIBLE);
                                postList.setVisibility(View.VISIBLE);
                            });
                        }
                    });
                    suggestionAdapter.getFilter().filter((query));
                    suggestionAdapter.notifyDataSetChanged();
                }

                searchSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int listPos, long l) {
                        NavigationHandler.openSubreddit(suggestionAdapter.getItem(listPos), getApplicationContext());

//                        Toast.makeText(getApplicationContext(), "Listview clicked " + suggestionAdapter.getItem(listPos), Toast.LENGTH_SHORT).show();
//                        // Open selected subreddit
                    }
                });
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public List<SubredditSearchResult> checkSubreddit(String query) {
        RedditClient subSearch = JRAW.getInstance(getApplicationContext());
        return subSearch.searchSubredditsByName(query);
    }
}
