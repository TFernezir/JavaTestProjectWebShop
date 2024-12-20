package example.WebShopTrening.CustomValidations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotEmptyOrWhitespaceValidator.class)
public @interface NotEmptyOrWhitespace {
    String message() default "String cannot be empty or whitespace";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}