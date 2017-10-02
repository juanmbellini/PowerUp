package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link CommentLike} class.
 */
public class CommentLikeTest {

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a comment like with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testCommentLikeWithValidValuesIsCreated() {
        final User mockedUser = Mockito.mock(User.class);
        final Comment mockedComment = Mockito.mock(Comment.class);

        final CommentLike commentLike = new CommentLike(mockedUser, mockedComment);

        Assert.assertEquals("The created comment like did not contain the user liking as user",
                mockedUser, commentLike.getUser());
        Assert.assertEquals("The created comment like did not contain the comment being liked as comment",
                mockedComment, commentLike.getComment());
    }


    // ==== Missing user ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        final Comment mockedComment = Mockito.mock(Comment.class);

        new CommentLike(null, mockedComment);
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_USER));
        }
    }


    // ==== Missing comment ====

    @Test(expected = ValidationException.class)
    public void testMissingCommentThrowsException() {
        final User mockedUser = Mockito.mock(User.class);

        new CommentLike(mockedUser, null);
    }

    @Test
    public void testMissingCommentValueErrors() {
        try {
            testMissingCommentThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_COMMENT));
        }
    }

}
