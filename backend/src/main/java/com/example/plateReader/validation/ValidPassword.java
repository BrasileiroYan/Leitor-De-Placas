package com.example.plateReader.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "A senha deve conter no mínimo um caractere especial, uma letra maiúscula, uma letra minúscula e um número.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
