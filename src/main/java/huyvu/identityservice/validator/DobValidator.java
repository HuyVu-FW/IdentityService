package huyvu.identityservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;


/// ///////////////////////////////////////////////////////tên của anotaion và , kiểu dữ liệu muốn validate
public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

    private int min;
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
          // việc xử lí null để cho Anotaion khác làm
        if(Objects.isNull(value))
            return true;
        long year = ChronoUnit.YEARS.between(value,LocalDate.now());


        return year >= min;
    }
}
