package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.NumericConstants;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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


    // ==== Valid arguments ====

    @Test
    public void testValidReviewIsCreated() {
        createReview();
    }

    @Test
    public void testUpdateReviewBody() {
        final Review review = createReview();
        final String newBody = generateRandomBodyWithCorrectLength();

        review.changeBody(newBody);

        Assert.assertEquals(BODY_WRONG_CHANGE_MESSAGE, newBody, review.getReview());
    }


    // ==== Missing User ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        new Review(null, Mockito.mock(Game.class), generateRandomBodyWithCorrectLength());
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
        new Review(Mockito.mock(User.class), null, generateRandomBodyWithCorrectLength());
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
        new Review(Mockito.mock(User.class), Mockito.mock(Game.class), null);
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
        review.changeBody(null);
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


    // ==== Short body ====

    @Test(expected = ValidationException.class)
    public void testShortBodyThrowsExceptionOnCreate() {
        new Review(Mockito.mock(User.class), Mockito.mock(Game.class), generateShortBody());
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
        review.changeBody(generateShortBody());
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
        new Review(Mockito.mock(User.class), Mockito.mock(Game.class), generateLongBody());
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
        review.changeBody(generateLongBody());
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
     * @return A {@link Review} (with valid arguments).
     */
    private static Review createReview() {
        return new Review(Mockito.mock(User.class), Mockito.mock(Game.class), generateRandomBodyWithCorrectLength());
    }
}
