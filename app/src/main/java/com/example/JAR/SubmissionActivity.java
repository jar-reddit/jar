package com.example.JAR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.JAR.databinding.ActivitySubmissionBinding;
import com.example.JAR.databinding.ViewCommentBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Comment;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.VoteDirection;
import net.dean.jraw.references.PublicContributionReference;
import net.dean.jraw.tree.CommentNode;
import net.dean.jraw.tree.RootCommentNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;


public class SubmissionActivity extends AppCompatActivity {

    private Submission post;
    private TextView title;
    private ImageView image;
    private ImageButton upVote;
    private ImageButton downVote;
    private Drawable thumbnail;
    private TextView score;
    private TextView commentScore;
    private ActivitySubmissionBinding views;
    private boolean hideDownload = true;


    public void onCreate(Bundle savedInstanceState) {
        views = ActivitySubmissionBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(views.getRoot());
        getPost();
        getComments();
        setContent();
        isUserless();
        isImagePost();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_submission, menu);
        if(hideDownload) {
            MenuItem item = menu.findItem(R.id.download);
            item.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                shareContent(post.getUrl());
                return true;
            case R.id.download:
                downloadImage(post.getUrl());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        upVote = (ImageButton) findViewById(R.id.Upvote);
        downVote = (ImageButton) findViewById(R.id.downvote);

        upVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upVoting(post);
                Log.d("Userless", String.valueOf(isUserless()));
            }
        });

        downVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downVoting(post);
                Log.d("Userless", String.valueOf(isUserless()));
            }
        });
    }

    public void getComments() {
        Background.execute(() -> {
            RedditClient redditClient = JRAW.getInstance(this);
            RootCommentNode root = redditClient.submission(post.getId()).comments();
            // This line is used to retrieve the comments from the post
            //Iterator<CommentNode<PublicContribution<?>>> it = root.walkTree().iterator();
            //root.getReplies();
            //while (it.hasNext()) {
            // PublicContribution<?> thing = it.next().getSubject();
            r_comments(root, views.commentList);
//            for (CommentNode comments : root.getReplies()) {
//                Log.d("comments", comments.getSubject().getBody());
//                runOnUiThread(() -> {
//                    ViewCommentBinding binding = ViewCommentBinding.inflate(getLayoutInflater());
//                    binding.username.setText(comments.getSubject().getAuthor());
//
//                    Comment comment = (Comment) comments.getSubject();
//                    binding.commentBody.setText(Html.fromHtml(comment.getBodyHtml(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
//                    BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(binding.commentBody);
//                    method.setOnLinkClickListener((textView, url) -> {
//                        Log.d("Clicked link", url);
//                        NavigationHandler.openLink(SubmissionActivity.this, url);
//                        return true;
//                    });
//
//
//                    views.commentList.addView(binding.getRoot());
//                });
//            }
            //}
        });
    }

    private void r_comments(CommentNode node, LinearLayout commentList) {
        if (node.getReplies().size() != 0) {
            for (Object commentO : node.getReplies()) {
                CommentNode commentN = (CommentNode) commentO;
                runOnUiThread(() -> {
                    ViewCommentBinding binding = ViewCommentBinding.inflate(getLayoutInflater());
                    binding.username.setText(commentN.getSubject().getAuthor());
                    Comment comment = (Comment) commentN.getSubject();
                    binding.commentBody.setText(Html.fromHtml(comment.getBodyHtml(), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL));
                    BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(binding.commentBody);
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
//                    binding.getRoot().setOnTouchListener((v, event) -> {
//                        if (binding.childComments.getVisibility() == View.VISIBLE) {
//                            binding.childComments.setVisibility(View.GONE);
//                        } else {
//                            binding.childComments.setVisibility(View.VISIBLE);
//                        }
//                        return true;
//                    });
                    commentList.addView(binding.getRoot());
                    r_comments(commentN, binding.childComments);
                });
            }
        }
//        for (Object comment : commentn.getReplies()) {
//            Comment comments = (Comment) comment;
//
//            Log.d("comments", comments.getSubject().getBody());
//            runOnUiThread(() -> {
//                ViewCommentBinding binding = ViewCommentBinding.inflate(getLayoutInflater());
//                binding.username.setText(comments.getSubject().getAuthor());
//
//                Comment comment = (Comment) comments.getSubject();
//                binding.commentBody.setText(Html.fromHtml(comment.getBodyHtml(), Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
//                BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(binding.commentBody);
//                method.setOnLinkClickListener((textView, url) -> {
//                    Log.d("Clicked link", url);
//                    NavigationHandler.openLink(SubmissionActivity.this, url);
//                    return true;
//                });
//
//
//                views.commentList.addView(binding.getRoot());
//            });
//        }
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
        //        return App.getAccountHelper().getReddit().getAuthMethod().isUserless();
        if(App.getAccountHelper().getReddit().getAuthManager().currentUsername().equalsIgnoreCase("<userless>"))
        {
            upVote.setEnabled(false);
            upVote.setVisibility(View.INVISIBLE);
            downVote.setEnabled(false);
            downVote.setVisibility(View.INVISIBLE);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void shareContent(String url)
    {
        Intent postIntent = new Intent();
        postIntent.setAction(Intent.ACTION_SEND);
        postIntent.putExtra(Intent.EXTRA_TEXT, url);
        postIntent.setType("text/html");

        Intent shareIntent = Intent.createChooser(postIntent, null);
        startActivity(shareIntent);
    }

    public void downloadImage(String URL){

        if (!checkStoragePermissions()) {
            return;
        }

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";

        final File directory = new File(dirPath);

        final String fileName = URL.substring(URL.lastIndexOf('/') + 1);

        Glide.with(this)
                .load(URL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                        Toast.makeText(SubmissionActivity.this, "Saving image to storage", Toast.LENGTH_SHORT).show();
                        saveImage(bitmap, directory, fileName);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable placeholder) {
                        super.onLoadFailed(placeholder);

                        Toast.makeText(SubmissionActivity.this, "Image download failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public Boolean checkStoragePermissions() {

        // This will return the current Status
        int externalStoragePerms = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (externalStoragePerms != PackageManager.PERMISSION_GRANTED) {

            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 1);
            return false;
        }

        return true;
    }

    private void saveImage(Bitmap image, File imageDirectory, String imageName) {


        if (!imageDirectory.exists()) {
            imageDirectory.mkdir();
        }
            File imageFile = new File(imageDirectory, imageName);
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                Toast.makeText(SubmissionActivity.this, "Image saved to storage", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(SubmissionActivity.this, "Error during save", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
    }

    public void isImagePost()
    {
       if(UrlDetector.detect(post.getUrl()).contains("image"))
       {
            hideDownload = false;
       }

    }
}
