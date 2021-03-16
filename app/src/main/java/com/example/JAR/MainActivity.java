package com.example.JAR;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.JAR.databinding.ActivitySubredditBinding;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends SubredditActivity implements NavigationView.OnNavigationItemSelectedListener {
    public MenuItem subUser;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.nav.setNavigationItemSelectedListener(this);
//        SubMenu users = binding.nav.getMenu().addSubMenu(5,5,0,"Users");
//        subUser = users.getItem();
//        com.example.JAR.databinding.
        if (App.getTokenStore().getUsernames().contains("mueea001")) {
            binding.nav.getMenu().add(0,187,0,"Secret Activity");
        }
        refreshLogins();
    }

    @Override
    protected void onPostCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Log.d("Item selected",item.getTitle().toString()+" "+item.getItemId());
        switch (item.getItemId()) {
            case R.id.nav_login: {
                NavigationHandler.openLogin(this);
                return false;
            }
            case 5:{

                if (item.getTitle().toString().equals("Guest")){
                    App.getAccountHelper().switchToUserless();
                } else {
                    App.getAccountHelper().switchToUser(item.getTitle().toString());
                }
                NavigationHandler.openMainActivity(this);
            }
            case 187: {
                Log.d("SECRET", "Open secret activity");
                NavigationHandler.openSecret(this);
            }
        }
        return false;
    }

    public void refreshLogins() {
        Log.d("Jar","Refreshing login menu");
        SubMenu users = binding.nav.getMenu().findItem(R.id.users_menu).getSubMenu();
        users.clear();
        String currentUser = getSharedPreferences(getPackageName().concat("users"), MODE_PRIVATE)
                .getString("lastUser","<userless>");

        for (String user: App.getTokenStore().getUsernames()) {
            MenuItem item = null;
            if (user.equals("<userless>")) {
                item = users.add(0,5,0,"Guest");
            } else {
                item = users.add(0,5,0,user);
            }
            if (user.equals(currentUser)) {
                Log.d("Jar","Icon set for "+user);
                item.setIcon(R.drawable.ic_baseline_account_circle_24);
            }
        }
    }


}
