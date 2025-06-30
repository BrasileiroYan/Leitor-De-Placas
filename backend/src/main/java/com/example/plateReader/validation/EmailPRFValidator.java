package com.example.plateReader.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailPRFValidator implements ConstraintValidator<EmailPRF, String> {

    @Override
    public void initialize(EmailPRF constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(email == null) {
            return true;
        }

        return email.endsWith("@prf.gov.br");
    }
}
