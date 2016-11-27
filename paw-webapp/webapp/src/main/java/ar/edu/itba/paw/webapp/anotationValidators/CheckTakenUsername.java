package ar.edu.itba.paw.webapp.anotationValidators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckTakenUsernameValidator.class)
@Documented
public @interface CheckTakenUsername {

    String message() default "Username already taken";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
