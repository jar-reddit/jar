package com.github.jarforreddit;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jarforreddit.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchView1.setSubmitButtonEnabled(true);
        binding.searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                WebView search = binding.searchView;
                search.getSettings().setJavaScriptEnabled(true);
                search.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        Uri uri = Uri.parse(url);
//                        if (uri.getAuthority().contains("reddit.com")) {
//                            NavigationHandler.openUri(Uri.parse(url), SearchActivity.this);
//                        }
//                        super.onPageFinished(view, url);
//                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        Uri uri = Uri.parse(url);
                        if (uri.getAuthority().contains("reddit.com")) {
                            NavigationHandler.openUri(Uri.parse(url), SearchActivity.this);
                        }
                        super.onPageStarted(view, url, favicon);
                    }
                });
                search.loadUrl("https://google.co.uk/search?q="+ Uri.encode("site:reddit.com "+query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
