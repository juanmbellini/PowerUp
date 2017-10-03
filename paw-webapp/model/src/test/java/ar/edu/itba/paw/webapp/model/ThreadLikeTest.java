package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link CommentLike} class.
 */
public class ThreadLikeTest {

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a thread like with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testThreadLikeWithValidValuesIsCreated() {
        final User mockedUser = Mockito.mock(User.class);
        final Thread mockedThread = Mockito.mock(Thread.class);

        final ThreadLike threadLike = new ThreadLike(mockedUser, mockedThread);

        Assert.assertEquals("The created thread like did not contain the user liking as user",
                mockedUser, threadLike.getUser());
        Assert.assertEquals("The created thread like did not contain the thread being liked as thread",
                mockedThread, threadLike.getThread());
    }


    // ==== Missing user ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        final Thread mockedThread = Mockito.mock(Thread.class);

        new ThreadLike(null, mockedThread);
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_USER));
        }
    }


    // ==== Missing thread ====

    @Test(expected = ValidationException.class)
    public void testMissingThreadThrowsException() {
        final User mockedUser = Mockito.mock(User.class);

        new ThreadLike(mockedUser, null);
    }

    @Test
    public void testMissingCommentValueErrors() {
        try {
            testMissingThreadThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_THREAD));
        }
    }

}
