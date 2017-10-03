package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Random;

/**
 * Performs testing over the {@link UserGameStatus} class.
 */
public class UserGameStatusTest {

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a user game score with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testUserGameStatusWithValidValuesOnCreate() {
        createUserGameStatus();
    }

    @Test
    public void testUserGameStatusWithValidValuesOnUpdate() {
        final UserGameStatus gameStatus = createUserGameStatus();
        final PlayStatus newPlayStatus = getRandomPlayStatus();

        final Game game = gameStatus.getGame();
        final User user = gameStatus.getUser();

        gameStatus.setPlayStatus(newPlayStatus);

        // Assert the play status has changed correctly
        Assert.assertEquals("The score did not changed as expected", newPlayStatus, gameStatus.getPlayStatus());

        // Assert other fields did not changed
        Assert.assertEquals("The game changed, but was not expected", game, gameStatus.getGame());
        Assert.assertEquals("The user changed, but was not expected", user, gameStatus.getUser());

    }


    // ==== Missing game ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsException() {
        new UserGameStatus(
                null,
                Mockito.mock(User.class),
                getRandomPlayStatus()
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
        new UserGameStatus(
                Mockito.mock(Game.class),
                null,
                getRandomPlayStatus()
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

    // ==== Missing play status ====

    @Test(expected = ValidationException.class)
    public void testMissingPlayStatusThrowsExceptionOnCreate() {
        new UserGameStatus(
                Mockito.mock(Game.class),
                Mockito.mock(User.class),
                null
        );
    }

    @Test
    public void testMissingPlayStatusValueErrorsOnCreate() {
        try {
            testMissingPlayStatusThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_PLAY_STATUS));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingPlayStatusThrowsExceptionOnUpdate() {
        createUserGameStatus().setPlayStatus(null);
    }

    @Test
    public void testMissingPlayStatusValueErrorsOnUpdate() {
        try {
            testMissingPlayStatusThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_PLAY_STATUS));
        }
    }


    // ==== Helper methods ====

    /**
     * @return A {@link UserGameScore} (with valid arguments).
     */
    private static UserGameStatus createUserGameStatus() {
        final Game mockedGame = Mockito.mock(Game.class);
        final User mockedUser = Mockito.mock(User.class);
        final PlayStatus mockedPlayStatus = getRandomPlayStatus();

        return new UserGameStatus(mockedGame, mockedUser, mockedPlayStatus);
    }

    /**
     * @return A randomly chosen {@link PlayStatus}.
     */
    private static PlayStatus getRandomPlayStatus() {
        final PlayStatus[] statuses = PlayStatus.values();
        final int amountOfStatuses = statuses.length;
        return statuses[new Random().nextInt(amountOfStatuses)];
    }
}
