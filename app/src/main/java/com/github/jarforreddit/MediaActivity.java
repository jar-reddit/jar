package com.github.jarforreddit;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.github.jarforreddit.R;
import com.github.jarforreddit.databinding.ViewImageBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import org.jetbrains.annotations.NotNull;

public class MediaActivity extends FragmentActivity {
    public ViewImageBinding binding;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras().get("type").equals("image")) {
            // handle images
            binding.getRoot().removeView(binding.videoPlayer); // remove video player

            // Load image using Glide
            Glide
                    .with(this)
                    .load(getIntent().getExtras().get("url"))
                    .into(binding.subImage);
        } else if (getIntent().getExtras().get("type").equals("video")) {
            // handle video
            binding.getRoot().removeView(binding.subImage); // remove image view

            // Get exoplayer to play video in url
            player = new SimpleExoPlayer.Builder(this).build();
            MediaItem video = MediaItem.fromUri(getIntent().getExtras().get("url").toString());
            binding.videoPlayer.setPlayer(player);
            player.setMediaItem(video);
            player.prepare();
            player.play();
            player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        }
    }

    /**
     * Destroy the player when the user closes this activity
     */
    @Override
    protected void onDestroy() {
        if (player != null) {
            player.release();
        }
        super.onDestroy();
    }

    /**
     * do nothing when orientation is changed
     *
     * @param newConfig the new config
     */
    @Override
    public void onConfigurationChanged(@NonNull @NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * pause player when the activity stops like when the screen is off
     */
    @Override
    protected void onStop() {
        if (player != null) {
            player.pause();
        }
        super.onStop();
    }

    private void errorMessage(String postHint, String url) {
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(this);
        errorDialogBuilder
                .setNeutralButton(R.string.ok, (dialog, which) -> MediaActivity.this.finish());
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
