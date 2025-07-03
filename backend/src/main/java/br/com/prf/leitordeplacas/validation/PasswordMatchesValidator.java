package br.com.prf.leitordeplacas.validation;

import br.com.prf.leitordeplacas.dto.ChangePasswordRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        ChangePasswordRequestDTO user = (ChangePasswordRequestDTO) obj;

        if(user.getNewPassword() != null && user.getNewPassword().equals(user.getConfirmNewPassword())) {
            return true;
        }

        return false;
    }
}
