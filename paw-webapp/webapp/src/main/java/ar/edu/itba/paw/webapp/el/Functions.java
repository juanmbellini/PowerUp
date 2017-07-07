package ar.edu.itba.paw.webapp.el;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Class containing taglib functions.
 */
public class Functions {

    public static final int MAX_COMMENT_DEPTH = 5;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYYY h:mm:ss a");

    public static String printCommentsTree(Collection<Comment> currentLevel, int depth, String baseURL, User currentUser) {
        if(depth > MAX_COMMENT_DEPTH || currentLevel.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder("<ul class='collection'>");
        for(Comment comment : currentLevel) {
            String escapedUsername = HtmlUtils.htmlEscape(comment.getCommenter().getUsername(), "UTF-8"),
                    escapedComment = HtmlUtils.htmlEscape(comment.getBody(), "UTF-8");
            long commentId = comment.getId();

            //Basic section
            builder.append("<li class='collection-item avatar'>")
                    .append("<a name='").append(comment.getId()).append("'></a>")
                    .append("<img src='").append(baseURL).append("profile-picture?username=").append(escapedUsername).append("' class='circle'>")
                    .append("<span class='title wrap-text'><a href='").append(baseURL).append("profile?username=").append(escapedUsername).append("'>").append(escapedUsername).append("</a></span>")
                    .append("<p class='wrap-text'>Submitted ").append(dateFormat.format(comment.getCreatedAt().getTime())).append("</p>")
                    .append("<br />")
                    .append("<p class='preserve-newlines wrap-text'>").append(escapedComment).append("</p>");
            //Action links
            if(currentUser != null) {
                builder.append("<!--Comment action links-->")
                    .append("<div class='action-links'>");
                    if(depth < MAX_COMMENT_DEPTH - 1) {
                        builder.append("<!--Reply link-->")
                                .append("<a href='#!' class='reply-link' data-comment-id='").append(commentId).append("' data-form-shown='false'>Reply</a>");
                    }
                if(currentUser.equals(comment.getCommenter())) {
                    builder.append("<!--Edit link-->");
                    if(depth < MAX_COMMENT_DEPTH - 1) {
                        builder.append(" | ");
                    }
                    builder.append("<a href='#!' class='edit-comment-link' data-comment-id='").append(commentId).append("' data-comment='").append(escapedComment).append("' data-form-shown='false'>Edit</a>")
                            .append("<!--Delete link-->")
                            .append(" | <a href='#!' class='delete-comment-link' data-comment-id='").append(commentId).append("' data-form-shown='false'>Delete</a>")
                            .append("</div>");
                }
                builder.append("<div class='action-form'></div>");
            }
            //Likes
            builder.append("<!--Un/like comment section-->")
                    .append("<span href='#!' class='secondary-content'><b>").append(comment.getLikeCount()).append("</b>&nbsp;&nbsp;");
            if(currentUser != null) {
                if(comment.isLikedBy(currentUser)) {
                    builder.append("<a href='#!' class='unlike-comment' data-comment-id='").append(commentId).append("'><i class='material-icons green-text'>thumb_up</i></a>");
                } else {
                    builder.append("<a href='#!' class='like-comment' data-comment-id='").append(commentId).append("'><i class='material-icons black-text'>thumb_up</i></a>");
                }
            } else {
                builder.append("<i class='material-icons black-text'>thumb_up</i>");
            }
            builder.append("</span>")
            .append("<!--End un/like comment section-->")
            .append(printCommentsTree(comment.getReplies(), depth+1, baseURL, currentUser))
            .append("</li>");
        }
        builder.append("</ul>");
        return builder.toString();
    }

    public static int min(int a, int b) {
        return Math.min(a, b);
    }
}
