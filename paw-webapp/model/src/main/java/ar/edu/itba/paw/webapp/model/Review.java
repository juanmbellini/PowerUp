package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;
import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

/**
 * Models a review made by a specific user for a specific game.
 */
@Entity
@Table(name = "reviews")
public class Review implements ValidationExceptionThrower, Likeable, ScoreChecker {

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
    private Calendar date;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<ReviewLike> likes;

    /*package*/  Review() {
        this.likes = new HashSet<>();
        // For Hibernate
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
    public Review(User user, Game game, String reviewBody, Integer storyScore, Integer graphicsScore,
                  Integer audioScore, Integer controlsScore, Integer funScore)
            throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(user, errorList, ValueErrorConstants.MISSING_USER);
        ValidationHelper.objectNotNull(game, errorList, ValueErrorConstants.MISSING_GAME);

        update(reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore, errorList);
        this.user = user;
        this.game = game;
        this.date = Calendar.getInstance();
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
                        Integer controlsScore, Integer funScore, List<ValueError> errorList)
            throws ValidationException {
        checkValues(review, storyScore, graphicsScore, audioScore, controlsScore, funScore, errorList);
        this.review = review;
        this.storyScore = storyScore;
        this.graphicsScore = graphicsScore;
        this.audioScore = audioScore;
        this.controlsScore = controlsScore;
        this.funScore = funScore;
    }


    /**
     * Id getter.
     *
     * @return The id.
     */
    public long getId() {
        return id;
    }

    @Override
    public long getLikeCount() {
        return likes.size();
    }

    /**
     * Game getter.
     *
     * @return The game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Story score getter.
     *
     * @return The story score.
     */
    public int getStoryScore() {
        return storyScore;
    }

    /**
     * Graphic score getter
     *
     * @return The graphic score.
     */
    public int getGraphicsScore() {
        return graphicsScore;
    }

    /**
     * Audio score getter.
     *
     * @return The audio score.
     */
    public int getAudioScore() {
        return audioScore;
    }

    /**
     * Control score getter.
     *
     * @return The control score.
     */
    public int getControlsScore() {
        return controlsScore;
    }

    /**
     * Fun score getter.
     *
     * @return The fun score.
     */
    public int getFunScore() {
        return funScore;
    }

    /**
     * Review getter.
     *
     * @return The review (i.e The body of the review).
     */
    public String getReview() {
        return review;
    }

    /**
     * User getter.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Date getter.
     *
     * @return The date (i.e. Date at which the review was created).
     */
    public Calendar getDate() {
        return date;
    }


    /**
     * Overall score getter.
     *
     * @return The overall score (i.e average of scores).
     */
    @Transient
    public double getOverallScore() {
        return (storyScore + graphicsScore + audioScore + controlsScore + funScore) / 5.0;
    }

    /**
     * Rating getter.
     *
     * @return The rating (i.e average of scores)
     */
    public double getRating() {
        return (double) (this.audioScore + this.controlsScore + this.funScore + this.graphicsScore + this.storyScore) / 5;
    }

    /**
     * Equals based on the id.
     *
     * @param o The object to be compared with.
     * @return {@code true} if they are the same, or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        return id == review.id;

    }

    /**
     * Hashcode based on the id.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


    /**
     * Checks the given values, throwing a {@link ValidationException} if any is wrong.
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
                             Integer controlsScore, Integer funScore, List<ValueError> errorList)
            throws ValidationException {
        errorList = errorList == null ? new LinkedList<>() : errorList;

        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(review, NumericConstants.REVIEW_BODY_MIN_LENGTH,
                NumericConstants.TEXT_FIELD_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_REVIEW_BODY,
                ValueErrorConstants.REVIEW_BODY_TOO_SHORT, ValueErrorConstants.REVIEW_BODY_TOO_LONG);

        checkScore(storyScore, ValueErrorConstants.MISSING_STORY_SCORE,
                ValueErrorConstants.STORY_SCORE_BELOW_MIN, ValueErrorConstants.STORY_SCORE_ABOVE_MAX, errorList);
        checkScore(graphicsScore, ValueErrorConstants.MISSING_GRAPHICS_SCORE,
                ValueErrorConstants.GRAPHICS_SCORE_BELOW_MIN, ValueErrorConstants.GRAPHICS_SCORE_ABOVE_MAX, errorList);
        checkScore(audioScore, ValueErrorConstants.MISSING_AUDIO_SCORE,
                ValueErrorConstants.AUDIO_SCORE_BELOW_MIN, ValueErrorConstants.AUDIO_SCORE_ABOVE_MAX, errorList);
        checkScore(controlsScore, ValueErrorConstants.MISSING_CONTROLS_SCORE,
                ValueErrorConstants.CONTROLS_SCORE_BELOW_MIN, ValueErrorConstants.CONTROLS_SCORE_ABOVE_MAX, errorList);
        checkScore(funScore, ValueErrorConstants.MISSING_FUN_SCORE,
                ValueErrorConstants.FUN_SCORE_BELOW_MIN, ValueErrorConstants.FUN_SCORE_ABOVE_MAX, errorList);

        throwValidationException(errorList);
    }

}