package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.NumericConstants;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

/**
 * Performs testing over the {@link Review} class.
 */
public class ReviewTest {

    /**
     * A {@link Faker} instance to create random review bodies.
     */
    private final static Faker FAKER = new Faker();

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating/updating a review with illegal values" +
                    " did not included the corresponding errors";
    private static final String BODY_WRONG_CHANGE_MESSAGE = "The body of the review did not changed as expected";
    private static final String STORY_WRONG_CHANGE_MESSAGE = "The story score did not changed as expected";
    private static final String GRAPHICS_WRONG_CHANGE_MESSAGE = "The graphics score did not changed as expected";
    private static final String AUDIO_WRONG_CHANGE_MESSAGE = "The audio score did not changed as expected";
    private static final String CONTROLS_WRONG_CHANGE_MESSAGE = "The controls score did not changed as expected";
    private static final String FUN_WRONG_CHANGE_MESSAGE = "The fun score did not changed as expected";

    private static final String BODY_NOT_EXPECTED_CHANGE_MESSAGE =
            "The body of the review changed, but was not expected";
    private static final String STORY_NOT_EXPECTED_CHANGE_MESSAGE =
            "The story score changed, but was not expected";
    private static final String GRAPHICS_NOT_EXPECTED_CHANGE_MESSAGE =
            "The graphics score changed, but was not expected";
    private static final String AUDIO_NOT_EXPECTED_CHANGE_MESSAGE =
            "The audio score changed, but was not expected";
    private static final String CONTROLS_NOT_EXPECTED_CHANGE_MESSAGE =
            "The controls score changed, but was not expected";
    private static final String FUN_NOT_EXPECTED_CHANGE_MESSAGE =
            "The fun score changed, but was not expected";


    /**
     * Magic number to be used as upper limit of the random generator of scores.
     */
    private static final int MAGIC_NUMBER = 0xFF;


    // ==== Valid arguments ====

    @Test
    public void testValidReviewIsCreated() {
        createReview();
    }

    @Test
    public void testUpdateReviewBody() {
        final Review review = createReview();
        final String newBody = generateRandomBodyWithCorrectLength();

        final int storyScore = review.getStoryScore();
        final int graphicsScore = review.getGraphicsScore();
        final int audioScore = review.getAudioScore();
        final int controlsScore = review.getControlsScore();
        final int funScore = review.getFunScore();

        review.update(
                newBody,
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );

        // Assert the review has the new body
        Assert.assertEquals(BODY_WRONG_CHANGE_MESSAGE, newBody, review.getReview());

        // Assert the review did not changed other values
        Assert.assertEquals(STORY_NOT_EXPECTED_CHANGE_MESSAGE, storyScore, review.getStoryScore());
        Assert.assertEquals(GRAPHICS_NOT_EXPECTED_CHANGE_MESSAGE, graphicsScore, review.getGraphicsScore());
        Assert.assertEquals(AUDIO_NOT_EXPECTED_CHANGE_MESSAGE, audioScore, review.getAudioScore());
        Assert.assertEquals(CONTROLS_NOT_EXPECTED_CHANGE_MESSAGE, controlsScore, review.getControlsScore());
        Assert.assertEquals(FUN_NOT_EXPECTED_CHANGE_MESSAGE, funScore, review.getFunScore());
    }

    @Test
    public void testUpdateReviewStoryScore() {
        final Review review = createReview();
        final int newStoryScore = generateRandomScoreInCorrectRange();

        final String body = review.getReview();
        final int graphicsScore = review.getGraphicsScore();
        final int audioScore = review.getAudioScore();
        final int controlsScore = review.getControlsScore();
        final int funScore = review.getFunScore();

        review.update(
                review.getReview(),
                newStoryScore,
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );

        // Assert the review has the new story score
        Assert.assertEquals(STORY_WRONG_CHANGE_MESSAGE, newStoryScore, review.getStoryScore());

        // Assert the review did not changed other values
        Assert.assertEquals(BODY_NOT_EXPECTED_CHANGE_MESSAGE, body, review.getReview());
        Assert.assertEquals(GRAPHICS_NOT_EXPECTED_CHANGE_MESSAGE, graphicsScore, review.getGraphicsScore());
        Assert.assertEquals(AUDIO_NOT_EXPECTED_CHANGE_MESSAGE, audioScore, review.getAudioScore());
        Assert.assertEquals(CONTROLS_NOT_EXPECTED_CHANGE_MESSAGE, controlsScore, review.getControlsScore());
        Assert.assertEquals(FUN_NOT_EXPECTED_CHANGE_MESSAGE, funScore, review.getFunScore());
    }

    @Test
    public void testUpdateReviewGraphicsScore() {
        final Review review = createReview();
        final int newGraphicsScore = generateRandomScoreInCorrectRange();

        final String body = review.getReview();
        final int storyScore = review.getStoryScore();
        final int audioScore = review.getAudioScore();
        final int controlsScore = review.getControlsScore();
        final int funScore = review.getFunScore();

        review.update(
                review.getReview(),
                review.getStoryScore(),
                newGraphicsScore,
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );

        // Assert the review has the new story score
        Assert.assertEquals(GRAPHICS_WRONG_CHANGE_MESSAGE, newGraphicsScore, review.getGraphicsScore());

        // Assert the review did not changed other values
        Assert.assertEquals(BODY_NOT_EXPECTED_CHANGE_MESSAGE, body, review.getReview());
        Assert.assertEquals(STORY_NOT_EXPECTED_CHANGE_MESSAGE, storyScore, review.getStoryScore());
        Assert.assertEquals(AUDIO_NOT_EXPECTED_CHANGE_MESSAGE, audioScore, review.getAudioScore());
        Assert.assertEquals(CONTROLS_NOT_EXPECTED_CHANGE_MESSAGE, controlsScore, review.getControlsScore());
        Assert.assertEquals(FUN_NOT_EXPECTED_CHANGE_MESSAGE, funScore, review.getFunScore());
    }

    @Test
    public void testUpdateReviewAudioScore() {
        final Review review = createReview();
        final int newAudioScore = generateRandomScoreInCorrectRange();

        final String body = review.getReview();
        final int storyScore = review.getStoryScore();
        final int graphicsScore = review.getGraphicsScore();
        final int controlsScore = review.getControlsScore();
        final int funScore = review.getFunScore();

        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                newAudioScore,
                review.getControlsScore(),
                review.getFunScore()
        );

        // Assert the review has the new story score
        Assert.assertEquals(AUDIO_WRONG_CHANGE_MESSAGE, newAudioScore, review.getAudioScore());

        // Assert the review did not changed other values
        Assert.assertEquals(BODY_NOT_EXPECTED_CHANGE_MESSAGE, body, review.getReview());
        Assert.assertEquals(STORY_NOT_EXPECTED_CHANGE_MESSAGE, storyScore, review.getStoryScore());
        Assert.assertEquals(GRAPHICS_NOT_EXPECTED_CHANGE_MESSAGE, graphicsScore, review.getGraphicsScore());
        Assert.assertEquals(CONTROLS_NOT_EXPECTED_CHANGE_MESSAGE, controlsScore, review.getControlsScore());
        Assert.assertEquals(FUN_NOT_EXPECTED_CHANGE_MESSAGE, funScore, review.getFunScore());
    }

    @Test
    public void testUpdateReviewControlsScore() {
        final Review review = createReview();
        final int newControlsScore = generateRandomScoreInCorrectRange();

        final String body = review.getReview();
        final int storyScore = review.getStoryScore();
        final int graphicsScore = review.getGraphicsScore();
        final int audioScore = review.getAudioScore();
        final int funScore = review.getFunScore();

        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                newControlsScore,
                review.getFunScore()
        );

        // Assert the review has the new story score
        Assert.assertEquals(CONTROLS_WRONG_CHANGE_MESSAGE, newControlsScore, review.getControlsScore());

        // Assert the review did not changed other values
        Assert.assertEquals(BODY_NOT_EXPECTED_CHANGE_MESSAGE, body, review.getReview());
        Assert.assertEquals(STORY_NOT_EXPECTED_CHANGE_MESSAGE, storyScore, review.getStoryScore());
        Assert.assertEquals(GRAPHICS_NOT_EXPECTED_CHANGE_MESSAGE, graphicsScore, review.getGraphicsScore());
        Assert.assertEquals(AUDIO_NOT_EXPECTED_CHANGE_MESSAGE, audioScore, review.getAudioScore());
        Assert.assertEquals(FUN_NOT_EXPECTED_CHANGE_MESSAGE, funScore, review.getFunScore());
    }

    @Test
    public void testUpdateReviewFunScore() {
        final Review review = createReview();
        final int newFunScore = generateRandomScoreInCorrectRange();

        final String body = review.getReview();
        final int storyScore = review.getStoryScore();
        final int graphicsScore = review.getGraphicsScore();
        final int audioScore = review.getAudioScore();
        final int controlsScore = review.getControlsScore();

        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                newFunScore
        );

        // Assert the review has the new story score
        Assert.assertEquals(FUN_WRONG_CHANGE_MESSAGE, newFunScore, review.getFunScore());

        // Assert the review did not changed other values
        Assert.assertEquals(BODY_NOT_EXPECTED_CHANGE_MESSAGE, body, review.getReview());
        Assert.assertEquals(STORY_NOT_EXPECTED_CHANGE_MESSAGE, storyScore, review.getStoryScore());
        Assert.assertEquals(GRAPHICS_NOT_EXPECTED_CHANGE_MESSAGE, graphicsScore, review.getGraphicsScore());
        Assert.assertEquals(AUDIO_NOT_EXPECTED_CHANGE_MESSAGE, audioScore, review.getAudioScore());
        Assert.assertEquals(CONTROLS_NOT_EXPECTED_CHANGE_MESSAGE, controlsScore, review.getControlsScore());
    }


    // ==== Missing User ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        new Review(
                null,
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_USER));
        }
    }


    // ==== Missing Game ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsException() {
        new Review(
                Mockito.mock(User.class),
                null,
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingGameValueErrors() {
        try {
            testMissingGameThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }


    // ==== Missing Body ====

    @Test(expected = ValidationException.class)
    public void testMissingBodyThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                null,
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingBodyValueErrorsOnCreate() {
        try {
            testMissingBodyThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_REVIEW_BODY));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingBodyThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                null,
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testMissingBodyValueErrorsOnUpdate() {
        try {
            testMissingBodyThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_REVIEW_BODY));
        }
    }


    // ==== Missing Story Score ====

    @Test(expected = ValidationException.class)
    public void testMissingStoryScoreThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                null,
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingStoryScoreValueErrorsOnCreate() {
        try {
            testMissingStoryScoreThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_STORY_SCORE));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingStoryScoreThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                null,
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testMissingStoryScoreValueErrorsOnUpdate() {
        try {
            testMissingStoryScoreThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_STORY_SCORE));
        }
    }


    // ==== Missing Graphics Score ====

    @Test(expected = ValidationException.class)
    public void testMissingGraphicsScoreThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                null,
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingGraphicsScoreValueErrorsOnCreate() {
        try {
            testMissingGraphicsScoreThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_GRAPHICS_SCORE));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingGraphicsScoreThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                null,
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testMissingGraphicsScoreValueErrorsOnUpdate() {
        try {
            testMissingGraphicsScoreThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_GRAPHICS_SCORE));
        }
    }


    // ==== Missing Audio Score ====

    @Test(expected = ValidationException.class)
    public void testMissingAudioScoreThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                null,
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingAudioScoreValueErrorsOnCreate() {
        try {
            testMissingAudioScoreThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_AUDIO_SCORE));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingAudioScoreThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                null,
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testMissingAudioScoreValueErrorsOnUpdate() {
        try {
            testMissingAudioScoreThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_AUDIO_SCORE));
        }
    }


    // ==== Missing Controls Score ====

    @Test(expected = ValidationException.class)
    public void testMissingControlsScoreThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                null,
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingControlsScoreValueErrorsOnCreate() {
        try {
            testMissingControlsScoreThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_CONTROLS_SCORE));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingControlsScoreThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                null,
                review.getFunScore()
        );
    }

    @Test
    public void testMissingControlsScoreValueErrorsOnUpdate() {
        try {
            testMissingControlsScoreThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_CONTROLS_SCORE));
        }
    }


    // ==== Missing Fun Score ====

    @Test(expected = ValidationException.class)
    public void testMissingFunScoreThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                null
        );
    }

    @Test
    public void testMissingFunScoreValueErrorsOnCreate() {
        try {
            testMissingFunScoreThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_FUN_SCORE));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingFunScoreThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                null
        );
    }

    @Test
    public void testMissingFunScoreValueErrorsOnUpdate() {
        try {
            testMissingFunScoreThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_FUN_SCORE));
        }
    }


    // ==== Short body ====

    @Test(expected = ValidationException.class)
    public void testShortBodyThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateShortBody(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testShortBodyValueErrorsOnCreate() {
        try {
            testShortBodyThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.REVIEW_BODY_TOO_SHORT));
        }
    }

    @Test(expected = ValidationException.class)
    public void testShortBodyThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                generateShortBody(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testShortBodyValueErrorsOnUpdate() {
        try {
            testShortBodyThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.REVIEW_BODY_TOO_SHORT));
        }
    }


    // ==== Long body ====

    @Test(expected = ValidationException.class)
    public void testLongBodyThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateLongBody(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testLongBodyValueErrorsOnCreate() {
        try {
            testLongBodyThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.REVIEW_BODY_TOO_LONG));
        }
    }

    @Test(expected = ValidationException.class)
    public void testLongBodyThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                generateLongBody(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testLongBodyValueErrorsOnUpdate() {
        try {
            testLongBodyThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.REVIEW_BODY_TOO_LONG));
        }
    }


    // ==== Story score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testStoryScoreBelowLowerLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreBelowLowerLimit(),   // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testStoryScoreBelowLowerLimitValueErrorsOnCreate() {
        try {
            testStoryScoreBelowLowerLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.STORY_SCORE_BELOW_MIN));
        }
    }

    @Test(expected = ValidationException.class)
    public void testStoryScoreBelowLowerLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                generateRandomScoreBelowLowerLimit(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testStoryScoreBelowLowerLimitValueErrorsOnUpdate() {
        try {
            testStoryScoreBelowLowerLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.STORY_SCORE_BELOW_MIN));
        }
    }


    // ==== Story score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testStoryScoreAboveUpperLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreAboveUpperLimit(),   // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testStoryScoreAboveUpperLimitValueErrorsOnCreate() {
        try {
            testStoryScoreAboveUpperLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.STORY_SCORE_ABOVE_MAX));
        }
    }

    @Test(expected = ValidationException.class)
    public void testStoryScoreAboveUpperLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                generateRandomScoreAboveUpperLimit(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testStoryScoreAboveUpperLimitValueErrorsOnUpdate() {
        try {
            testStoryScoreAboveUpperLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.STORY_SCORE_ABOVE_MAX));
        }
    }


    // ==== Graphics score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testGraphicsScoreBelowLowerLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreBelowLowerLimit(),   // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testGraphicsScoreBelowLowerLimitValueErrorsOnCreate() {
        try {
            testGraphicsScoreBelowLowerLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GRAPHICS_SCORE_BELOW_MIN));
        }
    }

    @Test(expected = ValidationException.class)
    public void testGraphicsScoreBelowLowerLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                generateRandomScoreBelowLowerLimit(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testGraphicsScoreBelowLowerLimitValueErrorsOnUpdate() {
        try {
            testGraphicsScoreBelowLowerLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GRAPHICS_SCORE_BELOW_MIN));
        }
    }


    // ==== Graphics score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testGraphicsScoreAboveUpperLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreAboveUpperLimit(),   // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testGraphicsScoreAboveUpperLimitValueErrorsOnCreate() {
        try {
            testGraphicsScoreAboveUpperLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GRAPHICS_SCORE_ABOVE_MAX));
        }
    }

    @Test(expected = ValidationException.class)
    public void testGraphicsScoreAboveUpperLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                generateRandomScoreAboveUpperLimit(),
                review.getAudioScore(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testGraphicsScoreAboveUpperLimitValueErrorsOnUpdate() {
        try {
            testGraphicsScoreAboveUpperLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GRAPHICS_SCORE_ABOVE_MAX));
        }
    }


    // ==== Audio score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testAudioScoreBelowLowerLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreBelowLowerLimit(),   // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testAudioScoreBelowLowerLimitValueErrorsOnCreate() {
        try {
            testAudioScoreBelowLowerLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.AUDIO_SCORE_BELOW_MIN));
        }
    }

    @Test(expected = ValidationException.class)
    public void testAudioScoreBelowLowerLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                generateRandomScoreBelowLowerLimit(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testAudioScoreBelowLowerLimitValueErrorsOnUpdate() {
        try {
            testAudioScoreBelowLowerLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.AUDIO_SCORE_BELOW_MIN));
        }
    }


    // ==== Audio score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testAudioScoreAboveUpperLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreAboveUpperLimit(),   // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testAudioScoreAboveUpperLimitValueErrorsOnCreate() {
        try {
            testAudioScoreAboveUpperLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.AUDIO_SCORE_ABOVE_MAX));
        }
    }

    @Test(expected = ValidationException.class)
    public void testAudioScoreAboveUpperLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                generateRandomScoreAboveUpperLimit(),
                review.getControlsScore(),
                review.getFunScore()
        );
    }

    @Test
    public void testAudioScoreAboveUpperLimitValueErrorsOnUpdate() {
        try {
            testAudioScoreAboveUpperLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.AUDIO_SCORE_ABOVE_MAX));
        }
    }


    // ==== Controls score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testControlsScoreBelowLowerLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreBelowLowerLimit(),   // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testControlsScoreBelowLowerLimitValueErrorsOnCreate() {
        try {
            testControlsScoreBelowLowerLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.CONTROLS_SCORE_BELOW_MIN));
        }
    }

    @Test(expected = ValidationException.class)
    public void testControlsScoreBelowLowerLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                generateRandomScoreBelowLowerLimit(),
                review.getFunScore()
        );
    }

    @Test
    public void testControlsScoreBelowLowerLimitValueErrorsOnUpdate() {
        try {
            testControlsScoreBelowLowerLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.CONTROLS_SCORE_BELOW_MIN));
        }
    }


    // ==== Controls score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testControlsScoreAboveUpperLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreAboveUpperLimit(),   // Controls score
                generateRandomScoreInCorrectRange()     // Fun Score
        );
    }

    @Test
    public void testControlsScoreAboveUpperLimitValueErrorsOnCreate() {
        try {
            testControlsScoreAboveUpperLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.CONTROLS_SCORE_ABOVE_MAX));
        }
    }

    @Test(expected = ValidationException.class)
    public void testControlsScoreAboveUpperLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                generateRandomScoreAboveUpperLimit(),
                review.getFunScore()
        );
    }

    @Test
    public void testControlsScoreAboveUpperLimitValueErrorsOnUpdate() {
        try {
            testControlsScoreAboveUpperLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.CONTROLS_SCORE_ABOVE_MAX));
        }
    }


    // ==== Fun score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testFunScoreBelowLowerLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreBelowLowerLimit()    // Fun Score
        );
    }

    @Test
    public void testFunScoreBelowLowerLimitValueErrorsOnCreate() {
        try {
            testFunScoreBelowLowerLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.FUN_SCORE_BELOW_MIN));
        }
    }

    @Test(expected = ValidationException.class)
    public void testFunScoreBelowLowerLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                generateRandomScoreBelowLowerLimit()
        );
    }

    @Test
    public void testFunScoreBelowLowerLimitValueErrorsOnUpdate() {
        try {
            testFunScoreBelowLowerLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.FUN_SCORE_BELOW_MIN));
        }
    }


    // ==== Fun score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testFunScoreAboveUpperLimitThrowsExceptionOnCreate() {
        new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),  // Body
                generateRandomScoreInCorrectRange(),    // Story score
                generateRandomScoreInCorrectRange(),    // Graphics score
                generateRandomScoreInCorrectRange(),    // Audio score
                generateRandomScoreInCorrectRange(),    // Controls score
                generateRandomScoreAboveUpperLimit()    // Fun Score
        );
    }

    @Test
    public void testFunScoreAboveUpperLimitValueErrorsOnCreate() {
        try {
            testFunScoreAboveUpperLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.FUN_SCORE_ABOVE_MAX));
        }
    }

    @Test(expected = ValidationException.class)
    public void testFunScoreAboveUpperLimitThrowsExceptionOnUpdate() {
        final Review review = createReview();
        review.update(
                review.getReview(),
                review.getStoryScore(),
                review.getGraphicsScore(),
                review.getAudioScore(),
                review.getControlsScore(),
                generateRandomScoreAboveUpperLimit()
        );
    }

    @Test
    public void testFunScoreAboveUpperLimitValueErrorsOnUpdate() {
        try {
            testFunScoreAboveUpperLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.FUN_SCORE_ABOVE_MAX));
        }
    }


    // ======== Helper methods ========

    /**
     * @return A random body whose length is between the valid limits.
     */
    private static String generateRandomBodyWithCorrectLength() {
        final int randomBodyLength = FAKER.number()
                .numberBetween(NumericConstants.REVIEW_BODY_MIN_LENGTH, NumericConstants.TEXT_FIELD_MAX_LENGTH + 1);
        return FAKER.lorem().fixedString(randomBodyLength);
    }

    /**
     * @return A random body whose length is below the valid limit.
     */
    private static String generateShortBody() {
        return FAKER.lorem().fixedString(NumericConstants.REVIEW_BODY_MIN_LENGTH - 1);
    }

    /**
     * @return A random body whose length is above the valid limit.
     */
    private static String generateLongBody() {
        return FAKER.lorem().fixedString(NumericConstants.TEXT_FIELD_MAX_LENGTH + 1);
    }

    /**
     * @return A random integer to be used as score. Will be in the valid range of score.
     */
    private static int generateRandomScoreInCorrectRange() {
        return FAKER.number().numberBetween(NumericConstants.MIN_SCORE, NumericConstants.MAX_SCORE + 1);
    }

    /**
     * @return A random integer below the lower limit to be used as score.
     */
    private static int generateRandomScoreBelowLowerLimit() {
        final int lowerLimit = NumericConstants.MIN_SCORE - new Random().nextInt(MAGIC_NUMBER) - 1;
        return FAKER.number().numberBetween(lowerLimit, NumericConstants.MIN_SCORE);
    }

    /**
     * @return A random integer above the upper limit to be used as score.
     */
    private static int generateRandomScoreAboveUpperLimit() {
        final int upperLimit = NumericConstants.MAX_SCORE + new Random().nextInt(MAGIC_NUMBER) + 2;
        return FAKER.number().numberBetween(NumericConstants.MAX_SCORE + 1, upperLimit);
    }

    /**
     * @return A {@link Review} (with valid arguments).
     */
    private static Review createReview() {
        return new Review(
                Mockito.mock(User.class),
                Mockito.mock(Game.class),
                generateRandomBodyWithCorrectLength(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange(),
                generateRandomScoreInCorrectRange()
        );
    }
}
