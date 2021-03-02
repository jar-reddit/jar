package com.example.JAR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.JAR.databinding.ViewPostBinding;
import com.google.android.material.card.MaterialCardView;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubmissionPreview;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

// This is the code for a singular post
public class PostView extends MaterialCardView implements View.OnClickListener {
    private TextView txtTitle;
    private ImageView imgThumbnail;
    private TextView txtScore;
    private TextView txtComments;
    private ViewPostBinding binding;
    private Submission post;
    private Drawable thumbnail;
    private Comment comment;

    // https://medium.com/@Sserra90/android-writing-a-compound-view-1eacbf1957fc
    public PostView(Context context) {
        super(context);
        this.setOnClickListener(this);
        init();
    }

    void init() {


        binding = ViewPostBinding.bind(inflate(getContext(),R.layout.view_post,this));
        txtTitle = binding.postTitle;
        imgThumbnail = binding.postImage;
        txtScore = binding.textScore;
        txtComments = binding.textComments;
        imgThumbnail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Test Click",post.getUrl());
//                Intent submissionIntent = new Intent(getContext(), MediaActivity.class);
//                submissionIntent.putExtra("Post", PostView.this.post); // This should transfer the post to SubmissionActivity
//                getContext().startActivity(submissionIntent);
                NavigationHandler.openLink(getContext(),post);
            }
        });



    }

    /**
     * Use this to set the data of the view
     * @param title posts title
     * @param uri thumbnail
     * @param score post score
     * @param comments number of comments
     */
    public void setData(String title, String uri, String score, String comments/*, String postID*/) {
        this.txtTitle.setText(title);

//        if (post.hasThumbnail()) {
//            Log.d("JAR img", uri);
//        }
        this.txtScore.setText(score);
        this.txtComments.setText(comments);
    }

    public void setPost(Submission post) {
        this.post = post;
        setData(post.getTitle(),post.getThumbnail(), String.valueOf(post.getScore()),""+post.getCommentCount()/*,post.getSubmission()*/);
//        imgThumbnail.setOnClickListener(this);
//        txtTitle.setOnClickListener(this);
        String previewUrl = "";
        if (post.hasThumbnail()) {
            // fallback image
            previewUrl = post.getThumbnail();
        }
        int largestWidth = 1080; // could be set from settings
        if (post.getPreview()!=null/* && post.getPreview().isEnabled()*/) {
            SubmissionPreview.ImageSet img = post.getPreview().getImages().get(0);
            for (SubmissionPreview.Variation variation : img.getResolutions()) {

                if (variation.getWidth() > largestWidth) {
                    break;
                }
                previewUrl = variation.getUrl();
            }

        }
        Glide.with(this).load(previewUrl).into(imgThumbnail);

    }

    /*public String getComments()
    {
        String x = post.getId();
        return x;
    }*/

    public void onClick(View v) {
            Log.d("Test Click","Clicked Post");
            Intent submissionIntent = new Intent(getContext(), SubmissionActivity.class);
            submissionIntent.putExtra("Post", this.post); // This should transfer the post to SubmissionActivity
            String x = post.getId();
            Log.d("this",x);
            getContext().startActivity(submissionIntent);
    }

    public Submission getPost() {
        return post;
    }
}
