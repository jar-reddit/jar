package com.github.jarforreddit;

import android.content.Context;
import android.content.SharedPreferences;

import net.dean.jraw.RedditClient;
import net.dean.jraw.android.SharedPreferencesTokenStore;
import net.dean.jraw.oauth.AccountHelper;
import net.dean.jraw.oauth.StatefulAuthHelper;

/**
 * Class that handles connection with the JRAW library
 */
public class JRAW {
    private static RedditClient INSTANCE = null; // store the reddit instance

    /**
     * A login helper that will use JRAW to log the user in
     *
     * @return auth helper that can be used to log a user in
     */
    public static StatefulAuthHelper getLoginHelper() {
        return App.getAccountHelper().switchToNewUser();
    }

    /**
     * Singleton that creates a RedditClient if it doesn't exist and returns it.
     *
     * @param context context of the activity or view
     * @return the current instance of RedditClient
     */
    public static RedditClient getInstance(Context context) {
        SharedPreferencesTokenStore tokenStore = App.getTokenStore();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName().concat("users"), Context.MODE_PRIVATE);
        AccountHelper accountHelper = App.getAccountHelper();
        if (INSTANCE == null) {
            if (tokenStore.getUsernames().size() <= 1) {
                accountHelper.switchToUserless();
            } else {
                accountHelper.trySwitchToUser(sharedPreferences.getString("lastUser", "<userless>"));
            }
        }
        INSTANCE = App.getAccountHelper().getReddit();
        return INSTANCE;
    }
}
