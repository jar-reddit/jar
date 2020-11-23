package com.example.JAR;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.JAR.RAW.Submission;

public class SubmissionActivity extends AppCompatActivity {

    private Submission post;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

    }

    private void getPost()
    {
        Bundle extras =getIntent().getExtras();
        post = (Submission) extras.get("Post");
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
