package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Performs testing over the {@link UserFollow} class.
 */
public class UserFollowTest {

    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating a user follow with illegal values" +
                    " did not included the corresponding errors";


    // ==== Valid arguments ====

    @Test
    public void testUserFollowWithValidValuesIsCreated() {
        final User mockedFollowed = Mockito.mock(User.class);
        final User mockedFollower = Mockito.mock(User.class);

        // Set different ids to the mocked users
        final long followedId = 1;
        final long followerId = 2;
        Mockito.when(mockedFollowed.getId()).thenReturn(followedId);
        Mockito.when(mockedFollower.getId()).thenReturn(followerId);

        final UserFollow userFollow = new UserFollow(mockedFollowed, mockedFollower);

        Assert.assertEquals("The created user follow did not contain the followed user liking as followed",
                mockedFollowed, userFollow.getFollowed());
        Assert.assertEquals("The created user follow did not contain the user following as following",
                mockedFollower, userFollow.getFollower());
    }


    // ==== Missing followed ====

    @Test(expected = ValidationException.class)
    public void testMissingFollowedThrowsException() {
        final User mockedFollower = Mockito.mock(User.class);

        new UserFollow(null, mockedFollower);
    }

    @Test
    public void testMissingFollowedValueErrors() {
        try {
            testMissingFollowedThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_FOLLOWED));
        }
    }


    // ==== Missing follower ====

    @Test(expected = ValidationException.class)
    public void testMissingFollowerThrowsException() {
        final User mockedFollowed = Mockito.mock(User.class);

        new UserFollow(mockedFollowed, null);
    }

    @Test
    public void testMissingFollowerValueErrors() {
        try {
            testMissingFollowerThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_FOLLOWER));
        }
    }


    // ==== Auto follow ====

    @Test(expected = ValidationException.class)
    public void testAutoFollowThrowsException() {
        final User mockedFollowed = Mockito.mock(User.class);
        final User mockedFollower = Mockito.mock(User.class);

        // Set same id to the mocked users
        final long followedId = 1;
        final long followerId = 1;
        Mockito.when(mockedFollowed.getId()).thenReturn(followedId);
        Mockito.when(mockedFollower.getId()).thenReturn(followerId);

        new UserFollow(mockedFollowed, mockedFollower);
    }

    @Test
    public void testAutoFollowValueErrors() {
        try {
            testAutoFollowThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.AUTO_FOLLOW));
        }
    }

}
