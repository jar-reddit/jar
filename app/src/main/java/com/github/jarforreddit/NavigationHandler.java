package com.github.jarforreddit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import net.dean.jraw.models.Submission;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * This class should handle all navigation.
 */
public class NavigationHandler {
    /**
     * Opens the correct Activity when url is passed.
     *
     * @param uri     the reddit uri you want to go to
     * @param context
     */
    public static void openUri(Uri uri, Context context) {
        List<String> uriPath = uri.getPathSegments();
        String subreddit;
        if (uriPath.size() >= 2 && uriPath.get(0).equals("r")) {
            Log.d("JAR URL", uriPath.get(1));
            subreddit = uriPath.get(1);
            if (uriPath.size() >= 4 && uriPath.get(2).equals("comments")) {
                openSubmission(uriPath.get(3), context);

            } else {
                openSubreddit(subreddit, context);
            }
        } else if (uriPath.size() == 0) {
            openFrontpage(context);
        } else {
            letAndroidHandleIt(uri.toString(), context);
        }
    }

    /**
     * Open the corresponding submission
     *
     * @param id
     * @param context
     */
    public static void openSubmission(String id, Context context) {

        Background.execute(() -> {
            Submission sub;
            sub = JRAW.getInstance(context).submission(id).inspect();
            openSubmission(sub, context);
        });

    }

    /**
     * open a submission when a {@link Submission} object is passed
     *
     * @param submission
     * @param context
     */
    public static void openSubmission(Submission submission, Context context) {
        Intent submissionIntent = new Intent(context, SubmissionActivity.class);
        submissionIntent.putExtra("Post", submission);
        context.startActivity(submissionIntent);
    }

    public static void openSubreddit(String subreddit, Context context) {
        openSubreddit(subreddit,0,context);
    }

    public static void openSubreddit(String subreddit, int sortCriteria, Context context) {
        Intent subredditIntent = new Intent(context, SubredditActivity.class);
        subredditIntent.setAction(Intent.ACTION_SEARCH);
        subredditIntent.putExtra("query", subreddit);
        subredditIntent.putExtra("sort",sortCriteria);
        context.startActivity(subredditIntent);
    }


    public static void openFrontpage(Context context) {
        openMainActivity(context);
    }

    public static void openMainActivity(Context context) {
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

    /**
     * Open the link in the appropriate activity
     *
     * @param url URL to resource
     */
    public static void openLink(Context context, String url) {
        try {
            openLink(context, new URL(url));
        } catch (MalformedURLException e) {
            Log.d("JAR/URL", e.getMessage());
            if (url.startsWith("/")) {
                openLink(context,"https://reddit.com"+url);
            }
        }
    }

    /**
     * Open a link in a submission like the video or image
     *
     * @param submission the instance of the submission
     */
    public static void openLink(Context context, Submission submission) {
        String type = UrlDetector.detect(submission.getUrl());
        if (type.equals("reddit:video")) {
            if (submission.getEmbeddedMedia() != null
                    && submission.getEmbeddedMedia().getRedditVideo() != null) {
                openVideo(context, submission.getEmbeddedMedia().getRedditVideo().getDashUrl());
            }
            // TODO: 16/03/2021 show error  
        } else {
            openLink(context, submission.getUrl());
        }

    }

    private static void openLink(Context context, URL url) {
        String type = UrlDetector.detect(url);
        if (type.contains("image")) {
            openImage(context, url.toString());
        } else if (type.contains("video")) {
            if (type.contains("imgur")) {
                String mp4Url = url.toString().replace(".gifv", ".mp4");
                openVideo(context, mp4Url);
            } else {
                openVideo(context, url.toString());
            }
        } else if (type.equals("reddit:link")) {
            openUri(Uri.parse(url.toString()),context);
        } else {
            letAndroidHandleIt(url.toString(), context);
        }
        // TODO: 02/03/2021 add more link types like article to open in WebActivity 
    }

    public static void letAndroidHandleIt(String url, Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void openVideo(Context context, String url) {
        openMedia(context, url, "video");

    }

    public static void openImage(Context context, String url) {
        openMedia(context, url, "image");

    }

    /**
     * Open the media activity
     *
     * @param context
     * @param url
     * @param type
     */
    private static void openMedia(Context context, String url, String type) {
        Log.d("JAR", "opening " + type);
        Intent mediaIntent = new Intent(context, MediaActivity.class);
        mediaIntent.putExtra("url", url);
        mediaIntent.putExtra("type", type);
        context.startActivity(mediaIntent);
    }

    /**
     * Open secret activity
     *
     * @param context the context
     */
    public static void openSecret(Context context) {
        Intent secretIntent = new Intent(context, SecretActivity.class);
        context.startActivity(secretIntent);
    }

    public static void openTomlEditor(Context context) {
        Intent tomlEditorIntent = new Intent(context,TomlActivity.class);
        context.startActivity(tomlEditorIntent);
    }
    
    public static void openCrash(Context context,String title, String message) {
        Intent crashIntent = new Intent(context,CrashActivity.class);
        crashIntent.putExtra("title",title);
        crashIntent.putExtra("message",message);
        context.startActivity(crashIntent);
        ((Activity)context).finish();
    }
    
    public static void openCrash(Context context, Throwable ex) {
        openCrash(context,ex.getClass().getName(),ex.getMessage());
    }
}
