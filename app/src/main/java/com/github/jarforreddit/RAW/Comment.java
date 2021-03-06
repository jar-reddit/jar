package com.github.jarforreddit.RAW;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Comment extends Thing implements Votable, Created{
    String approved_by;
    String author;
    String author_flair_css_class;
    String author_flair_text;
    String banned_by;
    String body;
    String body_html;
    String edited; // will be either date or boolean
    int gilded;
    boolean likes;
    String link_author;
    String link_id;
    String link_title;
    String link_url;
    int num_reports;
    String parent_id;
    List<Thing> replies;
    boolean saved;
    int score;
    boolean score_hidden;
    String subreddit;
    String subreddit_id;
    String distinguished;

    public Comment(JSONObject data) throws JSONException {
        super(data);
    }


    @Override
    public void upvote() {

    }

    @Override
    public void downvote() {

    }
}
