package com.example.JAR;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.JAR.databinding.ViewImageBinding;
import com.example.JAR.databinding.ViewMediaBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import net.dean.jraw.models.Submission;

import org.jetbrains.annotations.NotNull;

public class MediaActivity extends FragmentActivity {
    public ViewImageBinding binding;
    private SimpleExoPlayer player;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Submission submission = (Submission) getIntent().getExtras().get("Post");
        
        if (getIntent().getExtras().get("type").equals("image")) {
            binding.getRoot().removeView(binding.videoPlayer);
            Glide
                    .with(this)
                    .load(getIntent().getExtras().get("url"))
                    .into(binding.subImage);
        } else if(getIntent().getExtras().get("type").equals("video")) {
            binding.getRoot().removeView(binding.subImage);
            player = new SimpleExoPlayer.Builder(this).build();
            MediaItem video = MediaItem.fromUri(getIntent().getExtras().get("url").toString());
            binding.videoPlayer.setPlayer(player);
            player.setMediaItem(video);
            player.prepare();
            player.play();
        }
        
//        Log.d("Post Hint", submission.getPostHint());
//        if (submission.getPostHint().equals("image")) {
//            binding.getRoot().removeView(binding.videoPlayer);
//            Glide
//                    .with(this)
//                    .load(submission.getUrl())
//                    .error(Glide.with(this).load(submission.getPreview().getImages().get(0).getSource().getUrl()))
//                    .into(binding.subImage)
//            ;
//        } else if (submission.getPostHint().contains("video")) {
//            Log.d("Playing Video",submission.getUrl());
//            binding.getRoot().removeView(binding.subImage);
//            player = new SimpleExoPlayer.Builder(this).build();
//            if (submission.getPostHint().contains("hosted")) {
//                String url = submission.getEmbeddedMedia().getRedditVideo().getDashUrl();
//                MediaItem video = MediaItem.fromUri(url);
//                binding.videoPlayer.setPlayer(player);
//                player.setMediaItem(video);
//                player.prepare();
//                player.play();
//            } else {
//                errorMessage(submission.getPostHint(),submission.getUrl());
//            }
//        } else {
//            errorMessage(submission.getPostHint(),submission.getUrl());
//        }
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.release();
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    private void errorMessage(String postHint, String url) {
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MediaActivity.this.finish();
            }
        });
        if (postHint.contains("video")) {
            errorDialogBuilder.setTitle("Playback error")
                    .setMessage("Can't play videos of type: " + postHint +
                            "\n \n Url: " + url)
                    .show();
        } else {
            errorDialogBuilder.setTitle("Unhandled type")
                    .setMessage("Unhandled of type: " + postHint +
                            "\n \n Url: " + url)
                    .show();
        }
    }
}
