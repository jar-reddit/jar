package com.example.JAR;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import net.dean.jraw.oauth.AuthManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.JAR.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WebView login = binding.webview;
        login.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Uri uri = Uri.parse(url);
                if (uri.getAuthority().equals("localhost")) {

                }
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.login);
    }
}
