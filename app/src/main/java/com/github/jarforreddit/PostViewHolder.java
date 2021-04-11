package com.github.jarforreddit;

import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.jarforreddit.databinding.ViewPostBinding;

import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubmissionPreview;
import net.dean.jraw.models.VoteDirection;
import net.dean.jraw.oauth.AuthManager;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;


public class PostViewHolder extends RecyclerView.ViewHolder {
    private final PostView postItemView;
    private ViewPostBinding viewPostBinding;
    private VoteDirection vote;
    private Submission post;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postItemView = (PostView) itemView;
        viewPostBinding = ViewPostBinding.bind(postItemView);
    }

    public static PostViewHolder create(ViewGroup parent) {
        PostView view = new PostView(parent.getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new PostViewHolder(view);
    }

    public void bind(Submission post) {
        setUnvoteColour();
        this.post = post;
        String previewUrl = "";
        if (post.hasThumbnail()) {
            // fallback image
            previewUrl = post.getThumbnail();
        }
        int largestWidth = 1080; // could be set from settings
        if (post.getPreview() != null/* && post.getPreview().isEnabled()*/) {
            SubmissionPreview.ImageSet img = post.getPreview().getImages().get(0);
            for (SubmissionPreview.Variation variation : img.getResolutions()) {
                if (variation.getWidth() > largestWidth) {
                    break;
                }
                previewUrl = variation.getUrl();
            }
        }
        Glide.with(viewPostBinding.getRoot()).load(previewUrl).into(viewPostBinding.postImage);
        viewPostBinding.getRoot().setOnClickListener((view) -> {
            NavigationHandler.openSubmission(post, view.getContext());
        });

        viewPostBinding.postImage.setOnClickListener(view -> NavigationHandler.openLink(view.getContext(), post));

        BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(viewPostBinding.otherInfo);
        method.setOnLinkClickListener((textView, url) -> {
            Log.d("Clicked link", url);
            NavigationHandler.openLink(textView.getContext(), url);
            return true;
        });
        // TODO: 18/03/2021 Handle voting when user is offline
        viewPostBinding.postUpvoteButton.setOnClickListener((view) -> {
            upvote();
            vote(vote);
        });
        viewPostBinding.postDownvoteButton.setOnClickListener((view) -> {
            downvote();
            vote(vote);
        });
        vote = post.getVote();
        setVoteColour();
        viewPostBinding.textScore.setText(String.valueOf(post.getScore()));
        viewPostBinding.textComments.setText(String.valueOf(post.getCommentCount()));
        String format = Settings.getSettings(postItemView.getContext()).getString("posts.format");

        viewPostBinding.otherInfo.setText(Html
                .fromHtml(Formatter
                        .formatString(
                                postItemView.getContext(),
                                format,
                                post
                        ), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
        viewPostBinding.postTitle.setText(Html.fromHtml(
                Formatter.formatString(postItemView.getContext(),
                        Settings.getSettings(postItemView.getContext()).getString("posts.title"),
                        post
                ),
                Html.FROM_HTML_MODE_COMPACT));
    }

    private void upvote() {
        if (vote.equals(VoteDirection.UP)) {
            setUnvoteColour();
            vote = VoteDirection.NONE;
        } else {
            setUpvoteColour();
            vote = VoteDirection.UP;
        }
    }

    private void downvote() {
        if (vote.equals(VoteDirection.DOWN)) {
            setUnvoteColour();
            vote = VoteDirection.NONE;
        } else {
            setDownvoteColour();
            vote = VoteDirection.DOWN;
        }
    }

    private void setUpvoteColour() {

        viewPostBinding.postUpvoteButton.setColorFilter(Color.parseColor(Settings.getSettings(postItemView.getContext()).getString("general.upvote")));
        viewPostBinding.postDownvoteButton.setColorFilter(0xFFFFFF);
    }

    private void setDownvoteColour() {
        viewPostBinding.postDownvoteButton.setColorFilter(Color.parseColor(Settings.getSettings(postItemView.getContext()).getString("general.downvote")));
        viewPostBinding.postUpvoteButton.setColorFilter(0xFFFFFF);
    }

    private void setUnvoteColour() {
        viewPostBinding.postUpvoteButton.setColorFilter(0xFFFFFF);
        viewPostBinding.postDownvoteButton.setColorFilter(0xFFFFFF);
    }

    private void hideVoting() {
        viewPostBinding.postUpvoteButton.setVisibility(View.INVISIBLE);
        viewPostBinding.postDownvoteButton.setVisibility(View.INVISIBLE);
    }

    private void setVoteColour() {
        if (!JRAW.getInstance(postItemView.getContext()).getAuthManager().currentUsername().equals(AuthManager.USERNAME_USERLESS)) {
            if (vote.equals(VoteDirection.UP)) {
                setUpvoteColour();
            } else if (vote.equals(VoteDirection.DOWN)) {
                setDownvoteColour();
            }
        } else {
            hideVoting();
        }
    }

    private void vote(VoteDirection direction) {
        Background.execute(() -> {
            post.toReference(JRAW.getInstance(postItemView.getContext())).setVote(direction);
        });
    }
}
