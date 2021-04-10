package com.github.jarforreddit;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jarforreddit.R;
import com.github.jarforreddit.databinding.ActivityLoginBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.oauth.StatefulAuthHelper;

/**
 * Activity for handling the login to reddit
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    /**
     * Load login screen with contents
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final StatefulAuthHelper helper = JRAW.getLoginHelper();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WebView login = binding.webview;
        login.getSettings().setUserAgentString(App.getUserAgent().toString());
        login.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("Opening URL", url);
                if (helper.isFinalRedirectUrl(url)) {
                    view.stopLoading();
                    Log.d("JAR", url);
                    Background.execute(() -> {
                        RedditClient reddit = helper.onUserChallenge(url);
                        NavigationHandler.openMainActivity(LoginActivity.this);
                    });
                }
            }
        });
        CookieManager.getInstance().removeSessionCookies(value -> {

        });
        CookieManager.getInstance().flush();
        login.loadUrl(helper.getAuthorizationUrl(true,
                true,
                "read", "vote", "identity"
        ));

    }

    /**
     * Set the title of the activity when the view is displayed
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.login);
    }
}
