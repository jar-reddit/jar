package com.example.JAR;

import android.graphics.ColorFilter;
import android.os.Bundle;
import android.widget.ListAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.JAR.databinding.ActivitySecretBinding;

public class SecretActivity extends AppCompatActivity {
    private ActivitySecretBinding activity;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = ActivitySecretBinding.inflate(getLayoutInflater());
        setContentView(activity.getRoot());
        PostView p1 = new PostView(this);
            p1.getBinding().postTitle.setText("You'll never guess what the best app for reading Reddit will " +
                    "be | It's JAR For Reddit");
            p1.getBinding().otherInfo.setText("r/savedyouaclick - u/seeinthis - Spittin Facts");
            p1.getBinding().postDownvoteButton.setColorFilter(0xFFFFFF);
            p1.getBinding().textScore.setText("2565");
            p1.getBinding().textComments.setText("345");

        PostView p2 = new PostView(this);
            p2.getBinding().postTitle.setText("Bunch of Poster Fair attendees get rick-rolled");
            p2.getBinding().otherInfo.setText("r/news - u/rickastley - be careful what QR codes you scan");
            p2.getBinding().postDownvoteButton.setColorFilter(0xFFFFFF);
        p2.getBinding().textScore.setText("98354");
        p2.getBinding().textComments.setText("564");
            Glide
                    .with(this)
                    .load("https://nextcloud.mueed.co.uk/s/T6kc92GbqKQFFBD/preview")
                    .into(p2.getBinding().postImage);

        PostView p3 = new PostView(this);
        p3.getBinding().postTitle.setText("We are the creators of the best Reddit reader. Ask us anything.");
        p3.getBinding().otherInfo.setText("r/IAmA - u/JARForReddit - JARF or Reddit");
        p3.getBinding().postDownvoteButton.setColorFilter(0xFFFFFF);
        activity.getRoot().addView(p1);
        activity.getRoot().addView(p2);
        activity.getRoot().addView(p3);
    }
}
