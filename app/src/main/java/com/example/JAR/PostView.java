package com.example.JAR;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.JAR.databinding.ViewPostBinding;

import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
import net.dean.jraw.tree.RootCommentNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
// This is the code for a singular post
public class PostView extends LinearLayout implements View.OnClickListener {
    private TextView txtTitle;
    private ImageView imgThumbnail;
    private TextView txtScore;
    private TextView txtComments;
    private ViewPostBinding binding;
    private Submission post;
    private Drawable thumbnail;

    // https://medium.com/@Sserra90/android-writing-a-compound-view-1eacbf1957fc
    public PostView(Context context) {
        super(context);

        init();
    }

    void init() {


        binding = ViewPostBinding.bind(inflate(getContext(),R.layout.view_post,this));
        txtTitle = binding.postTitle;
        imgThumbnail = binding.postImage;
        txtScore = binding.textScore;
        txtComments = binding.textComments;


    }

    /**
     * Use this to set the data of the view
     * @param title posts title
     * @param uri thumbnail
     * @param score post score
     * @param comments number of comments
     */
    public void setData(String title, String uri, String score, String comments) {
        this.txtTitle.setText(title);

//        if (post.hasThumbnail()) {
//            Log.d("JAR img", uri);
//        }
        this.txtScore.setText(score);
        this.txtComments.setText(comments);
    }

    public void setPost(Submission post) {
        this.post = post;
        setData(post.getTitle(),post.getThumbnail(), String.valueOf(post.getScore()),""+post.getCommentCount());
    }

    public void onClick(View v) {
            Log.d("Test Click","Clicked Post");
    }

    public void setThumbnail(Drawable d) {
        thumbnail=d;
//        imgThumbnail.setMinimumWidth(LayoutParams.MATCH_PARENT);
    }

    public Submission getPost() {
        return post;
    }


    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (post.hasThumbnail()) {
            if (thumbnail==null) {
                MainActivity.testExecutor.execute(() -> {

                    Log.d("Jar: GET", post.getThumbnail());
                    try {

                        thumbnail = Drawable.createFromStream((InputStream) new URL(post.getUrl()).getContent(), "src");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.post(() -> {
                        imgThumbnail.setImageDrawable(thumbnail);
                        imgThumbnail.setOnClickListener(this);
                    });
                });
            } else {
                imgThumbnail.setImageDrawable(thumbnail);
            }
        }

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
