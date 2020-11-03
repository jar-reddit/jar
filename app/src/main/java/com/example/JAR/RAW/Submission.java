package com.example.JAR.RAW;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Submission class is used for reddit submissions (posts)
 */
public class Submission extends Thing {
    private int ups; // upvotes
    private String title;
    private int num_comments; // number of comments
    private String author;
    private String selftext; 

    public Submission(JSONObject data) throws JSONException {
        super(data);
        JSONObject submissionData = data.getJSONObject("data");
        ups = submissionData.getInt("ups");
        title = submissionData.getString("title");
        num_comments = submissionData.getInt("num_comments");
        author = submissionData.getString("author");
        selftext= submissionData.getString("selftext");
    }

    @Override
    public String toString() {
        return "Submission{" +
                "ups=" + ups +
                ", title='" + title + '\'' +
                ", num_comments=" + num_comments +
                ", author='" + author + '\'' +
                ", selftext='" + selftext + '\'' +
                '}';
    }
}