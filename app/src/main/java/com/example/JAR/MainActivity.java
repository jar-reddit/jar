package com.example.JAR;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.JAR.databinding.ActivitySubredditBinding;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends SubredditActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.nav.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_login: {
                NavigationHandler.openLogin(this);
            }
        }
        return false;
    }
}
