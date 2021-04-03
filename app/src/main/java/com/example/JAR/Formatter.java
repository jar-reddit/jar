package com.example.JAR;

import android.content.Context;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import com.x5.template.filters.FilterArgs;
import com.x5.template.filters.ObjectFilter;

import net.dean.jraw.models.Comment;
import net.dean.jraw.models.PublicContribution;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Votable;

import java.time.format.DateTimeFormatter;
import java.util.Date;


public class Formatter {
    private static Chunk chunk;

    private static void init() {
        if (chunk==null) {
            Theme theme = new Theme();
            theme.registerFilter(new DateFilter());
            chunk = theme.makeChunk();
        }

    }

    private static void addSubmissionTags(Submission post) {
        chunk.set("title", post.getTitle());
        chunk.set("flair", post.getLinkFlairText());
        chunk.set("author_flair", post.getAuthorFlairText());
        chunk.set("self_text_html", post.getSelfTextHtml());
        chunk.set("self_text", post.getSelfText());
        chunk.set("domain", post.getDomain());
        chunk.set("time",post.getCreated());
        chunk.set("url",post.getUrl());
    }

    /**
     * Formats string for posts({@link Submission}) or {@link Comment}s
     * @param context the context
     * @param string  the string to be formatted (template)
     * @param post    the {@link Submission} or {@link Comment}
     * @return        The formatted string
     */
    public static String formatString(Context context, String string, PublicContribution<?> post) {
        init();
        chunk.set("author", post.getAuthor());
        chunk.set("id", post.getId());
        chunk.set("score", post.getScore());
        chunk.set("subreddit", post.getSubreddit());
        chunk.set("subreddit_full", post.getSubredditFullName());
        chunk.set("myvote", post.getVote().toString());
        chunk.set("gold_no", post.getGilded());
        chunk.set("edit_time", post.getEdited());
        chunk.set("sticky", post.isStickied());
        if (post instanceof Comment) {
            addCommentTags((Comment)post);
        } else if (post instanceof Submission) {
            addSubmissionTags((Submission) post);
        }
        String formattedStr = formatString(context,string);
        return formattedStr;
    }

    private static class DateFilter extends ObjectFilter{

        @Override
        public String getFilterName() {
            return "date";
        }

        @Override
        public Object transformObject(Chunk chunk, Object object, FilterArgs args) {
            if (object instanceof Date){
                Date date = (Date) object;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(args.getUnresolvedArgs()[0]);
                    return sdf.format(date);
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
            return "wrong type: "+object.getClass().getSimpleName();
        }


    }

    private static void addCommentTags(Comment post) {
        chunk.set("self_text",post.getBody());
        chunk.set("self_text_html",post.getBodyHtml());
        chunk.set("time",post.getCreated());
        chunk.set("controversial",post.getControversiality());
        chunk.set("url",post.getUrl());
    }
    
    public static String formatString(Context context, String string) {
        init();
        chunk.setMultiple(Settings.getSettings(context).toMap());
        chunk.resetTemplate();
        chunk.append(string);
        String formattedStr = chunk.toString();
        chunk.resetTemplate();
        chunk=null;
        return formattedStr;
    }
    
}
