package com.example.JAR;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.JAR.databinding.ViewPostBinding;

import net.dean.jraw.models.Submission;

public class PostView extends LinearLayout {
    private TextView txtTitle;
    private TextView txtSelftext;
    private TextView txtScore;
    private TextView txtComments;
    private ViewPostBinding binding;
    private Submission post;

    // https://medium.com/@Sserra90/android-writing-a-compound-view-1eacbf1957fc
    public PostView(Context context) {
        super(context);

        init();
    }

    void init() {


        binding = ViewPostBinding.bind(inflate(getContext(),R.layout.view_post,this));
        txtTitle = binding.postTitle;
        txtSelftext = binding.postSelftext;
        txtScore = binding.textScore;
        txtComments = binding.textComments;


    }

    /**
     * Use this to set the data of the view
     * @param title posts title
     * @param selftext post selftext
     * @param score post score
     * @param comments number of comments
     */
    public void setData(String title, String selftext, String score, String comments) {
        this.txtTitle.setText(title);
        this.txtSelftext.setText(selftext);
        this.txtScore.setText(score);
        this.txtComments.setText(comments);
    }

    public void setPost(Submission post) {
        this.post = post;
        setData(post.getTitle(),post.getSelfText(),"" + post.getScore(),""+post.getCommentCount()); // TODO: 07/11/20 Check if needed
    }

}
