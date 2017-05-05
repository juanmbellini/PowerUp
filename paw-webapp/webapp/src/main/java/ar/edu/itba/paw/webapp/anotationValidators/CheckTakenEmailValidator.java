package ar.edu.itba.paw.webapp.anotationValidators;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckTakenEmailValidator implements ConstraintValidator<CheckTakenEmail, String> {


    /**
     * A user service to make user operations.
     */
    @Autowired
    private  UserService userService;

    @Override
    public void initialize(CheckTakenEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if ( object == null ) {
            return true;
        }

        return userService.findByEmail(object) == null;
//        return ! userService.existsWithEmail(object);
    }
}