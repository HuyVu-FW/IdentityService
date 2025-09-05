package huyvu.identityservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

//antaion này sẽ được dùng ở đâu
@Target(FIELD)

// sẽ được xử lí ở đâu
@Retention(RetentionPolicy.RUNTIME)

@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {

    //default properties
    String message() default "{DobConstraint.message}";


//    custome  proerties
    int min();


    //default properties

    Class<?>[] groups() default {};
    //default properties

    Class<? extends Payload>[] payload() default {};
}
