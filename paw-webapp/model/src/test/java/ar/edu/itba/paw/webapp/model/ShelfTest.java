package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.NumericConstants;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link Shelf} class.
 */
public class ShelfTest {

    /**
     * A {@link Faker} instance to create random shelf names.
     */
    private final static Faker FAKER = new Faker();

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating/updating a shelf with illegal values" +
                    " did not included the corresponding errors";

    // ==== Valid arguments ====

    @Test
    public void testValidReviewIsCreated() {
        createShelf();
    }

    @Test
    public void testUpdateReviewName() {
        final Shelf shelf = createShelf();
        final String newName = generateRandomNameWithCorrectLength();

        shelf.update(newName);

        Assert.assertEquals("The name of the shelf did not changed as expected", newName, shelf.getName());
    }


    // ==== Valid behaviour====

    @Test
    public void testGameIsAdded() {
        final Shelf shelf = createShelf();
        final Game mockedGame = Mockito.mock(Game.class);

        shelf.addGame(mockedGame);

        Assert.assertTrue("Adding a game did not worked as expected", shelf.getGames().contains(mockedGame));
    }

    @Test
    public void testGameIsRemoved() {
        final Shelf shelf = createShelf();
        final Game mockedGame = Mockito.mock(Game.class);
        shelf.addGame(mockedGame);

        final ShelfGame shelfGame = shelf.removeGame(mockedGame);

        Assert.assertFalse("Removing a game did not worked as expected",
                shelf.getGames().contains(mockedGame));
        Assert.assertEquals("The shelf game returned when removing a game from a shelf " +
                        "did not contained the correct game",
                mockedGame, shelfGame.getGame());
        Assert.assertEquals("The shelf game returned when removing a game from a shelf " +
                        "did not contained the correct shelf",
                shelf, shelfGame.getShelf());
    }

    @Test
    public void testRemovingGamesFromTheReturnedSetOfGamesDoesNotAffectTheShelf() {
        final Shelf shelf = createShelf();
        final Game mockedGame = Mockito.mock(Game.class);

        shelf.addGame(mockedGame);
        shelf.getGames().remove(mockedGame);

        Assert.assertTrue("Removing a game from the set returned by the getGames method affected the shelf",
                shelf.getGames().contains(mockedGame));
    }

    @Test
    public void testRemovingOneGameDoesNotAffectOtherGames() {
        final Shelf shelf = createShelf();
        final Game mockedGame1 = Mockito.mock(Game.class);
        final Game mockedGame2 = Mockito.mock(Game.class);
        shelf.addGame(mockedGame1);
        shelf.addGame(mockedGame2);

        final ShelfGame shelfGame = shelf.removeGame(mockedGame1);

        // Deleted game
        Assert.assertFalse("Removing a game did not worked as expected",
                shelf.getGames().contains(mockedGame1));
        Assert.assertEquals("The shelf game returned when removing a game from a shelf " +
                        "did not contained the correct game",
                mockedGame1, shelfGame.getGame());
        Assert.assertEquals("The shelf game returned when removing a game from a shelf " +
                        "did not contained the correct shelf",
                shelf, shelfGame.getShelf());

        // Not deleted game
        Assert.assertTrue("Removing a game did not worked as expected",
                shelf.getGames().contains(mockedGame2));
        Assert.assertNotEquals("The shelf game returned when removing a game from a shelf " +
                        "did not contained the correct game",
                mockedGame2, shelfGame.getGame());
    }

    @Test
    public void testRemovingANotContainedGameReturnsNull() {
        final Shelf shelf = createShelf();
        final Game mockedGame1 = Mockito.mock(Game.class);
        final Game mockedGame2 = Mockito.mock(Game.class);
        shelf.addGame(mockedGame1);

        final ShelfGame shelfGame = shelf.removeGame(mockedGame2);

        Assert.assertNull("Removing a game did not worked as expected", shelfGame);

        // Not deleted game
        Assert.assertTrue("Removing a game did not worked as expected",
                shelf.getGames().contains(mockedGame1));
    }


    // ==== Missing User ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        new Shelf(generateRandomNameWithCorrectLength(), null);
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_USER));
        }
    }


    // ==== Missing Name ====

    @Test(expected = ValidationException.class)
    public void testMissingNameThrowsExceptionOnCreate() {
        new Shelf(null, Mockito.mock(User.class));
    }

    @Test
    public void testMissingNameValueErrorsOnCreate() {
        try {
            testMissingNameThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_NAME));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingNameThrowsExceptionOnUpdate() {
        createShelf().update(null);
    }

    @Test
    public void testMissingNameValueErrorsOnUpdate() {
        try {
            testMissingNameThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_NAME));
        }
    }


    // ==== Short Name ====

    @Test(expected = ValidationException.class)
    public void testShortNameThrowsExceptionOnCreate() {
        new Shelf(generateShortName(), Mockito.mock(User.class));
    }

    @Test
    public void testShortNameValueErrorsOnCreate() {
        try {
            testShortNameThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.NAME_TOO_SHORT));
        }
    }

    @Test(expected = ValidationException.class)
    public void testShortNameThrowsExceptionOnUpdate() {
        createShelf().update(generateShortName());
    }

    @Test
    public void testShortNameValueErrorsOnUpdate() {
        try {
            testShortNameThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.NAME_TOO_SHORT));
        }
    }


    // ==== Long Name ====

    @Test(expected = ValidationException.class)
    public void testLongNameThrowsExceptionOnCreate() {
        new Shelf(generateLongName(), Mockito.mock(User.class));
    }

    @Test
    public void testLongNameValueErrorsOnCreate() {
        try {
            testLongNameThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.NAME_TOO_LONG));
        }
    }

    @Test(expected = ValidationException.class)
    public void testLongNameThrowsExceptionOnUpdate() {
        createShelf().update(generateLongName());
    }

    @Test
    public void testLongNameValueErrorsOnUpdate() {
        try {
            testLongNameThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.NAME_TOO_LONG));
        }
    }


    // ==== Missing game when adding ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsExceptionWhenAdding() {
        createShelf().addGame(null);
    }

    @Test
    public void testMissingGameValueErrorsWhenAdding() {
        try {
            testMissingGameThrowsExceptionWhenAdding();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }

    // ==== Missing game when removing ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsExceptionWhenRemoving() {
        final Shelf shelf = createShelf();
        final Game mockedGame = Mockito.mock(Game.class);
        shelf.addGame(mockedGame);

        shelf.removeGame(null);
    }

    @Test
    public void testMissingGameValueErrorsWhenRemoving() {
        try {
            testMissingGameThrowsExceptionWhenRemoving();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }


    // ==== Helper methods ====

    /**
     * @return A random name whose length is between the valid limits.
     */
    private static String generateRandomNameWithCorrectLength() {
        final int randomBodyLength = FAKER.number()
                .numberBetween(NumericConstants.NAME_MIN_LENGTH, NumericConstants.NAME_MAX_LENGTH + 1);
        return FAKER.lorem().fixedString(randomBodyLength);
    }

    /**
     * @return A random name whose length is below the valid limit.
     */
    private static String generateShortName() {
        return FAKER.lorem().fixedString(NumericConstants.NAME_MIN_LENGTH - 1);
    }

    /**
     * @return A random name whose length is below the valid limit.
     */
    private static String generateLongName() {
        return FAKER.lorem().fixedString(NumericConstants.NAME_MAX_LENGTH + 1);
    }

    /**
     * @return A {@link Shelf} (with valid arguments).
     */
    private static Shelf createShelf() {
        final String name = generateRandomNameWithCorrectLength();
        final User mockedUser = Mockito.mock(User.class);

        return new Shelf(name, mockedUser);
    }
}
