package ar.edu.itba.paw.webapp.model.validation;

import java.util.List;

import static ar.edu.itba.paw.webapp.model.validation.NumericConstants.MAX_SCORE;
import static ar.edu.itba.paw.webapp.model.validation.NumericConstants.MIN_SCORE;

/**
 * Interface defining behaviour for an object that can check score.
 */
public interface ScoreChecker {

    /**
     * Checks if the given {@code score} is valid.
     *
     * @param score        The score to be checked.
     * @param missingError The {@link ValueError} representing the missing value error.
     * @param belowError   The {@link ValueError} representing a below 0 error.
     * @param aboveError   The {@link ValueError} representing an above 10 error.
     * @param errorList    The list containing errors.
     */
    default void checkScore(Integer score, ValueError missingError, ValueError belowError, ValueError aboveError,
                            List<ValueError> errorList) {
        ValidationHelper.intNotNullAndLengthBetweenTwoValues(score, MIN_SCORE, MAX_SCORE, errorList,
                missingError, belowError, aboveError);

    }
}
