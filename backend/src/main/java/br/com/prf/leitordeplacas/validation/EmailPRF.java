package br.com.prf.leitordeplacas.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailPRFValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailPRF {

    String message() default "O email deve ser do domínio @prf.gov.br";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
