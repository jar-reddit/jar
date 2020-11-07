package com.example.JAR;

import android.content.Context;
import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Flair;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

import java.util.List;
import java.util.UUID;

public class JRAW {
    static RedditClient init(Context context) {
        UserAgent ua = new UserAgent("android", context.getPackageName(),context.getString(R.string.app_version),"JARForReddit");
        Credentials creds = Credentials.userlessApp("fxPqP8pMEuu-Tw", UUID.randomUUID());
        NetworkAdapter na = new OkHttpNetworkAdapter(ua);
        RedditClient redditClient = OAuthHelper.automatic(na,creds);
        Log.d("JAR",redditClient.getHttp().getUserAgent().toString());
        return redditClient;
    }


}
