package com.example.JAR;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.dean.jraw.models.Submission;


public class SubmissionActivity extends AppCompatActivity {

    private Submission post;
    private TextView title;
    private ImageView image;


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
        //title = extras.get("title");
    }

    private void setContent()
    {
        TextView title = (TextView) findViewById(R.id.submissionTitle);
        ImageView image = (ImageView) findViewById(R.id.submissionImage);
        //this.post = post;
        title.setText(post.getTitle()); // This sets the title of the post to the one retrieved from the post variable

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
