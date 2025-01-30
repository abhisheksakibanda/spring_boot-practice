package com.abhisheksakibanda.demoapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public @interface ValidEmailDomain {
    String message() default "Email domain must end with '.edu'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class ValidEmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

    @Override
        public void initialize(ValidEmailDomain validEmailDomain) {
        ConstraintValidator.super.initialize(validEmailDomain);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.endsWith(".edu");
    }
}
