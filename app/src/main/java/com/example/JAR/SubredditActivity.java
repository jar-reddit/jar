package com.example.JAR;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.JAR.databinding.ActivitySubredditBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.models.SubredditSearchResult;
import net.dean.jraw.models.SubredditSearchSort;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.BackoffStrategy;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.RedditIterable;
import net.dean.jraw.pagination.SubredditSearchPaginator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Super class for any subreddit activity
 */
public class SubredditActivity extends AppCompatActivity {
    private DefaultPaginator<Submission> page; // subreddit
    private boolean loadingPosts = false;
    public ActivitySubredditBinding binding;
    private List<Submission> allPosts;
    private boolean frontpage = true;
    private ListView searchSuggestions;
    RecyclerView postList;
    List<String> suggestionList = new ArrayList<>();
    ArrayAdapter<String> suggestionAdapter;
    PostAdapter postAdapter;
    String [] topSubs;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topSubs = getResources().getStringArray(R.array.top_subreddits);
        binding = ActivitySubredditBinding.inflate(getLayoutInflater()); // Joining views to Java
        if (allPosts == null) {
            allPosts = Listing.empty(); // initialise an empty list
        }
        postList = binding.postList;
        postAdapter = new PostAdapter(allPosts);
        postList.setAdapter(postAdapter);
        postList.setLayoutManager(new LinearLayoutManager(this));
        setContentView(binding.getRoot()); // set the layout
        if ( !(this instanceof MainActivity)) {
            disableNav();
        }



        searchSuggestions = binding.listview;
        setSupportActionBar(binding.topAppBar);


        suggestionAdapter = new ArrayAdapter<>(SubredditActivity.this, android.R.layout.simple_list_item_1, suggestionList);
        searchSuggestions.setAdapter(suggestionAdapter);

        Intent intent = getIntent();
        String query;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            frontpage = false;
            query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("JAR", query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            NavigationHandler.openUri(intent.getData(),this);
            return;
        }
        Background.execute(() -> {
            loadingPosts = true;
            RedditClient reddit = JRAW.getInstance(this); // Gets the client
            if (frontpage) {
                page = reddit.frontPage().build(); // Gets The Front Page

            } else if (intent.getStringExtra(SearchManager.QUERY) != null) {
                List<SubredditSearchResult> sub = checkSubreddit(intent.getStringExtra(SearchManager.QUERY));
                if (sub.size() > 0) {
                    page = reddit.subreddit(sub.get(0).getName()).posts().build();
                } else {
                    finish();
                }
            }

            List<Submission> posts = page.next().getChildren(); // This retrieves all the posts
            allPosts.addAll(posts);
            loadingPosts = false;
            Log.d("Jar", "added posts");
            runOnUiThread(() -> {
                binding.progressBar.setVisibility(View.INVISIBLE);
                postAdapter.notifyDataSetChanged();

            });
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
        Log.d("Jar actBar","Option menu being created");
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView search = (SearchView) item.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, this.getClass())));
        search.setQueryHint(getResources().getString(R.string.search_hint));
        search.setIconifiedByDefault(false);
        search.requestFocus();

        Intent intent = getIntent();
        if (intent.getStringExtra("query") != null) {
            String message = "r/" + intent.getStringExtra("query");
            ActionBar aB = getSupportActionBar();
            aB.setTitle(message);
        }

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                suggestionAdapter = new ArrayAdapter(SubredditActivity.this, android.R.layout.simple_list_item_1, suggestionList);
                suggestionAdapter.clear();
                searchSuggestions.setAdapter(suggestionAdapter);
                postList.setVisibility(View.INVISIBLE);
                searchSuggestions.setVisibility(View.VISIBLE);
                suggestionAdapter.addAll(topSubs);
//                searchSuggestions.setTextFilterEnabled(true);
                suggestionAdapter.getFilter().filter((query));
                suggestionAdapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), String.valueOf(suggestionAdapter.getCount()),Toast.LENGTH_SHORT).show();

//                {
//                    Background.execute(() ->
//                    {
////                        RedditClient subSub = JRAW.getInstance(getApplicationContext());
////                        if (query.length() > 0) {
////                            List<SubredditSearchResult> subSuggestList = subSub.searchSubredditsByName(query);
////                            suggestionList.clear();
////                            if (subSuggestList.size() > 0 ) {
////                                for (int i = 0; i < subSuggestList.size(); i++) {
////                                    suggestionList.add(subSuggestList.get(i).getName());
//
//                        if (query.length() > 3) {
////                         List<Subreddit> subSuggestList = newSubredditSearch(query);
////                            List<String> subSuggestList = recommendedSubs();
////                         suggestionList.clear();
////                         if (subSuggestList.size() > 0 ) {
////                                for (int i = 0; i < subSuggestList.size(); i++) {
////                                    suggestionList.add(subSuggestList.get(i).getName());
////                                }
////                            }
//                            suggestionList.addAll(recommendedSubs());
//                            searchSuggestions.setTextFilterEnabled(true);
////                            Looper lx = getMainLooper();
////                            if (Looper.myLooper() == null) {
////                                lx.prepare();
////                            }
//                        } else {
//                            runOnUiThread(() -> {
//                                suggestionList.clear();
//                                searchSuggestions.setVisibility(View.INVISIBLE);
//                                postList.setVisibility(View.VISIBLE);
//                            });
//                        }
//                    });
//                    suggestionAdapter.getFilter().filter((query));
//                    suggestionAdapter.notifyDataSetChanged();
//                }

                searchSuggestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int listPos, long l) {
//                        search.setIconified(true);
//                        item.collapseActionView();
                        Toast.makeText(getApplicationContext(), String.valueOf(listPos), Toast.LENGTH_SHORT).show();
                        NavigationHandler.openSubreddit(suggestionAdapter.getItem(listPos), SubredditActivity.this);

//                        Toast.makeText(getApplicationContext(), "Listview clicked " + suggestionAdapter.getItem(listPos), Toast.LENGTH_SHORT).show();
//                        // Open selected subreddit
                    }
                });
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.web_search:
                Log.d("JAR Menu click","web search");
                NavigationHandler.openWebSearch(this, "Nothing","Google");
                return true;
            case R.id.action_search:
                Log.d("JAR Menu click","Action search");
                return true;
            case R.id.sort_Posts:
                FragmentManager fm = getFragmentManager();
                SortFragment sf = new SortFragment();
                sf.show(fm, "hello");
//                Toast.makeText(this, "Hi I am working",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public List<SubredditSearchResult> checkSubreddit(String query) {
        RedditClient subSearch = JRAW.getInstance(getApplicationContext());
        return subSearch.searchSubredditsByName(query);
    }

//    public List<Subreddit> newSubredditSearch (String query) {
//        RedditClient subSearch = JRAW.getInstance(getApplicationContext());
//        SubredditSearchPaginator.Builder builder = subSearch.searchSubreddits();
//        SubredditSearchPaginator p = builder.query(query).limit(10).build();
//        List<Subreddit> searchList = p.next();
//        return searchList;
//    }

    public String newSubredditSearch (String query) {
        RedditClient subSearch = JRAW.getInstance(getApplicationContext());
        SubredditSearchPaginator.Builder builder = subSearch.searchSubreddits();
        SubredditSearchPaginator p = builder.query(query).limit(1).build();
        List<Subreddit> searchList = p.next();
        return searchList.get(0).getName();
    }

    public List<String> recommendedSubs()
    {
        RedditClient recs = JRAW.getInstance(getApplicationContext());
        List<String> recommend = null;
        recs.recommendedSubreddits(recommend, false);
        return recommend;
    }


    public void showSortOptions(int sortCriteria)
    {
        RedditClient reddit = JRAW.getInstance(this);
        if (frontpage) {
            switch (sortCriteria) {
                case 0:
//                  String p = allPosts.get(0).getPermalink();
//                  Log.d("postHTML", p)
                    Log.d("HOT", String.valueOf(sortCriteria));
                    page = reddit.frontPage().sorting(SubredditSort.HOT).build();
                    backgroundTasks();
                    break;
                case 1:
                    Log.d("NEW", String.valueOf(sortCriteria));
                    page = reddit.frontPage().sorting(SubredditSort.NEW).build();
                    backgroundTasks();
                    break;
                case 2:
                    Log.d("TOP", String.valueOf(sortCriteria));
                    page = reddit.frontPage().sorting(SubredditSort.TOP).build();
                    backgroundTasks();
                    break;
                case 3:
                    Log.d("RISING", String.valueOf(sortCriteria));
                    page = reddit.frontPage().sorting(SubredditSort.RISING).build();
                    backgroundTasks();
                    break;
                case 4:
                    Log.d("CONTROVERSIAL", String.valueOf(sortCriteria));
                    page = reddit.frontPage().sorting(SubredditSort.CONTROVERSIAL).build();
                    backgroundTasks();
                    break;
                case 5:
                    Log.d("BEST", String.valueOf(sortCriteria));
                    page = reddit.frontPage().sorting(SubredditSort.BEST).build();
                    backgroundTasks();
                    break;
            }
        }
        else
        {
            String postSub = allPosts.get(0).getSubreddit();
            switch (sortCriteria) {
                case 0:
                    Toast.makeText(this, String.valueOf(sortCriteria), Toast.LENGTH_SHORT).show();
                    Log.d("HOT", String.valueOf(sortCriteria));
                    page = reddit.subreddit(postSub).posts().sorting(SubredditSort.HOT).build();
                    backgroundTasks();
                    break;
                case 1:
                    Toast.makeText(this, String.valueOf(sortCriteria), Toast.LENGTH_SHORT).show();
                    Log.d("NEW", String.valueOf(sortCriteria));
                    page = reddit.subreddit(postSub).posts().sorting(SubredditSort.NEW).build();
                    backgroundTasks();
                    break;
                case 2:
                    Toast.makeText(this, String.valueOf(sortCriteria), Toast.LENGTH_SHORT).show();
                    Log.d("TOP", String.valueOf(sortCriteria));
                    page = reddit.subreddit(postSub).posts().sorting(SubredditSort.TOP).build();
                    backgroundTasks();
                    break;
                case 3:
                    Toast.makeText(this, String.valueOf(sortCriteria), Toast.LENGTH_SHORT).show();
                    Log.d("RISING", String.valueOf(sortCriteria));
                    page = reddit.subreddit(postSub).posts().sorting(SubredditSort.RISING).build();
                    backgroundTasks();
                    break;
                case 4:
                    Toast.makeText(this, String.valueOf(sortCriteria), Toast.LENGTH_SHORT).show();
                    Log.d("CONTROVERSIAL", String.valueOf(sortCriteria));
                    page = reddit.subreddit(postSub).posts().sorting(SubredditSort.CONTROVERSIAL).build();
                    backgroundTasks();
                    break;
                case 5:
                    Toast.makeText(this, String.valueOf(sortCriteria), Toast.LENGTH_SHORT).show();
                    Log.d("BEST", String.valueOf(sortCriteria));
                    page = reddit.subreddit(postSub).posts().sorting(SubredditSort.BEST).build();
                    backgroundTasks();
                    break;
            }
        }

        }

    public void enableNav() {
        binding.getRoot().addView(binding.nav,binding.nav.getLayoutParams());
    }
    public void disableNav() {
        binding.getRoot().removeView(binding.nav);
        binding.topAppBar.setNavigationIcon(null);
    }

    public void backgroundTasks()
    {
        Background.execute(() -> {
            allPosts.clear();
            List<Submission> posts = page.next().getChildren();
            allPosts.addAll(posts);
            runOnUiThread(() -> {
                postAdapter.notifyDataSetChanged();
            });
        });
    }
}
