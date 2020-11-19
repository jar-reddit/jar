package com.example.JAR;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.dean.jraw.models.Submission;


public class PostViewHolder extends RecyclerView.ViewHolder {
    private final PostView postItemView;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postItemView = (PostView) itemView;
    }

    public void bind(Submission post) {
        postItemView.setPost(post);
        Log.d("JAR:PostViewHolder", "set the post for " +post.getId());
    }

    public static PostViewHolder create(ViewGroup parent) {
        PostView view = new PostView(parent.getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new PostViewHolder(view);
    }
}
