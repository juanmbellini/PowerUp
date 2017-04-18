package ar.edu.itba.paw.webapp.model.validation;


import static ar.edu.itba.paw.webapp.model.validation.ValueError.ErrorCause.*;

/**
 * Class containing {@link ValueError} constants to be reused.
 * <p>
 * Created by Juan Marcos Bellini on 12/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ValueErrorConstants {


    private static final String ILLEGAL_SCORE_ERROR_MESSAGE = "The score must be between " +
            NumericConstants.MIN_SCORE + " and " + NumericConstants.MAX_SCORE + ".";


    public static final ValueError MISSING_USERNAME = new ValueError(MISSING_VALUE, "username",
            "The username is missing.");

    public static final ValueError USERNAME_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "username",
            "The username is too short.");

    public static final ValueError USERNAME_TOO_LONG = new ValueError(ILLEGAL_VALUE, "username",
            "The username is too long.");


    public static final ValueError MISSING_E_MAIL = new ValueError(MISSING_VALUE, "email", "The email is missing.");

    public static final ValueError E_MAIL_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "email", "The email is too short.");

    public static final ValueError E_MAIL_TOO_LONG = new ValueError(ILLEGAL_VALUE, "email", "The email is too long.");

    public static final ValueError INVALID_E_MAIL = new ValueError(ILLEGAL_VALUE, "email", "The email is not valid.");


    public static final ValueError MISSING_PASSWORD = new ValueError(MISSING_VALUE, "password",
            "The password is missing.");

    public static final ValueError PASSWORD_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "password",
            "The password is too short.");

    public static final ValueError PASSWORD_TOO_LONG = new ValueError(ILLEGAL_VALUE, "password",
            "The password is too long.");


    public static final ValueError MISSING_AUTHORITY = new ValueError(MISSING_VALUE, "authority",
            "The authority is missing");


    public static final ValueError MISSING_PICTURE = new ValueError(MISSING_VALUE, "picture",
            "The picture is missing");

    public static final ValueError PICTURE_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "picture",
            "The picture is too short");

    public static final ValueError PICTURE_TOO_LONG = new ValueError(ILLEGAL_VALUE, "picture",
            "The picture is too long");


    public static final ValueError MISSING_MIME_TYPE = new ValueError(MISSING_VALUE, "mimeType",
            "The mime type is missing");

    public static final ValueError MIME_TYPE_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "mimeType",
            "The mime type is too short");

    public static final ValueError MIME_TYPE_TOO_LONG = new ValueError(ILLEGAL_VALUE, "mimeType",
            "The mime type is too long");


    public static final ValueError MISSING_PLAY_STATUS = new ValueError(MISSING_VALUE, "playStatus",
            "The play status is missing");

    public static final ValueError PLAY_STATUS_ALREADY_SET = new ValueError(ALREADY_EXISTS, "(userId, gameId)",
            "The given user already set a play status for the given game. Remove first.");


    public static final ValueError MISSING_SCORE = new ValueError(MISSING_VALUE, "score",
            "The score is missing");

    public static final ValueError SCORE_BELOW_MIN = new ValueError(ILLEGAL_VALUE, "score",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError SCORE_ABOVE_MAX = new ValueError(ILLEGAL_VALUE, "score",
            ILLEGAL_SCORE_ERROR_MESSAGE);


    public static final ValueError MISSING_CREATOR = new ValueError(MISSING_VALUE, "creator",
            "The creator is missing.");


    public static final ValueError MISSING_TITLE = new ValueError(MISSING_VALUE, "title", "The title is missing.");

    public static final ValueError TITLE_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "title",
            "The title is too short.");
    public static final ValueError TITLE_TOO_LONG = new ValueError(ILLEGAL_VALUE, "title",
            "The title is too long.");


    public static final ValueError MISSING_NAME = new ValueError(MISSING_VALUE, "name", "The name is missing.");

    public static final ValueError NAME_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "name",
            "The name is too short.");
    public static final ValueError NAME_TOO_LONG = new ValueError(ILLEGAL_VALUE, "name",
            "The name is too long.");


    public static final ValueError THREAD_BODY_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "initialComment",
            "The initial comment is too short.");

    public static final ValueError THREAD_BODY_TOO_LONG = new ValueError(ILLEGAL_VALUE, "initialComment",
            "The initial comment is too long.");


    public static final ValueError MISSING_USER = new ValueError(MISSING_VALUE, "user", "The user is missing.");


    public static final ValueError MISSING_THREAD = new ValueError(MISSING_VALUE, "thread", "The thread is missing.");


    public static final ValueError MISSING_COMMENTER = new ValueError(MISSING_VALUE, "commenter",
            "The commenter is missing.");


    public static final ValueError MISSING_COMMENT = new ValueError(MISSING_VALUE, "comment",
            "The comment is missing.");

    public static final ValueError COMMENT_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "comment",
            "The comment is too short.");
    public static final ValueError COMMENT_TOO_LONG = new ValueError(ILLEGAL_VALUE, "comment",
            "The comment is too long.");


    public static final ValueError MISSING_PARENT = new ValueError(MISSING_VALUE, "parentComment",
            "The parent comment is missing.");

    public static ValueError CYCLE_IN_PARENTS = new ValueError(ILLEGAL_VALUE, "parent",
            "A cycle was detected when updating the parent");


    public static final ValueError MISSING_GAME = new ValueError(MISSING_VALUE, "game", "The game is missing.");


    public static final ValueError MISSING_REVIEW_BODY = new ValueError(MISSING_VALUE, "review",
            "The review body is missing.");

    public static final ValueError REVIEW_BODY_TOO_SHORT = new ValueError(ILLEGAL_VALUE, "review",
            "The review body is too short.");
    public static final ValueError REVIEW_BODY_TOO_LONG = new ValueError(ILLEGAL_VALUE, "review",
            "The review body is too long.");


    public static final ValueError MISSING_STORY_SCORE = new ValueError(MISSING_VALUE, "storyScore",
            "The story score is missing");

    public static final ValueError STORY_SCORE_BELOW_MIN = new ValueError(ILLEGAL_VALUE, "storyScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError STORY_SCORE_ABOVE_MAX = new ValueError(ILLEGAL_VALUE, "storyScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError MISSING_GRAPHICS_SCORE = new ValueError(MISSING_VALUE, "graphicsScore",
            "The graphics score is missing");

    public static final ValueError GRAPHICS_SCORE_BELOW_MIN = new ValueError(ILLEGAL_VALUE, "graphicsScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError GRAPHICS_SCORE_ABOVE_MAX = new ValueError(ILLEGAL_VALUE, "graphicsScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError MISSING_AUDIO_SCORE = new ValueError(MISSING_VALUE, "audioScore",
            "The audio score is missing");

    public static final ValueError AUDIO_SCORE_BELOW_MIN = new ValueError(ILLEGAL_VALUE, "audioScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError AUDIO_SCORE_ABOVE_MAX = new ValueError(ILLEGAL_VALUE, "audioScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError MISSING_CONTROLS_SCORE = new ValueError(MISSING_VALUE, "controlsScore",
            "The controls score is missing");

    public static final ValueError CONTROLS_SCORE_BELOW_MIN = new ValueError(ILLEGAL_VALUE, "controlsScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError CONTROLS_SCORE_ABOVE_MAX = new ValueError(ILLEGAL_VALUE, "controlsScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError MISSING_FUN_SCORE = new ValueError(MISSING_VALUE, "funScore",
            "The fun score is missing");

    public static final ValueError FUN_SCORE_BELOW_MIN = new ValueError(ILLEGAL_VALUE, "funScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);

    public static final ValueError FUN_SCORE_ABOVE_MAX = new ValueError(ILLEGAL_VALUE, "funScore",
            ILLEGAL_SCORE_ERROR_MESSAGE);
}
