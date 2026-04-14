package com.Equipo07_SportPulseMS.ms_auth.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || !password.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password must be at least 8 characters, one uppercase and one number"
            ).addConstraintViolation();

            return false;
        }

        return true;
    }
}