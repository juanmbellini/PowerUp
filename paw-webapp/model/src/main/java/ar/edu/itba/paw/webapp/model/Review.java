package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Models a review made by a specific user for a specific game.
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @SequenceGenerator(name = "reviews_seq", sequenceName = "reviews_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String review;

    @Column(nullable = false, name = "story_score")
    private int storyScore;

    @Column(nullable = false, name = "graphics_score")
    private int graphicsScore;

    @Column(nullable = false, name = "audio_score")
    private int audioScore;

    @Column(nullable = false, name = "controls_score")
    private int controlsScore;

    @Column(nullable = false, name = "fun_score")
    private int funScore;

    /*package*/  Review() {
        //for hibernate
    }


    /**
     * Creates a review.
     *
     * @param user          The user creating the review.
     * @param game          The reviewed game.
     * @param reviewBody    THe body of the review.
     * @param storyScore    The new story score.
     * @param graphicsScore The graphics score.
     * @param audioScore    The audio score.
     * @param controlsScore The control score.
     * @param funScore      The fun score.
     * @throws ValidationException If any value is wrong.
     */
    public Review(User user, Game game, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                  Integer controlsScore, Integer funScore)
            throws ValidationException {
        List<ValidationException.ValueError> errorList = new LinkedList<>();
        if (user == null) {
            errorList.add(MISSING_USER);
        }
        if (game == null) {
            errorList.add(MISSING_GAME);
        }
        update(reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore, errorList);
        this.user = user;
        this.game = game;
        this.date = LocalDate.now();
    }


    /**
     * Updates the review.
     *
     * @param review        The new body of the review.
     * @param storyScore    The new story score.
     * @param graphicsScore The new graphics score.
     * @param audioScore    The new audio score.
     * @param controlsScore The new control score.
     * @param funScore      The new fun score.
     * @throws ValidationException If any value is wrong.
     */
    public void update(String review, Integer storyScore, Integer graphicsScore, Integer audioScore,
                       Integer controlsScore, Integer funScore)
            throws ValidationException {
        update(review, storyScore, graphicsScore, audioScore, controlsScore, funScore, new LinkedList<>());
    }

    /**
     * Updates the review, receiving a list of detected errors before executing this method.
     *
     * @param review        The new body of the review.
     * @param storyScore    The new story score.
     * @param graphicsScore The new graphics score.
     * @param audioScore    The new audio score.
     * @param controlsScore The new control score.
     * @param funScore      The new fun score.
     * @param errorList     The list containing possible errors detected before executing the method.
     * @throws ValidationException If any value is wrong.
     */
    private void update(String review, Integer storyScore, Integer graphicsScore, Integer audioScore,
                        Integer controlsScore, Integer funScore, List<ValidationException.ValueError> errorList)
            throws ValidationException {
        checkValues(review, storyScore, graphicsScore, audioScore, controlsScore, funScore, errorList);
        this.review = review;
        this.storyScore = storyScore;
        this.graphicsScore = graphicsScore;
        this.audioScore = audioScore;
        this.controlsScore = controlsScore;
        this.funScore = funScore;
    }


    public double getRating() {
        return (double) (this.audioScore + this.controlsScore + this.funScore + this.graphicsScore + this.storyScore) / 5;
    }

    public long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public int getStoryScore() {
        return storyScore;
    }

    public int getGraphicsScore() {
        return graphicsScore;
    }

    public int getAudioScore() {
        return audioScore;
    }

    public int getControlsScore() {
        return controlsScore;
    }

    public int getFunScore() {
        return funScore;
    }

    public String getReview() {
        return review;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    @Transient
    public double getOverallScore() {
        return (storyScore + graphicsScore + audioScore + controlsScore + funScore) / 5.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        return id == review.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


    /**
     * Checks the given value, throwing a {@link ValidationException} if any is wrong.
     *
     * @param review        The review to be checked.
     * @param storyScore    The story score to be checked.
     * @param graphicsScore The graphics score to be checked.
     * @param audioScore    The audio score to be checked.
     * @param controlsScore The control score to be checked.
     * @param funScore      The fun score to be checked.
     * @param errorList     A list containing possible detected errors before calling this method.
     * @throws ValidationException If any value is wrong.
     */
    private void checkValues(String review, Integer storyScore, Integer graphicsScore, Integer audioScore,
                             Integer controlsScore, Integer funScore, List<ValidationException.ValueError> errorList)
            throws ValidationException {
        errorList = errorList == null ? new LinkedList<>() : errorList;
        if (review == null) {
            errorList.add(MISSING_REVIEW_BODY);
        } else if (review.isEmpty()) {
            errorList.add(REVIEW_BODY_TOO_SHORT);
        }
        checkScore(storyScore, MISSING_STORY_SCORE, STORY_SCORE_BELOW_0, STORY_SCORE_BELOW_0, errorList);
        checkScore(graphicsScore, MISSING_GRAPHICS_SCORE, GRAPHICS_SCORE_BELOW_0, GRAPHICS_SCORE_BELOW_0, errorList);
        checkScore(audioScore, MISSING_AUDIO_SCORE, AUDIO_SCORE_BELOW_0, AUDIO_SCORE_BELOW_0, errorList);
        checkScore(controlsScore, MISSING_CONTROLS_SCORE, CONTROLS_SCORE_BELOW_0, CONTROLS_SCORE_BELOW_0, errorList);
        checkScore(funScore, MISSING_FUN_SCORE, FUN_SCORE_BELOW_0, FUN_SCORE_BELOW_0, errorList);

        if (!errorList.isEmpty()) {
            throw new ValidationException(errorList);
        }
    }


    /**
     * Checks if the given {@code score} is valid.
     *
     * @param score        The score to be checked.
     * @param missingError The {@link ValidationException.ValueError} representing the missing value error.
     * @param belowError   The {@link ValidationException.ValueError} representing a below 0 error.
     * @param aboveError   The {@link ValidationException.ValueError} representing an above 10 error.
     * @param errorList    The list containing errors.
     */
    private void checkScore(Integer score, ValidationException.ValueError missingError,
                            ValidationException.ValueError belowError, ValidationException.ValueError aboveError,
                            List<ValidationException.ValueError> errorList) {
        if (score == null) {
            errorList.add(missingError);
        } else if (score < 0) {
            errorList.add(belowError);
        } else if (score > 10) {
            errorList.add(aboveError);
        }
    }


    private static final ValidationException.ValueError MISSING_USER =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "user", "The user is missing.");

    private static final ValidationException.ValueError MISSING_GAME =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "game", "The game is missing.");

    private static final ValidationException.ValueError MISSING_REVIEW_BODY =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "review", "The review body is missing.");
    private static final ValidationException.ValueError REVIEW_BODY_TOO_SHORT =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "review", "The review body is too short.");


    private static final String ILLEGAL_SCORE_ERROR_MESSAGE = "The score must be between 0 and 10";

    // Story
    private static final ValidationException.ValueError MISSING_STORY_SCORE =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "storyScore", "The story score is missing");
    private static final ValidationException.ValueError STORY_SCORE_BELOW_0 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "storyScore", ILLEGAL_SCORE_ERROR_MESSAGE);
    private static final ValidationException.ValueError STORY_SCORE_ABOVE_10 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "storyScore", ILLEGAL_SCORE_ERROR_MESSAGE);

    // Graphics
    private static final ValidationException.ValueError MISSING_GRAPHICS_SCORE =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "graphicsScore", "The graphics score is missing");
    private static final ValidationException.ValueError GRAPHICS_SCORE_BELOW_0 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "graphicsScore", ILLEGAL_SCORE_ERROR_MESSAGE);
    private static final ValidationException.ValueError GRAPHICS_SCORE_ABOVE_10 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "graphicsScore", ILLEGAL_SCORE_ERROR_MESSAGE);

    // Audio
    private static final ValidationException.ValueError MISSING_AUDIO_SCORE =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "audioScore", "The audio score is missing");
    private static final ValidationException.ValueError AUDIO_SCORE_BELOW_0 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "audioScore", ILLEGAL_SCORE_ERROR_MESSAGE);
    private static final ValidationException.ValueError AUDIO_SCORE_ABOVE_10 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "audioScore", ILLEGAL_SCORE_ERROR_MESSAGE);

    // Controls
    private static final ValidationException.ValueError MISSING_CONTROLS_SCORE =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "controlsScore", "The controls score is missing");
    private static final ValidationException.ValueError CONTROLS_SCORE_BELOW_0 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "controlsScore", ILLEGAL_SCORE_ERROR_MESSAGE);
    private static final ValidationException.ValueError CONTROLS_SCORE_ABOVE_10 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "controlsScore", ILLEGAL_SCORE_ERROR_MESSAGE);

    // Fun
    private static final ValidationException.ValueError MISSING_FUN_SCORE =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.MISSING_VALUE,
                    "funScore", "The fun score is missing");
    private static final ValidationException.ValueError FUN_SCORE_BELOW_0 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "funScore", ILLEGAL_SCORE_ERROR_MESSAGE);
    private static final ValidationException.ValueError FUN_SCORE_ABOVE_10 =
            new ValidationException.ValueError(ValidationException.ValueError.ErrorCause.ILLEGAL_VALUE,
                    "funScore", ILLEGAL_SCORE_ERROR_MESSAGE);


}