package com.example.JAR;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public static PostViewHolder create(ViewGroup parent, Context context) {
        // TODO: 19/11/20 What does this do? 
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_post, parent, false);
        return new PostViewHolder(new PostView(context));
    }
}
