package com.github.jarforreddit;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import net.dean.jraw.models.Submission;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private List<Submission> posts;
    public PostAdapter(List<Submission> posts) {
        this.posts=posts;
        Log.d("JAR", "New post adapter");
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("JAR", "onCreateViewHolder");
        return PostViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Submission current = posts.get(position);
        holder.bind(current);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
