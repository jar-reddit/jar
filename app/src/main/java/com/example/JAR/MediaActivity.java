package com.example.JAR;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.JAR.databinding.ViewImageBinding;
import com.example.JAR.databinding.ViewMediaBinding;

import net.dean.jraw.models.Submission;

public class MediaActivity extends AppCompatActivity {
    public ViewImageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Submission submission = (Submission) getIntent().getExtras().get("Post");
        Glide.with(this).load(submission.getUrl()).into(binding.subImage);
    }
}
