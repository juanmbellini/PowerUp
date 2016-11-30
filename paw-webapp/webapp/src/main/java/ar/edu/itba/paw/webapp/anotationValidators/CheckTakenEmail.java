package ar.edu.itba.paw.webapp.anotationValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckTakenEmailValidator.class)
@Documented
public @interface CheckTakenEmail {

    String message() default "Email already taken";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
