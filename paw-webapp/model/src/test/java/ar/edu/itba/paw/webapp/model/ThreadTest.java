package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.NumericConstants;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * Performs testing over the {@link Thread} class.
 */
public class ThreadTest {

    /**
     * A {@link Faker} instance to create random thread names and bodies.
     */
    private final static Faker FAKER = new Faker();

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating/updating a thread with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testValidThreadIsCreated() {
        createThread();
    }

    @Test
    public void testThreadTitleIsUpdated() {
        final Thread thread = createThread();
        final String newTitle = generateCorrectLengthTitle();

        final String body = thread.getBody();

        thread.update(newTitle, thread.getBody());

        Assert.assertEquals("The title of the thread did not changed as expected",
                newTitle, thread.getTitle());
        Assert.assertEquals("The body changed, but was not expected", body, thread.getBody());
    }

    @Test
    public void testThreadBodyIsUpdated() {
        final Thread thread = createThread();
        final String newBody = generateCorrectLengthBody();

        final String title = thread.getTitle();

        thread.update(thread.getTitle(), newBody);

        Assert.assertEquals("The body of the thread did not changed as expected",
                newBody, thread.getBody());
        Assert.assertEquals("The title changed, but was not expected", title, thread.getTitle());
    }

    @Test
    public void testNullIsAPossibleBodyOnCreate() {
        final Thread thread = new Thread(Mockito.mock(User.class), generateCorrectLengthTitle(), null);

        Assert.assertNull("The thread was not created as expected. It's body should be null",
                thread.getBody());
    }

    @Test
    public void testNullIsAPossibleBodyOnUpdate() {
        final Thread thread = createThread();
        thread.update(thread.getTitle(), null);

        Assert.assertNull("The thread was not update as expected. It's body should be null",
                thread.getBody());
    }

    @Test
    public void testEmptyStringIsAPossibleBodyOnCreate() {
        final Thread thread = new Thread(Mockito.mock(User.class), generateCorrectLengthTitle(), "");

        Assert.assertNotNull("The thread was not created as expected. It's body should not be null",
                thread.getBody());
        Assert.assertTrue("The thread was not created as expected. It's body should be empty",
                thread.getBody().isEmpty());
    }

    @Test
    public void testEmptyStringIsAPossibleBodyOnUpdate() {
        final Thread thread = createThread();
        thread.update(thread.getTitle(), "");

        Assert.assertNotNull("The thread was not created as expected. It's body should not be null",
                thread.getBody());
        Assert.assertTrue("The thread was not created as expected. It's body should be empty",
                thread.getBody().isEmpty());
    }


    // ==== Missing User ====

    @Test(expected = ValidationException.class)
    public void testMissingUserThrowsException() {
        new Thread(
                null,
                generateCorrectLengthTitle(),
                generateCorrectLengthBody()
        );
    }

    @Test
    public void testMissingUserValueErrors() {
        try {
            testMissingUserThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_CREATOR));
        }
    }


    // ==== Missing Title ====

    @Test(expected = ValidationException.class)
    public void testMissingTitleThrowsExceptionOnCreate() {
        new Thread(
                Mockito.mock(User.class),
                null,
                generateCorrectLengthBody()
        );
    }

    @Test
    public void testMissingTitleValueErrorsOnCreate() {
        try {
            testMissingTitleThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_TITLE));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingTitleThrowsExceptionOnUpdate() {
        final Thread thread = createThread();
        thread.update(null, thread.getBody());
    }

    @Test
    public void testMissingTitleValueErrorsOnUpdate() {
        try {
            testMissingTitleThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_TITLE));
        }
    }


    // ==== Missing Body ====

    // Not tested, as Threads allow null body


    // ==== Short Title ====

    @Test(expected = ValidationException.class)
    public void testShortTitleThrowsExceptionOnCreate() {
        new Thread(
                Mockito.mock(User.class),
                generateShortTitle(),
                generateCorrectLengthBody()
        );
    }

    @Test
    public void testShortTitleValueErrorsOnCreate() {
        try {
            testShortTitleThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.TITLE_TOO_SHORT));
        }
    }

    @Test(expected = ValidationException.class)
    public void testShortTitleThrowsExceptionOnUpdate() {
        final Thread thread = createThread();
        thread.update(generateShortTitle(), thread.getBody());
    }

    @Test
    public void testShortTitleValueErrorsOnUpdate() {
        try {
            testShortTitleThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.TITLE_TOO_SHORT));
        }
    }


    // ==== Long Title ====

    @Test(expected = ValidationException.class)
    public void testLongTitleThrowsExceptionOnCreate() {
        new Thread(
                Mockito.mock(User.class),
                generateLongTitle(),
                generateCorrectLengthBody()
        );
    }

    @Test
    public void testLongTitleValueErrorsOnCreate() {
        try {
            testLongTitleThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.TITLE_TOO_LONG));
        }
    }

    @Test(expected = ValidationException.class)
    public void testLongTitleThrowsExceptionOnUpdate() {
        final Thread thread = createThread();
        thread.update(generateLongTitle(), thread.getBody());
    }

    @Test
    public void testLongTitleValueErrorsOnUpdate() {
        try {
            testLongTitleThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.TITLE_TOO_LONG));
        }
    }


    // ==== Short Body ====

    // Not tested as Thread body min. length is 0


    // ==== Long Body ====

    @Test(expected = ValidationException.class)
    public void testLongBodyThrowsExceptionOnCreate() {
        new Thread(
                Mockito.mock(User.class),
                generateCorrectLengthTitle(),
                generateLongBody()
        );
    }

    @Test
    public void testLongBodyValueErrorsOnCreate() {
        try {
            testLongBodyThrowsExceptionOnCreate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.THREAD_BODY_TOO_LONG));
        }
    }

    @Test(expected = ValidationException.class)
    public void testLongBodyThrowsExceptionOnUpdate() {
        final Thread thread = createThread();
        thread.update(thread.getTitle(), generateLongBody());
    }

    @Test
    public void testLongBodyValueErrorsOnUpdate() {
        try {
            testLongBodyThrowsExceptionOnUpdate();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.THREAD_BODY_TOO_LONG));
        }
    }


    // ==== Helper methods ====

    /**
     * @return A {@link Thread} (with valid arguments).
     */
    private static Thread createThread() {
        final User mockedUser = Mockito.mock(User.class);
        final String randomTitle = generateCorrectLengthTitle();
        final String randomBody = generateCorrectLengthBody();

        return new Thread(mockedUser, randomTitle, randomBody);
    }

    /**
     * @return A random body whose length is between the valid limits.
     */
    private static String generateCorrectLengthTitle() {
        final int randomBodyLength = FAKER.number()
                .numberBetween(NumericConstants.TITLE_MIN_LENGTH, NumericConstants.TITLE_MAX_LENGTH + 1);
        return FAKER.lorem().fixedString(randomBodyLength);
    }

    /**
     * @return A random body whose length is below the valid limit.
     */
    private static String generateShortTitle() {
        return FAKER.lorem().fixedString(NumericConstants.TITLE_MIN_LENGTH - 1);
    }

    /**
     * @return A random body whose length is above the valid limit.
     */
    private static String generateLongTitle() {
        return FAKER.lorem().fixedString(NumericConstants.TITLE_MAX_LENGTH + 1);
    }

    /**
     * @return A random body whose length is between the valid limits.
     */
    private static String generateCorrectLengthBody() {
        final int randomBodyLength = FAKER.number()
                .numberBetween(NumericConstants.THREAD_BODY_MIN_LENGTH, NumericConstants.TEXT_FIELD_MAX_LENGTH + 1);
        return FAKER.lorem().fixedString(randomBodyLength);
    }

    /**
     * @return A random body whose length is above the valid limit.
     */
    private static String generateLongBody() {
        return FAKER.lorem().fixedString(NumericConstants.TEXT_FIELD_MAX_LENGTH + 1);
    }
}
