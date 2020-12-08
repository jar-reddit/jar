package com.example.JAR;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.dean.jraw.models.Submission;

import java.util.List;

public class LinkHandler {
    public static void openUri(Uri uri, Context context) {
        List<String> uriPath = uri.getPathSegments();
        String subreddit;
        if (uriPath.get(0).equals("r")) {
            Log.d("JAR URL",uriPath.get(1));
            subreddit = uriPath.get(1);
            if (uriPath.size() >=4 && uriPath.get(2).equals("comments")) {
                Intent submissionIntent = new Intent(context, SubmissionActivity.class);
                Background.execute(() -> {
                    Log.d("Post URL",uriPath.get(3));
                    Submission s = JRAW.getInstance(context).submission(uriPath.get(3)).inspect();
                    ((Activity) context).runOnUiThread(()-> {
                        submissionIntent.putExtra("Post",s);
                        context.startActivity(submissionIntent);
                    });
                });


            } else {
                Intent subredditIntent = new Intent(context, SubredditActivity.class);
                subredditIntent.setAction(Intent.ACTION_SEARCH);
                subredditIntent.putExtra("query", subreddit);
                context.startActivity(subredditIntent);
            }
        } else {
            Intent subredditIntent = new Intent(context, SubredditActivity.class);
            context.startActivity(subredditIntent);
        }
    }
}
