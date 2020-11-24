package com.example.JAR;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.dean.jraw.models.Submission;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class SubmissionActivity extends AppCompatActivity {

    private Submission post;
    private TextView title;
    private ImageView image;
    private Drawable thumbnail;
    private TextView score;
    private TextView commentScore;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        getPost();
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
        //image.setImageResource(R.drawable.post.getThumbnail());
        title.setText(post.getTitle()); // This sets the title of the post to the one retrieved from the post variable
        TextView score = (TextView) findViewById(R.id.submissionScore);
        score.setText(String.valueOf(post.getScore()));
        commentScore.setText(""+post.getCommentCount());
/*        if (post.hasThumbnail()) {
            if (thumbnail==null) {
                    Log.d("Jar: GET", post.getThumbnail());
                    try {

                        thumbnail = Drawable.createFromStream((InputStream) new URL(post.getUrl()).getContent(), "src");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.post(() -> {
                        //imgThumbnail.setImageDrawable(thumbnail);
                        image.setImageDrawable(thumbnail);
                    });
                });
            } else {
                image.setImageDrawable(thumbnail);
            }
        }*/

    }





    /*public void getComments()
            // https://mattbdean.gitbooks.io/jraw/content/cookbook.html
    {
        RootCommentNode comment = redditClient.submission("need to find a way to pull the post id");
        // This line is used to retrieve the comments from the post
        Iterator<CommentNode<PublicContribution<?>>> it = root.walkTree().iterator();

        while (it.hasNext()) {
            PublicContribution<?> thing = it.next().getSubject();
            System.out.println(thing.getBody());
        }
    }*/
}
