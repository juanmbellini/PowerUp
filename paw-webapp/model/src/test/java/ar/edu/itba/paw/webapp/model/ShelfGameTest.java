package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link ShelfGame} class.
 */
public class ShelfGameTest {

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a shelf game with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testShelfGameWithValidValuesIsCreated() {
        final Game mockedGame = Mockito.mock(Game.class);
        final Shelf mockedShelf = Mockito.mock(Shelf.class);

        final ShelfGame shelfGame = new ShelfGame(mockedGame, mockedShelf);

        Assert.assertEquals("The created game shelf did not contain the game contained in the shelf",
                mockedGame, shelfGame.getGame());
        Assert.assertEquals("The created game shelf did not contain the shelf to which it belongs",
                mockedShelf, shelfGame.getShelf());
    }


    // ==== Missing Game ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsException() {
        final Shelf mockedShelf = Mockito.mock(Shelf.class);
        new ShelfGame(null, mockedShelf);
    }

    @Test
    public void testMissingGameValueErrors() {
        try {
            testMissingGameThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }


    // ==== Missing Shelf ====

    @Test(expected = ValidationException.class)
    public void testMissingShelfThrowsException() {
        final Game mockedGame = Mockito.mock(Game.class);
        new ShelfGame(mockedGame, null);
    }

    @Test
    public void testMissingShelfValueErrors() {
        try {
            testMissingShelfThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_SHELF));
        }
    }

}
