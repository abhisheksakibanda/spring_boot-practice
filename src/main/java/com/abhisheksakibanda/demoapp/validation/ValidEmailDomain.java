package com.abhisheksakibanda.demoapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidEmailDomainValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmailDomain {
    String message() default "Email domain must end with '.edu'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class ValidEmailDomainValidator implements ConstraintValidator<ValidEmailDomain, String> {

//    Not required to implement the initialize method in this case.
//    @Override
//    public void initialize(ValidEmailDomain validEmailDomain) {
//        ConstraintValidator.super.initialize(validEmailDomain);
//    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.endsWith(".edu");
    }
}
