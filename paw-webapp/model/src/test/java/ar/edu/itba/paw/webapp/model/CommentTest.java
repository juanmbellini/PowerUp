package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.NumericConstants;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link Comment} class.
 */
public class CommentTest {

    /**
     * A {@link Faker} instance to create random comment bodies.
     */
    private final static Faker FAKER = new Faker();

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating/updating a comment/reply with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testCommentWithValidValuesIsCreated() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();

        new Comment(mockedThread, randomBody, mockedUser);
    }

    @Test
    public void testReplyWithValidValuesIsCreated() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final Comment mockedComment = Mockito.mock(Comment.class);
        Mockito.when(mockedComment.getThread()).thenReturn(mockedThread);

        final User mockedUserReplying = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();

        final Comment reply = new Comment(mockedComment, randomBody, mockedUserReplying);

        Assert.assertEquals("The created reply does not have the comment being replied as a parent comment",
                mockedComment, reply.getParentComment());
        Assert.assertEquals("The created reply does not have the same thread as the comment being replied",
                mockedComment.getThread(), reply.getThread());
    }

    @Test
    public void testUpdatedCommentBodyWithValidValue() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();
        final Comment comment = new Comment(mockedThread, randomBody, mockedUser);

        final String newBody = generateRandomBodyWithCorrectLength();
        comment.update(newBody);
    }


    // ==== Missing thread ====

    @Test(expected = ValidationException.class)
    public void testMissingThreadThrowsException() {
        final Thread nullThread = null;
        final User mockedUser = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();

        new Comment(nullThread, randomBody, mockedUser);
    }

    @Test
    public void testMissingThreadValueErrorsOnCommentCreation() {
        try {
            testMissingThreadThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_THREAD));
        }
    }

    // ==== Missing parent ====

    @Test(expected = ValidationException.class)
    public void testMissingParentThrowsException() {
        final Comment nullParent = null;
        final User mockedUser = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();

        new Comment(nullParent, randomBody, mockedUser);
    }

    @Test
    public void testMissingParentValueErrorsOnCommentCreation() {
        try {
            testMissingParentThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_PARENT));
        }
    }


    // ==== Missing commenter ====

    @Test(expected = ValidationException.class)
    public void testMissingCommenterThrowsException() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final String randomBody = generateRandomBodyWithCorrectLength();

        new Comment(mockedThread, randomBody, null);
    }

    @Test
    public void testMissingCommenterValueErrorsOnCommentCreation() {
        try {
            testMissingCommenterThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_COMMENTER));
        }
    }


    // ==== Missing replier ====

    @Test(expected = ValidationException.class)
    public void testMissingReplierThrowsException() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final Comment mockedComment = Mockito.mock(Comment.class);
        Mockito.when(mockedComment.getThread()).thenReturn(mockedThread);

        final String randomBody = generateRandomBodyWithCorrectLength();

        final Comment reply = new Comment(mockedComment, randomBody, null);
    }

    @Test
    public void testMissingReplierValueErrorsOnCommentCreation() {
        try {
            testMissingReplierThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_COMMENTER));
        }
    }


    // ==== Missing body ====

    @Test(expected = ValidationException.class)
    public void testMissingBodyThrowsException() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);

        final Comment reply = new Comment(mockedThread, null, null);
    }

    @Test
    public void testMissingBodyValueErrorsOnCommentCreation() {
        try {
            testMissingBodyThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_COMMENT));
        }
    }


    // ==== Short body ====

    @Test(expected = ValidationException.class)
    public void testExceptionIsThrownWithShortBodyOnCommentCreation() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);
        final String randomShortBody = generateShortBody();

        new Comment(mockedThread, randomShortBody, mockedUser);
    }

    @Test
    public void testShortBodyValueErrorsOnCommentCreation() {
        try {
            testExceptionIsThrownWithShortBodyOnCommentCreation();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.COMMENT_TOO_SHORT));
        }
    }

    @Test(expected = ValidationException.class)
    public void testExceptionIsThrownWithShortBodyOnReplyCreation() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final Comment mockedComment = Mockito.mock(Comment.class);
        Mockito.when(mockedComment.getThread()).thenReturn(mockedThread);

        final User mockedUserReplying = Mockito.mock(User.class);

        final String randomShortBody = generateShortBody();

        new Comment(mockedComment, randomShortBody, mockedUserReplying);
    }

    @Test
    public void testShortBodyValueErrorsOnReplyCreation() {
        try {
            testExceptionIsThrownWithShortBodyOnReplyCreation();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.COMMENT_TOO_SHORT));
        }
    }

    @Test(expected = ValidationException.class)
    public void testExceptionIsThrownWithShortBodyOnCommentUpdate() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();
        final Comment comment = new Comment(mockedThread, randomBody, mockedUser);

        final String newBody = generateShortBody();
        comment.update(newBody);
    }

    @Test
    public void testShortBodyValueErrorsOnCommentUpdate() {
        try {
            testExceptionIsThrownWithShortBodyOnCommentUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.COMMENT_TOO_SHORT));
        }
    }


    // ==== Long body ====

    @Test(expected = ValidationException.class)
    public void testExceptionIsThrownWithLongBodyOnCommentCreation() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);
        final String randomLongBody = generateLongBody();

        new Comment(mockedThread, randomLongBody, mockedUser);
    }

    @Test
    public void testLongBodyValueErrorsOnCommentCreation() {
        try {
            testExceptionIsThrownWithLongBodyOnCommentCreation();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.COMMENT_TOO_LONG));
        }
    }

    @Test(expected = ValidationException.class)
    public void testExceptionIsThrownWithLongBodyOnReplyCreation() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final Comment mockedComment = Mockito.mock(Comment.class);
        Mockito.when(mockedComment.getThread()).thenReturn(mockedThread);

        final User mockedUserReplying = Mockito.mock(User.class);

        final String randomLongBody = generateLongBody();

        new Comment(mockedComment, randomLongBody, mockedUserReplying);
    }

    @Test
    public void testLongBodyValueErrorsOnReplyCreation() {
        try {
            testExceptionIsThrownWithLongBodyOnReplyCreation();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.COMMENT_TOO_LONG));
        }
    }

    @Test(expected = ValidationException.class)
    public void testExceptionIsThrownWithLongBodyOnCommentUpdate() {
        final Thread mockedThread = Mockito.mock(Thread.class);
        final User mockedUser = Mockito.mock(User.class);
        final String randomBody = generateRandomBodyWithCorrectLength();
        final Comment comment = new Comment(mockedThread, randomBody, mockedUser);

        final String newBody = generateLongBody();
        comment.update(newBody);
    }

    @Test
    public void testLongBodyValueErrorsOnCommentUpdate() {
        try {
            testExceptionIsThrownWithLongBodyOnCommentUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.COMMENT_TOO_LONG));
        }
    }


    // ======== Helper methods ========

    /**
     * @return A random body whose length is between the valid limits.
     */
    private static String generateRandomBodyWithCorrectLength() {
        final int randomBodyLength = FAKER.number()
                .numberBetween(NumericConstants.COMMENT_BODY_MIN_LENGTH, NumericConstants.TEXT_FIELD_MAX_LENGTH + 1);
        return FAKER.lorem().fixedString(randomBodyLength);
    }

    /**
     * @return A random body whose length is below the valid limit.
     */
    private static String generateShortBody() {
        return FAKER.lorem().fixedString(NumericConstants.COMMENT_BODY_MIN_LENGTH - 1);
    }

    /**
     * @return A random body whose length is above the valid limit.
     */
    private static String generateLongBody() {
        return FAKER.lorem().fixedString(NumericConstants.TEXT_FIELD_MAX_LENGTH + 1);
    }
}
