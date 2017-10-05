package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.NumericConstants;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValueErrorConstants;
import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Performs testing over the {@link User} class.
 */
public class UserTest {

    /**
     * A {@link Faker} instance to create random usernames and emails.
     */
    private final static Faker FAKER = new Faker();

    /**
     * Magic number to be used for fake data generation.
     */
    private static final int MAGIC_NUMBER = 0xFF;


    private static final String MISSING_VALUE_ERRORS_MESSAGE =
            "The exception thrown when creating/updating a user with illegal values" +
                    " did not included the corresponding errors";

    private static final String SET_PLAY_STATUS_ERROR = "Setting a play status did not worked as expected";
    private static final String REMOVE_PLAY_STATUS_ERROR = "Removing a play status did not worked as expected";
    private static final String SCORING_ERROR = "Scoring a game did not worked as expected";
    private static final String UNSCORING_ERROR = "Unscoring a game did not worked as expected";
    private static final String ADD_AUTHORITY_ERROR = "Adding an authority did not worked as expected";
    private static final String REMOVE_AUTHORITY_ERROR = "Removing an authority did not worked as expected";


    // ==== Valid arguments ====

    @Test
    public void testUserIsCreated() {
        createUser();
    }

    @Test
    public void testUpdatePassword() {
        final User user = createUser();
        final String newPassword = FAKER.internet().password();

        user.changePassword(newPassword);

        // Assert the user has the new password
        Assert.assertEquals("The password did not changed as expected",
                newPassword, user.getHashedPassword());
    }

    @Test
    public void testSetPlayStatusForFirstTime() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final PlayStatus playStatus = UserGameStatusTest.getRandomPlayStatus();

        user.setPlayStatus(game, playStatus);

        final Map<Long, PlayStatus> playedGamed = user.getPlayedGames();
        Assert.assertNotNull(SET_PLAY_STATUS_ERROR, playedGamed);
        Assert.assertTrue(SET_PLAY_STATUS_ERROR, playedGamed.containsKey(game.getId()));
        Assert.assertEquals(SET_PLAY_STATUS_ERROR, playStatus, playedGamed.get(game.getId()));
    }

    @Test
    public void testSetTwoPlayStatuses() {
        final User user = createUser();

        final Game game1 = Mockito.mock(Game.class);
        final Game game2 = Mockito.mock(Game.class);
        Mockito.when(game1.getId()).thenReturn(1L);
        Mockito.when(game2.getId()).thenReturn(2L);

        final PlayStatus playStatus1 = UserGameStatusTest.getRandomPlayStatus();
        final PlayStatus playStatus2 = UserGameStatusTest.getRandomPlayStatus();

        user.setPlayStatus(game1, playStatus1);
        user.setPlayStatus(game2, playStatus2);

        final Map<Long, PlayStatus> playedGamed = user.getPlayedGames();
        Assert.assertNotNull(SET_PLAY_STATUS_ERROR, playedGamed);
        Assert.assertTrue(SET_PLAY_STATUS_ERROR, playedGamed.containsKey(game1.getId()));
        Assert.assertTrue(SET_PLAY_STATUS_ERROR, playedGamed.containsKey(game2.getId()));
        Assert.assertEquals(SET_PLAY_STATUS_ERROR, playStatus1, playedGamed.get(game1.getId()));
        Assert.assertEquals(SET_PLAY_STATUS_ERROR, playStatus2, playedGamed.get(game2.getId()));
    }

    @Test
    public void testChangePlayStatus() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final PlayStatus playStatus1 = UserGameStatusTest.getRandomPlayStatus();
        user.setPlayStatus(game, playStatus1);

        final PlayStatus playStatus2 = UserGameStatusTest.getRandomPlayStatus();
        user.setPlayStatus(game, playStatus2);

        final Map<Long, PlayStatus> playedGamed = user.getPlayedGames();
        Assert.assertNotNull(SET_PLAY_STATUS_ERROR, playedGamed);
        Assert.assertTrue(SET_PLAY_STATUS_ERROR, playedGamed.containsKey(game.getId()));
        Assert.assertEquals(SET_PLAY_STATUS_ERROR, playStatus2, playedGamed.get(game.getId()));
    }

    @Test
    public void testChangePlayStatusWithTwoGamesPlayed() {
        final User user = createUser();

        final Game game1 = Mockito.mock(Game.class);
        final Game game2 = Mockito.mock(Game.class);
        Mockito.when(game1.getId()).thenReturn(1L);

        final PlayStatus playStatus1 = UserGameStatusTest.getRandomPlayStatus();
        final PlayStatus playStatus2 = UserGameStatusTest.getRandomPlayStatus();
        user.setPlayStatus(game1, playStatus1);
        user.setPlayStatus(game2, playStatus2);

        final PlayStatus playStatus3 = UserGameStatusTest.getRandomPlayStatus();
        user.setPlayStatus(game1, playStatus3);

        final Map<Long, PlayStatus> playedGamed = user.getPlayedGames();
        Assert.assertNotNull(SET_PLAY_STATUS_ERROR, playedGamed);
        Assert.assertTrue(SET_PLAY_STATUS_ERROR, playedGamed.containsKey(game1.getId()));
        Assert.assertTrue(SET_PLAY_STATUS_ERROR, playedGamed.containsKey(game2.getId()));
        Assert.assertEquals(SET_PLAY_STATUS_ERROR, playStatus3, playedGamed.get(game1.getId()));
        Assert.assertEquals(SET_PLAY_STATUS_ERROR, playStatus2, playedGamed.get(game2.getId()));
    }

    @Test
    public void testRemovePlayStatusWithExistingGame() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final PlayStatus playStatus = UserGameStatusTest.getRandomPlayStatus();

        user.setPlayStatus(game, playStatus);
        final UserGameStatus gameStatus = user.removePlayStatus(game);
        Assert.assertNotNull(REMOVE_PLAY_STATUS_ERROR, gameStatus);
        Assert.assertEquals(REMOVE_PLAY_STATUS_ERROR, game, gameStatus.getGame());
        Assert.assertEquals(REMOVE_PLAY_STATUS_ERROR, playStatus, gameStatus.getPlayStatus());

        final Map<Long, PlayStatus> playedGamed = user.getPlayedGames();
        Assert.assertNotNull(REMOVE_PLAY_STATUS_ERROR, playedGamed);
        Assert.assertFalse(REMOVE_PLAY_STATUS_ERROR, playedGamed.containsKey(game.getId()));
        Assert.assertNull(REMOVE_PLAY_STATUS_ERROR, playedGamed.get(game.getId()));
    }

    @Test
    public void testRemovePlayStatusWithoutExistingGame() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final UserGameStatus gameStatus = user.removePlayStatus(game);
        Assert.assertNull(REMOVE_PLAY_STATUS_ERROR, gameStatus);

        final Map<Long, PlayStatus> playedGamed = user.getPlayedGames();
        Assert.assertNotNull(REMOVE_PLAY_STATUS_ERROR, playedGamed);
        Assert.assertFalse(REMOVE_PLAY_STATUS_ERROR, playedGamed.containsKey(game.getId()));
        Assert.assertNull(REMOVE_PLAY_STATUS_ERROR, playedGamed.get(game.getId()));
    }


    @Test
    public void testScoringAGameForFirstTime() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final int score = UserGameScoreTest.generateRandomScoreInCorrectRange();

        user.scoreGame(game, score);

        final Map<Long, Integer> scoredGamed = user.getScoredGames();
        Assert.assertNotNull(SCORING_ERROR, scoredGamed);
        Assert.assertTrue(SCORING_ERROR, scoredGamed.containsKey(game.getId()));
        Assert.assertEquals(SCORING_ERROR, (Integer) score, scoredGamed.get(game.getId()));
    }

    @Test
    public void testScoringTwoGames() {
        final User user = createUser();

        final Game game1 = Mockito.mock(Game.class);
        final Game game2 = Mockito.mock(Game.class);
        Mockito.when(game1.getId()).thenReturn(1L);
        Mockito.when(game2.getId()).thenReturn(2L);

        final int score1 = UserGameScoreTest.generateRandomScoreInCorrectRange();
        final int score2 = UserGameScoreTest.generateRandomScoreInCorrectRange();

        user.scoreGame(game1, score1);
        user.scoreGame(game2, score2);

        final Map<Long, Integer> scoredGamed = user.getScoredGames();
        Assert.assertNotNull(SCORING_ERROR, scoredGamed);
        Assert.assertTrue(SCORING_ERROR, scoredGamed.containsKey(game1.getId()));
        Assert.assertTrue(SCORING_ERROR, scoredGamed.containsKey(game2.getId()));
        Assert.assertEquals(SCORING_ERROR, (Integer) score1, scoredGamed.get(game1.getId()));
        Assert.assertEquals(SCORING_ERROR, (Integer) score2, scoredGamed.get(game2.getId()));
    }

    @Test
    public void testChangeScore() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final int score1 = UserGameScoreTest.generateRandomScoreInCorrectRange();
        user.scoreGame(game, score1);

        final int score2 = UserGameScoreTest.generateRandomScoreInCorrectRange();
        user.scoreGame(game, score2);

        final Map<Long, Integer> scoredGamed = user.getScoredGames();
        Assert.assertNotNull(SCORING_ERROR, scoredGamed);
        Assert.assertTrue(SCORING_ERROR, scoredGamed.containsKey(game.getId()));
        Assert.assertEquals(SCORING_ERROR, (Integer) score2, scoredGamed.get(game.getId()));
    }

    @Test
    public void testChangeScoreWithTwoGamesScored() {
        final User user = createUser();

        final Game game1 = Mockito.mock(Game.class);
        final Game game2 = Mockito.mock(Game.class);
        Mockito.when(game1.getId()).thenReturn(1L);

        final int score1 = UserGameScoreTest.generateRandomScoreInCorrectRange();
        final int score2 = UserGameScoreTest.generateRandomScoreInCorrectRange();
        user.scoreGame(game1, score1);
        user.scoreGame(game2, score2);

        final int score3 = UserGameScoreTest.generateRandomScoreInCorrectRange();
        user.scoreGame(game1, score3);

        final Map<Long, Integer> scoredGamed = user.getScoredGames();
        Assert.assertNotNull(SCORING_ERROR, scoredGamed);
        Assert.assertTrue(SCORING_ERROR, scoredGamed.containsKey(game1.getId()));
        Assert.assertTrue(SCORING_ERROR, scoredGamed.containsKey(game2.getId()));
        Assert.assertEquals(SCORING_ERROR, (Integer) score3, scoredGamed.get(game1.getId()));
        Assert.assertEquals(SCORING_ERROR, (Integer) score2, scoredGamed.get(game2.getId()));
    }

    @Test
    public void testUnscoreGameWithExistingGame() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final int score = UserGameScoreTest.generateRandomScoreInCorrectRange();

        user.scoreGame(game, score);
        final UserGameScore gameScore = user.unscoreGame(game);
        Assert.assertNotNull(UNSCORING_ERROR, gameScore);
        Assert.assertEquals(UNSCORING_ERROR, game, gameScore.getGame());
        Assert.assertEquals(UNSCORING_ERROR, score, gameScore.getScore());

        final Map<Long, Integer> scoredGamed = user.getScoredGames();
        Assert.assertNotNull(UNSCORING_ERROR, scoredGamed);
        Assert.assertFalse(UNSCORING_ERROR, scoredGamed.containsKey(game.getId()));
        Assert.assertNull(UNSCORING_ERROR, scoredGamed.get(game.getId()));
    }

    @Test
    public void testUnscoreGameWithoutExistingGame() {
        final User user = createUser();

        final Game game = Mockito.mock(Game.class);
        Mockito.when(game.getId()).thenReturn(1L);

        final UserGameScore gameScore = user.unscoreGame(game);
        Assert.assertNull(UNSCORING_ERROR, gameScore);

        final Map<Long, Integer> scoredGamed = user.getScoredGames();
        Assert.assertNotNull(UNSCORING_ERROR, scoredGamed);
        Assert.assertFalse(UNSCORING_ERROR, scoredGamed.containsKey(game.getId()));
        Assert.assertNull(UNSCORING_ERROR, scoredGamed.get(game.getId()));
    }

    @Test
    public void testAddAuthority() {
        final User user = createUser();
        final Authority authority = getRandomAuthority();

        user.addAuthority(authority);

        Assert.assertNotNull(ADD_AUTHORITY_ERROR, user.getAuthorities());
        Assert.assertTrue(ADD_AUTHORITY_ERROR, user.getAuthorities().contains(authority));
    }

    @Test
    public void testAddSameAuthorityTwice() {
        final User user = createUser();
        final Authority authority = getRandomAuthority();

        user.addAuthority(authority);
        user.addAuthority(authority);

        Assert.assertNotNull(ADD_AUTHORITY_ERROR, user.getAuthorities());
        Assert.assertTrue(ADD_AUTHORITY_ERROR, user.getAuthorities().contains(authority));
    }

    @Test
    public void testRemoveContainedAuthority() {
        final User user = createUser();
        final Authority authority = getRandomAuthority();

        user.addAuthority(authority);
        user.removeAuthority(authority);

        Assert.assertNotNull(REMOVE_AUTHORITY_ERROR, user.getAuthorities());
        Assert.assertFalse(REMOVE_AUTHORITY_ERROR, user.getAuthorities().contains(authority));
    }

    @Test
    public void testRemoveNotContainedAuthority() {
        final User user = createUser();
        final Authority authority = getRandomAuthority();

        user.removeAuthority(authority);

        Assert.assertNotNull(REMOVE_AUTHORITY_ERROR, user.getAuthorities());
        Assert.assertFalse(REMOVE_AUTHORITY_ERROR, user.getAuthorities().contains(authority));
    }

    // Not testing several combinations of authorities (adding two and removing one, or similar tests)
    // Because there is only one authority in the enum


    // ==== Missing username ====

    @Test(expected = ValidationException.class)
    public void testMissingUsernameThrowsException() {
        new User(
                null,
                generateRandomEmailWithCorrectLength(),
                FAKER.internet().password()
        );
    }

    @Test
    public void testMissingUsernameValueErrors() {
        try {
            testMissingUsernameThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_USERNAME));
        }
    }


    // ==== Missing email ====

    @Test(expected = ValidationException.class)
    public void testMissingEmailThrowsException() {
        new User(
                generateRandomUsernameWithCorrectLength(),
                null,
                FAKER.internet().password()
        );
    }

    @Test
    public void testMissingEmailValueErrors() {
        try {
            testMissingEmailThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_E_MAIL));
        }
    }


    // ==== Missing password ====

    @Test(expected = IllegalArgumentException.class)
    public void testMissingPasswordThrowsExceptionOnCreate() {
        new User(
                generateRandomUsernameWithCorrectLength(),
                generateRandomEmailWithCorrectLength(),
                null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingPasswordThrowsExceptionOnUpdate() {
        createUser().changePassword(null);
    }


    // ==== Short username ====

    @Test(expected = ValidationException.class)
    public void testShortUsernameThrowsException() {
        new User(
                generateShortUsername(),
                generateRandomEmailWithCorrectLength(),
                FAKER.internet().password()
        );
    }

    @Test
    public void testShortUsernameValueErrors() {
        try {
            testShortUsernameThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.USERNAME_TOO_SHORT));
        }
    }


    // ==== Long username ====

    @Test(expected = ValidationException.class)
    public void testLongUsernameThrowsException() {
        new User(
                generateLongUsername(),
                generateRandomEmailWithCorrectLength(),
                FAKER.internet().password()
        );
    }

    @Test
    public void testLongUsernameValueErrors() {
        try {
            testLongUsernameThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.USERNAME_TOO_LONG));
        }
    }


    // ==== Short email

    // This is not tested because creating a valid email needs at least 5 characters (i.e <char>@<char>.<char>)
    // And the min. length for an email is 1 (should test if the min changes, though)


    // ==== Long email ====

    @Test(expected = ValidationException.class)
    public void testLongEmailThrowsException() {
        new User(
                generateRandomUsernameWithCorrectLength(),
                generateLongEmail(),
                FAKER.internet().password()
        );
    }

    @Test
    public void testLongEmailValueErrors() {
        try {
            testLongEmailThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.E_MAIL_TOO_LONG));
        }
    }


    // ==== Missing game on play status ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsExceptionOnSetPlayStatus() {
        final User user = createUser();
        final PlayStatus playStatus = UserGameStatusTest.getRandomPlayStatus();
        user.setPlayStatus(null, playStatus);
    }

    @Test
    public void testMissingGameValueErrorsOnSetPlayStatus() {
        try {
            testMissingGameThrowsExceptionOnSetPlayStatus();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsExceptionOnRemovePlayStatus() {
        final User user = createUser();
        user.removePlayStatus(null);
    }

    @Test
    public void testMissingGameValueErrorsOnRemovePlayStatus() {
        try {
            testMissingGameThrowsExceptionOnRemovePlayStatus();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }


    // ==== Missing game on score ====

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsExceptionOnSetScore() {
        final User user = createUser();
        final int score = UserGameScoreTest.generateRandomScoreInCorrectRange();
        user.scoreGame(null, score);
    }

    @Test
    public void testMissingGameValueErrorsOnSetScore() {
        try {
            testMissingGameThrowsExceptionOnSetScore();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingGameThrowsExceptionOnRemoveScore() {
        final User user = createUser();
        user.unscoreGame(null);
    }

    @Test
    public void testMissingGameValueErrorsOnRemoveScore() {
        try {
            testMissingGameThrowsExceptionOnRemoveScore();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE, e.getErrors().contains(ValueErrorConstants.MISSING_GAME));
        }
    }


    // ==== Missing play status ====

    @Test(expected = ValidationException.class)
    public void testMissingPlayStatusThrowsException() {
        final User user = createUser();
        final Game game = Mockito.mock(Game.class);

        user.setPlayStatus(game, null);
    }

    @Test
    public void testMissingPlayStatusValueErrors() {
        try {
            testMissingPlayStatusThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_PLAY_STATUS));
        }
    }


    // ==== Missing score ====

    @Test(expected = ValidationException.class)
    public void testMissingScoreThrowsException() {
        final User user = createUser();
        final Game game = Mockito.mock(Game.class);

        user.scoreGame(game, null);
    }

    @Test
    public void testMissingScoreValueErrors() {
        try {
            testMissingScoreThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_GAME_SCORE));
        }
    }


    // ==== Score below lower limit ====

    @Test(expected = ValidationException.class)
    public void testBelowLowerLimitThrowsException() {
        final User user = createUser();
        final Game game = Mockito.mock(Game.class);
        final int score = UserGameScoreTest.generateRandomScoreBelowLowerLimit();

        user.scoreGame(game, score);
    }

    @Test
    public void testBelowLowerLimitValueErrors() {
        try {
            testBelowLowerLimitThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GAME_SCORE_BELOW_MIN));
        }
    }


    // ==== Score above upper limit ====

    @Test(expected = ValidationException.class)
    public void testAboveUpperLimitThrowsException() {
        final User user = createUser();
        final Game game = Mockito.mock(Game.class);
        final int score = UserGameScoreTest.generateRandomScoreAboveUpperLimit();

        user.scoreGame(game, score);
    }

    @Test
    public void testAboveUpperLimitValueErrors() {
        try {
            testAboveUpperLimitThrowsException();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.GAME_SCORE_ABOVE_MAX));
        }
    }


    // ==== Missing authority ====

    @Test(expected = ValidationException.class)
    public void testMissingAuthorityThrowsExceptionOnAdd() {
        createUser().addAuthority(null);
    }

    @Test
    public void testMissingAuthorityValueErrorsOnAdd() {
        try {
            testMissingAuthorityThrowsExceptionOnAdd();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_AUTHORITY));
        }
    }

    @Test(expected = ValidationException.class)
    public void testMissingAuthorityThrowsExceptionOnRemove() {
        createUser().removeAuthority(null);
    }

    @Test
    public void testMissingAuthorityValueErrorsOnRemove() {
        try {
            testMissingAuthorityThrowsExceptionOnRemove();
        } catch (ValidationException e) {
            Assert.assertTrue(MISSING_VALUE_ERRORS_MESSAGE,
                    e.getErrors().contains(ValueErrorConstants.MISSING_AUTHORITY));
        }
    }


    // TODO: test changeProfilePicture


    // ==== Helper methods ====

    /**
     * @return A {@link User} (with valid arguments).
     */
    private static User createUser() {
        final String username = generateRandomUsernameWithCorrectLength();
        final String email = generateRandomEmailWithCorrectLength();
        final String password = FAKER.internet().password();

        return new User(username, email, password);
    }

    /**
     * @return A random username whose length is between the valid limits.
     */
    private static String generateRandomUsernameWithCorrectLength() {
        int tries = 0;
        // Try to get a username with the correct length
        while (tries < MAGIC_NUMBER) {
            final String username = FAKER.name().username();
            final int length = username.length();
            if (length >= NumericConstants.USERNAME_MIN_LENGTH && length <= NumericConstants.USERNAME_MAX_LENGTH) {
                return username;
            }
            tries++;
        }
        // In case a username could not be get, generate any string
        return IntStream.range(0, NumericConstants.USERNAME_MIN_LENGTH)
                .mapToObj(num -> (char) num)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    /**
     * @return A random username whose length is below the valid limit.
     */
    private static String generateShortUsername() {
        return FAKER.lorem()
                .fixedString(NumericConstants.USERNAME_MIN_LENGTH - 1)
                .replaceAll("\\s+", ""); // Removes all white spaces (will make it even shorter)
    }

    /**
     * @return A random username whose length is above the valid limit.
     */
    private static String generateLongUsername() {
        return FAKER.lorem()
                .fixedString(NumericConstants.USERNAME_MAX_LENGTH + 1)
                .replaceAll("\\s+", "a"); // Changes all white spaces into any character
    }

    /**
     * @return A random email whose length is between the valid limits.
     */
    private static String generateRandomEmailWithCorrectLength() {
        int tries = 0;
        // Try to get a username with the correct length
        while (tries < MAGIC_NUMBER) {
            final String email = FAKER.internet().safeEmailAddress();
            final int length = email.length();
            if (length >= NumericConstants.EMAIL_MIN_LENGTH && length <= NumericConstants.EMAIL_MAX_LENGTH) {
                return email;
            }
            tries++;
        }
        final String domain = "a.a";
        // In case a username could not be get, generate any string
        return IntStream.range(0, NumericConstants.EMAIL_MIN_LENGTH - domain.length() - 1)
                .mapToObj(num -> (char) num)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .append("@")
                .append(domain)
                .toString();
    }

    /**
     * @return A random email whose length is above the valid limit.
     */
    private static String generateLongEmail() {
        return IntStream.range(0, NumericConstants.EMAIL_MAX_LENGTH + 1)
                .mapToObj(num -> (char) 'a')
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .append("@a.a")
                .toString();
    }

    /**
     * @return A randomly chosen {@link Authority}.
     */
    private static Authority getRandomAuthority() {
        final Authority[] authorities = Authority.values();
        final int amountOfAuthorities = authorities.length;
        return authorities[new Random().nextInt(amountOfAuthorities)];
    }
}
