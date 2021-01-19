package com.example.JAR;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.JAR.RAW.Reddit;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Flair;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.oauth.StatefulAuthHelper;

import java.util.List;
import java.util.UUID;

import static java.sql.Types.NULL;

public class JRAW {
    private static RedditClient INSTANCE = null;
     static RedditClient init(Context context) {

//        UserAgent ua = new UserAgent("android", context.getPackageName(),context.getString(R.string.app_version),"JARForReddit");
//        Credentials creds = Credentials.userlessApp("fxPqP8pMEuu-Tw", UUID.randomUUID());
//        NetworkAdapter na = new OkHttpNetworkAdapter(ua);
//        RedditClient redditClient = OAuthHelper.automatic(na,creds);
//        Log.d("JAR",redditClient.getHttp().getUserAgent().toString());
         App.getAccountHelper().switchToUserless();

        return App.getAccountHelper().getReddit();
    }

    public static StatefulAuthHelper getLoginHelper() {
        StatefulAuthHelper authHelper = App.getAccountHelper().switchToNewUser();
        return authHelper;
    }

    public static void setINSTANCE(RedditClient reddit) {
         INSTANCE = reddit;
    }



    public static RedditClient getInstance(Context context)
    {
        SharedPreferencesTokenStore tokenStore = App.getTokenStore();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName().concat("users"), Context.MODE_PRIVATE);
        AccountHelper accountHelper = App.getAccountHelper();
        if (INSTANCE==null) {
            if (tokenStore.getUsernames().size()<=1) {
                accountHelper.switchToUserless();
            } else {
                accountHelper.trySwitchToUser(sharedPreferences.getString("lastUser","<userless>"));
            }
        }
        INSTANCE = App.getAccountHelper().getReddit();
        return INSTANCE;
    }
}
