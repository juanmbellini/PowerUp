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
 * Performs testing over the {@link UserGameScore} class.
 */
public class UserGameScoreTest {

    /**
     * A {@link Faker} instance to create random scores.
     */
    private final static Faker FAKER = new Faker();

    /**
     * Magic number to be used as upper limit of the random generator of scores.
     */
    private static final int MAGIC_NUMBER = 0xFF;

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a user game score with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testUserGameScoreWithValidValuesOnCreate() {
        createUserGameScore();
    }

    @Test
    public void testUserGameScoreWithValidValuesOnUpdate() {
        final UserGameScore gameScore = createUserGameScore();
        final int newScore = generateRandomScoreInCorrectRange();

        final Game game = gameScore.getGame();
        final User user = gameScore.getUser();

        gameScore.setScore(newScore);

        // Assert the game score has changed correctly
        Assert.assertEquals("The score did not changed as expected", newScore, gameScore.getScore());

        // Assert other fields did not changed
        Assert.assertEquals("The game changed, but was not expected", game, gameScore.getGame());
        Assert.assertEquals("The user changed, but was not expected", user, gameScore.getUser());

    }


    // ==== Missing game ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsException() {
        new UserGameScore(
                null,
                Mockito.mock(User.class),
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


    // ==== Missing user ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        new UserGameScore(
                Mockito.mock(Game.class),
                null,
                generateRandomScoreInCorrectRange()
        );
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_USER));
        }
    }


    // ==== Score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testScoreBelowLowerLimitThrowsExceptionOnCreate() {
        new UserGameScore(
                Mockito.mock(Game.class),
                Mockito.mock(User.class),
                generateRandomScoreBelowLowerLimit()
        );
    }

    @Test
    public void testFunScoreBelowLowerLimitValueErrorsOnCreate() {
        try {
            testScoreBelowLowerLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GAME_SCORE_BELOW_MIN));
        }
    }

    @Test(expected = ValidationException.class)
    public void testScoreBelowLowerLimitThrowsExceptionOnUpdate() {
        final UserGameScore gameScore = createUserGameScore();
        gameScore.setScore(generateRandomScoreBelowLowerLimit());
    }

    @Test
    public void testScoreBelowLowerLimitValueErrorsOnUpdate() {
        try {
            testScoreBelowLowerLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GAME_SCORE_BELOW_MIN));
        }
    }


    // ==== Score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testScoreAboveUpperLimitThrowsExceptionOnCreate() {
        new UserGameScore(
                Mockito.mock(Game.class),
                Mockito.mock(User.class),
                generateRandomScoreAboveUpperLimit()
        );
    }

    @Test
    public void testScoreAboveUpperLimitValueErrorsOnCreate() {
        try {
            testScoreAboveUpperLimitThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GAME_SCORE_ABOVE_MAX));
        }
    }

    @Test(expected = ValidationException.class)
    public void testScoreAboveUpperLimitThrowsExceptionOnUpdate() {
        final UserGameScore gameScore = createUserGameScore();
        gameScore.setScore(generateRandomScoreAboveUpperLimit());
    }

    @Test
    public void testScoreAboveUpperLimitValueErrorsOnUpdate() {
        try {
            testScoreAboveUpperLimitThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GAME_SCORE_ABOVE_MAX));
        }
    }


    // ==== Helper methods ====

    /**
     * @return A {@link UserGameScore} (with valid arguments).
     */
    private static UserGameScore createUserGameScore() {
        final Game mockedGame = Mockito.mock(Game.class);
        final User mockedUser = Mockito.mock(User.class);
        final int score = generateRandomScoreInCorrectRange();

        return new UserGameScore(mockedGame, mockedUser, score);
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

}
