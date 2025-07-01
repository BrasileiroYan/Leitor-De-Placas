package br.com.prf.leitordeplacas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {

    String message() default "A nova senha não confere com a sua confirmação.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
