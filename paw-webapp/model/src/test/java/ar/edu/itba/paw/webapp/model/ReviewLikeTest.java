package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link ReviewLike} class.
 */
public class ReviewLikeTest {

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a review like with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testReviewLikeWithValidValuesIsCreated() {
        final User mockedUser = Mockito.mock(User.class);
        final Review mockedReview = Mockito.mock(Review.class);

        final ReviewLike reviewLike = new ReviewLike(mockedUser, mockedReview);

        Assert.assertEquals("The created review like did not contain the user liking as user",
                mockedUser, reviewLike.getUser());
        Assert.assertEquals("The created review like did not contain the review being liked as review",
                mockedReview, reviewLike.getreview());
    }


    // ==== Missing user ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        final Review mockedReview = Mockito.mock(Review.class);

        new ReviewLike(null, mockedReview);
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_USER));
        }
    }


    // ==== Missing review ====

    @Test(expected = ValidationException.class)
    public void testMissingCommentThrowsException() {
        final User mockedUser = Mockito.mock(User.class);

        new ReviewLike(mockedUser, null);
    }

    @Test
    public void testMissingCommentValueErrors() {
        try {
            testMissingCommentThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_REVIEW));
        }
    }

}
