package ar.edu.itba.paw.webapp.model.validation;


import static ar.edu.itba.paw.webapp.model.validation.ValueError.ErrorCause.ILLEGAL_VALUE;
import static ar.edu.itba.paw.webapp.model.validation.ValueError.ErrorCause.MISSING_VALUE;

/**
 * Class containing {@link ValueError} constants to be reused.
 * <p>
 * Created by Juan Marcos Bellini on 12/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ValueErrorConstants {


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


    private static final String ILLEGAL_SCORE_ERROR_MESSAGE = "The score must be between 0 and 10";

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
