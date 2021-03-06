package com.github.jarforreddit;

import android.content.Context;

import com.github.jarforreddit.R;
import com.github.jarforreddit.databinding.ViewPostBinding;
import com.google.android.material.card.MaterialCardView;

// This is the code for a singular post
public class PostView extends MaterialCardView {
    private final ViewPostBinding binding;

    public PostView(Context context) {
        super(context);
        binding = ViewPostBinding.bind(inflate(getContext(), R.layout.view_post, this));
    }

    public ViewPostBinding getBinding() {
        return binding;
    }
}
