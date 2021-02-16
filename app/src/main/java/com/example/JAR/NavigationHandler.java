package com.example.JAR;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.dean.jraw.models.Submission;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.List;

/**
 * This class should handle all navigation.
 */
public class NavigationHandler {
    /**
     * Opens the correct Activity when url is passed.
     * @param uri the reddit uri you want to go to
     * @param context
     */
    public static void openUri(Uri uri, Context context) {
        List<String> uriPath = uri.getPathSegments();
        String subreddit;
        if (uriPath.size() >=2 && uriPath.get(0).equals("r")) {
            Log.d("JAR URL",uriPath.get(1));
            subreddit = uriPath.get(1);
            if (uriPath.size() >=4 && uriPath.get(2).equals("comments")) {
                openSubmission(uriPath.get(3),context);
//                Intent submissionIntent = new Intent(context, SubmissionActivity.class);
//                Background.execute(() -> {
//                    Log.d("Post URL",uriPath.get(3));
//                    Submission s = JRAW.getInstance(context).submission(uriPath.get(3)).inspect();
//                    ((Activity) context).runOnUiThread(()-> {
//                        submissionIntent.putExtra("Post",s);
//                        context.startActivity(submissionIntent);
//                    });
//                });


            } else {
                openSubreddit(subreddit,context);
//                Intent subredditIntent = new Intent(context, SubredditActivity.class);
//                subredditIntent.setAction(Intent.ACTION_SEARCH);
//                subredditIntent.putExtra("query", subreddit);
//                context.startActivity(subredditIntent);
            }
        } else {
            openFrontpage(context);
//            Intent subredditIntent = new Intent(context, SubredditActivity.class);
//            context.startActivity(subredditIntent);
        }
    }

    /**
     * Open the corresponding submission
     * @param id
     * @param context
     */
    public static void openSubmission(String id, Context context) {

        Background.execute(()->{
            Submission sub;
            sub = JRAW.getInstance(context).submission(id).inspect();
            openSubmission(sub,context);
        });

    }

    /**
     * open a submission when a {@link Submission} object is passed
     * @param submission
     * @param context
     */
    public static void openSubmission(Submission submission, Context context) {
        Intent submissionIntent = new Intent(context, SubmissionActivity.class);
        submissionIntent.putExtra("Post",submission);
        context.startActivity(submissionIntent);
    }

    public static void openSubreddit(String subreddit, Context context) {
        Intent subredditIntent = new Intent(context, SubredditActivity.class);
        subredditIntent.setAction(Intent.ACTION_SEARCH);
        subredditIntent.putExtra("query", subreddit);
        context.startActivity(subredditIntent);
//        Background.execute(()->{
//            DefaultPaginator<Submission> page = JRAW.getInstance(context).subreddit(subreddit).posts().build();
//
//        });
    }

    public static void openSubreddit(String subreddit, int sortCriteria, Context context)
    {

    }



    public static void openFrontpage(Context context) {
        Intent subredditIntent = new Intent(context, SubredditActivity.class);
        context.startActivity(subredditIntent);
    }

    public static void openMainActivty(Context context) {
        Intent mainIntent = new Intent(context, MainActivity.class);
        context.startActivity(mainIntent);
    }

    public static void openWebSearch(Context context, String query, String searchEngine) {
        Intent searchIntent = new Intent(context, SearchActivity.class);
        context.startActivity(searchIntent);
    }

    public static void openLogin(Context context) {
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
    }
}
