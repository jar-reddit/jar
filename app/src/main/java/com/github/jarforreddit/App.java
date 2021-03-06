package com.github.jarforreddit;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import net.dean.jraw.android.AndroidHelper;
import net.dean.jraw.android.AppInfo;
import net.dean.jraw.android.AppInfoProvider;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.android.SimpleAndroidLogAdapter;
import net.dean.jraw.http.LogAdapter;
import net.dean.jraw.http.SimpleHttpLogger;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.AccountHelper;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class App extends Application {
    private static AccountHelper accountHelper;
    private static SharedPreferencesTokenStore tokenStore;
    private static UserAgent ua;

    public static AccountHelper getAccountHelper() {
        return accountHelper;
    }

    public static SharedPreferencesTokenStore getTokenStore() {
        return tokenStore;
    }

    public static UserAgent getUserAgent() {
        return ua;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        AppInfoProvider provider = new AppInfoProvider() {
            @NotNull
            @Override
            public AppInfo provide() {
                Context context = getApplicationContext();
                ua = new UserAgent(
                        "android",
                        context.getPackageName(),
                        BuildConfig.VERSION_NAME
                                .concat("-")
                                .concat(BuildConfig.BUILD_TYPE),
                        "JARForReddit");
                return new AppInfo("fxPqP8pMEuu-Tw",
                        "http://localhost:8080/",
                        ua
                );
            }
        };
        UUID uuid;
        String strUUID = getSharedPreferences(getPackageName(), MODE_PRIVATE)
                .getString("uuid","");
        if (strUUID.equals("")) {
            uuid = UUID.randomUUID();
            getSharedPreferences(getPackageName(),MODE_PRIVATE)
                    .edit()
                    .putString("uuid",uuid.toString())
                    .apply();
        } else {
            uuid = UUID.fromString(strUUID);
        }
        Log.d("UUID",uuid.toString());

        // Store our access tokens and refresh tokens in shared preferences
        tokenStore = new SharedPreferencesTokenStore(getApplicationContext());
        // Load stored tokens into memory
        tokenStore.load();
        // Automatically save new tokens as they arrive
        tokenStore.setAutoPersist(true);

        // An AccountHelper manages switching between accounts and into/out of userless mode.
        accountHelper = AndroidHelper.accountHelper(provider, uuid, tokenStore);
        Settings.getInstance(getApplicationContext());
        switch (Settings.getSettings(getApplicationContext()).getString("general.theme")) {
            case "light":

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "system":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        // Every time we use the AccountHelper to switch between accounts (from one account to
        // another, or into/out of userless mode), call this function
        accountHelper.onSwitch(redditClient -> {
            // By default, JRAW logs HTTP activity to System.out. We're going to use Log.i()
            // instead.

            try {
                getSharedPreferences(getPackageName().concat("users"), MODE_PRIVATE)
                        .edit()
                        .putString("lastUser", redditClient
                                .me()
                                .getUsername())
                        .apply();
                Log.d("Current user", redditClient.me().getUsername());
            } catch (IllegalStateException e) {
                getSharedPreferences(getPackageName().concat("users"), MODE_PRIVATE)
                        .edit()
                        .putString("lastUser", "<userless>")
                        .apply();
                Log.d("Current user", "<userless>");
            }
            LogAdapter logAdapter = new SimpleAndroidLogAdapter(Log.INFO);

            // We're going to use the LogAdapter to write down the summaries produced by
            // SimpleHttpLogger
            redditClient.setLogger(
                    new SimpleHttpLogger(SimpleHttpLogger.DEFAULT_LINE_LENGTH, logAdapter));

            // If you want to disable logging, use a NoopHttpLogger instead:
            // redditClient.setLogger(new NoopHttpLogger());
            return null;
        });
        Log.d("Jar", "usernames: " + tokenStore.getUsernames().toString());


    }

    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
        if (Looper.getMainLooper().isCurrentThread()) {
            NavigationHandler.openCrash(getApplicationContext(), e);
        } else {
            new Handler(Looper.getMainLooper()).post(() -> {
                Intent crashIntent = new Intent(getApplicationContext(),CrashActivity.class);
                crashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                crashIntent.putExtra("title",e.getClass().getSimpleName());
                crashIntent.putExtra("message",e.getMessage());
                startActivity(crashIntent);
            });
        }
    };

}
