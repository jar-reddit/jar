package com.example.JAR;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.JAR.databinding.ActivitySubmissionBinding;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
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


    public void onCreate(Bundle savedInstanceState)
    {
        views = ActivitySubmissionBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(views.getRoot());
        getPost();
        getComments();
        setContent();
    }

    private void getPost()
    {
        Bundle extras = getIntent().getExtras();
        post = (Submission) extras.get("Post");
    }

    private void setContent()
    {
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
                .into(image);
        title.setText(post.getTitle()); // This sets the title of the post to the one retrieved from the post variable
        TextView score = (TextView) findViewById(R.id.submissionScore);
        score.setText(String.valueOf(post.getScore()));
        commentScore.setText(""+post.getCommentCount());
        if (post.isSelfPost()) {
            views.submissionSelfText.setText(Html.fromHtml(post.getSelfTextHtml(),Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
//            views.submissionSelfText.setMovementMethod(BetterLinkMovementMethod.linkifyHtml(views.submissionSelfText));
            BetterLinkMovementMethod method = BetterLinkMovementMethod.linkifyHtml(views.submissionSelfText);
            method.setOnLinkClickListener((textView,url)->{
                // TODO: 02/03/2021 handle the url passed here 
                Log.d("Clicked link",url);
                return true;
            });
        }
        views.submissionUser.setText(post.getAuthor());

    }

    public void getComments()
    {
        Background.execute(()->{
            RedditClient redditClient = JRAW.getInstance(this);
            RootCommentNode root = redditClient.submission(post.getId()).comments();
            // This line is used to retrieve the comments from the post
            //Iterator<CommentNode<PublicContribution<?>>> it = root.walkTree().iterator();
            //root.getReplies();
            //while (it.hasNext()) {
               // PublicContribution<?> thing = it.next().getSubject();
            for(CommentNode comments: root.getReplies()) {
                Log.d("comments", comments.getSubject().getBody());
            }
                //}
        });
    }
}
