package com.example.JAR;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.JAR.databinding.ActivitySubmissionBinding;
import com.example.JAR.databinding.ViewCommentBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.VoteDirection;
import net.dean.jraw.references.PublicContributionReference;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;


public class SubmissionActivity extends AppCompatActivity {

    private Submission post;
    private TextView title;
    private ImageView image;
    private Drawable thumbnail;
    private TextView score;
    private TextView commentScore;
    //private String postID;
    private ActivitySubmissionBinding views;
    private String titleFormat;
    private String bodyFormat;


    public void onCreate(Bundle savedInstanceState) {
        titleFormat = Settings.getSettings(this).getString("comments.title");
        bodyFormat = Settings.getSettings(this).getString("comments.body");
        views = ActivitySubmissionBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(views.getRoot());
        getPost();
        getComments();
        setContent();
    }

    private void getPost() {
        Bundle extras = getIntent().getExtras();
        post = (Submission) extras.get("Post");
    }

    private void setContent() {
        setTitle(post.getSubreddit());
        views.userAvatar.setVisibility(View.GONE);
        TextView title = (TextView) findViewById(R.id.submissionTitle);
        ImageView image = (ImageView) findViewById(R.id.submissionImage);

        TextView commentScore = (TextView) findViewById(R.id.commentScore);
        Glide.with(this)
                .load(post.getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        image.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .centerCrop()
                .into(image);
        image.setOnClickListener((view) -> {
            NavigationHandler.openLink(this, post);
        });
        image.setAdjustViewBounds(true);
        title.setText(post.getTitle()); // This sets the title of the post to the one retrieved from the post variable
        TextView score = (TextView) findViewById(R.id.submissionScore);
        score.setText(String.valueOf(post.getScore()));
        commentScore.setText("" + post.getCommentCount());
        if (post.isSelfPost() && post.getSelfTextHtml() != null) {
            views.submissionSelfText.setText(Html.fromHtml(post.getSelfTextHtml(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
//            views.submissionSelfText.setMovementMethod(BetterLinkMovementMethod.linkifyHtml(views.submissionSelfText));
            BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(views.submissionSelfText);
            method.setOnLinkClickListener((textView, url) -> {
                Log.d("Clicked link", url);
                NavigationHandler.openLink(SubmissionActivity.this, url);
                return true;
            });
        }
        views.submissionUser.setText(post.getAuthor());
        ImageButton upVote = (ImageButton) findViewById(R.id.Upvote);
        ImageButton downVote = (ImageButton) findViewById(R.id.downvote);
        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upVoting(post);
            }
        });

        downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downVoting(post);
            }
        });
    }

    public void getComments() {
        Background.execute(() -> {
            RedditClient redditClient = JRAW.getInstance(this);
            RootCommentNode root = redditClient.submission(post.getId()).comments();
            r_comments(root, views.commentList);
        });
    }

    /**
     * Recursive method that walks the tree similar to pre-order traversal
     * @param node  the root node on which you want to start
     * @param commentList the view holder for children comments
     */
    private void r_comments(CommentNode node, LinearLayout commentList) {
        // TODO: 04/04/2021 use a recycler view here
        if (node.getReplies().size() != 0) {
            for (Object commentO : node.getReplies()) {
                CommentNode commentN = (CommentNode) commentO;
                runOnUiThread(() -> {
                    ViewCommentBinding binding = ViewCommentBinding.inflate(getLayoutInflater());
                    Comment comment = (Comment) commentN.getSubject();
                    binding.username.setText(Html.fromHtml(Formatter.formatString(this, titleFormat, comment), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                    binding.commentBody.setText(Html.fromHtml(Formatter.formatString(this, bodyFormat, comment), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                    BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(this);
                    method.setOnLinkClickListener((textView, url) -> {
                        Log.d("Clicked link", url);
                        NavigationHandler.openLink(SubmissionActivity.this, url);
                        return true;
                    });
                    binding.getRoot().setOnClickListener(v -> {
                        if (binding.childComments.getVisibility() == View.VISIBLE) {
                            binding.childComments.setVisibility(View.GONE);
                        } else {
                            binding.childComments.setVisibility(View.VISIBLE);
                        }
                    });
                    commentList.addView(binding.getRoot());
                    r_comments(commentN, binding.childComments);
                });
            }
        }
    }

    private void upVoting(Submission post) {
        if (isUserless()) {
            Toast.makeText(getApplicationContext(), "You do not have voting privileges, please log in or message the moderator", Toast.LENGTH_SHORT).show();
        } else {
            Background.execute(() -> {
                PublicContributionReference pcr = post.toReference(JRAW.getInstance(this));
                if (post.getVote() == VoteDirection.UP) {
                    pcr.unvote();
                } else {
                    pcr.setVote(VoteDirection.UP);
                    Log.d("Secret Ballot", "voted up yo");
                }
            });
        }
    }

    private void downVoting(Submission post) {
        if (isUserless()) {
            Toast.makeText(getApplicationContext(), "You do not have voting privileges, please log in or message the moderator", Toast.LENGTH_SHORT).show();
        } else {
            Background.execute(() -> {
                PublicContributionReference pcr = post.toReference(JRAW.getInstance(this));
                if (post.getVote() == VoteDirection.DOWN) {
                    pcr.unvote();
                } else {
                    pcr.setVote(VoteDirection.DOWN);
                    Log.d("Secret Ballot", "voted down yo");
                }
            });
        }
    }

    public boolean isUserless() {
        return App.getAccountHelper().getReddit().getAuthMethod().isUserless();
    }
}
