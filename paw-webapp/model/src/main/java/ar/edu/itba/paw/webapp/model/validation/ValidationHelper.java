package ar.edu.itba.paw.webapp.model.validation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class containing static methods for validation.
 * <p>
 * Created by Juan Marcos Bellini on 13/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
public class ValidationHelper {


    /**
     * Checks that the given {@code object} is not null.
     *
     * @param object         The object to be checked.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     */
    public static void objectNotNull(Object object, List<ValueError> errorList, ValueError nullFieldError) {
        if (object == null) {
            errorList.add(nullFieldError);
        }
    }


    /**
     * Checks that the given {@code number} is not null, and that it is between {@code min} and {@code max}.
     *
     * @param number         The number to be checked.
     * @param min            The min. value.
     * @param max            The max. value.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     * @param belowMinError  The error to be added on "below min" situation.
     * @param aboveMaxError  The error to be added on "above max" situation.
     */
    public static void intNotNullAndLengthBetweenTwoValues(Integer number, int min, int max, List<ValueError> errorList,
                                                           ValueError nullFieldError,
                                                           ValueError belowMinError,
                                                           ValueError aboveMaxError) {
        if (number == null) {
            errorList.add(nullFieldError);
        } else {
            intIsBetweenTwoValues(number, min, max, errorList, belowMinError, aboveMaxError);
        }
    }

    /**
     * Checks that the given {@code number} is between {@code min} and {@code max}, if it's not null.
     *
     * @param number        The number to be checked.
     * @param min           The min. value.
     * @param max           The max. value.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param belowMinError The error to be added on "too short" length.
     * @param aboveMaxError The error to be added on "too long" length.
     */
    public static void intNullOrLengthBetweenTwoValues(Integer number, int min, int max, List<ValueError> errorList,
                                                       ValueError belowMinError,
                                                       ValueError aboveMaxError) {
        if (number != null) {
            intIsBetweenTwoValues(number, min, max, errorList, belowMinError, aboveMaxError);
        }
    }

    /**
     * Checks that the given {@code text} is not null,
     * and that its length is between {@code minLength} and {@code maxLength}.
     *
     * @param text           The text to be checked.
     * @param minLength      The text's min. length.
     * @param maxLength      The text's max. length.
     * @param errorList      The list containing the errors, in which new possible errors will be added.
     * @param nullFieldError The error to be added on null value.
     * @param tooShortError  The error to be added on "too short" length.
     * @param tooLongError   The error to be added on "too long" length.
     */
    public static void stringNotNullAndLengthBetweenTwoValues(String text, int minLength, int maxLength,
                                                              List<ValueError> errorList,
                                                              ValueError nullFieldError,
                                                              ValueError tooShortError,
                                                              ValueError tooLongError) {
        if (text == null) {
            errorList.add(nullFieldError);
        } else {
            intIsBetweenTwoValues(text.length(), minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given {@code text}'s length is between {@code minLength} and {@code maxLength},
     * if it's not null.
     *
     * @param text          The text to be checked.
     * @param minLength     The text's min. length.
     * @param maxLength     The text's max. length.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param tooShortError The error to be added no "too short" length.
     * @param tooLongError  The error to be added no "too long" length.
     */
    public static void stringNullOrLengthBetweenTwoValues(String text, int minLength, int maxLength,
                                                          List<ValueError> errorList,
                                                          ValueError tooShortError, ValueError tooLongError) {
        if (text != null) {
            intIsBetweenTwoValues(text.length(), minLength, maxLength, errorList, tooShortError, tooLongError);
        }
    }

    /**
     * Checks that the given {@code number} is between {@code min} and {@code max}.
     *
     * @param number        The number to be checked.
     * @param min           The min. number.
     * @param max           The max. number.
     * @param errorList     The list containing the errors, in which new possible errors will be added.
     * @param tooShortError The error to be added on below {@code min} number.
     * @param tooLongError  The error to be added on above {@code max} number.
     */
    public static void intIsBetweenTwoValues(int number, int min, int max, List<ValueError> errorList,
                                             ValueError tooShortError, ValueError tooLongError) {
        if (number < min) {
            errorList.add(tooShortError);
        } else if (number > max) {
            errorList.add(tooLongError);
        }
    }

    /**
     * Checks that there are at most {@code maxAmountOfNulls} null values in the given {@code values}.
     *
     * @param maxAmountOfNulls     The max. amount of nulls allowed.
     * @param errorList            The list containing the errors, in which new possible errors will be added.
     * @param NOT_AT_LEAST_M_NULLS The error to be added on breaking the rule of max. amount of nulls.
     * @param values               The values to be checked.
     */
    public static void maxAmountOfNulls(int maxAmountOfNulls, List<ValueError> errorList,
                                        ValueError NOT_AT_LEAST_M_NULLS, Object... values) {
        if (Arrays.stream(values).filter(each -> each == null)
                .collect(Collectors.toList()).size() > maxAmountOfNulls) {
            errorList.add(NOT_AT_LEAST_M_NULLS);
        }
    }

    /**
     * Checks that any of the given {@code values} are null.
     *
     * @param errorList    The list containing the errors, in which new possible errors will be added.
     * @param anyNullError The error to be added on breaking the rule of max. amount of nulls.
     * @param values       The values to be checked.
     */
    public static void anyNull(List<ValueError> errorList, ValueError anyNullError, Object... values) {
        maxAmountOfNulls(0, errorList, anyNullError, values);
    }


}
