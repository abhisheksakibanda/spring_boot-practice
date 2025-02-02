package com.abhisheksakibanda.demoapp.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidEmailDomainValidatorTest {

    private final ValidEmailDomainValidator emailDomainValidator = new ValidEmailDomainValidator();

    @Test
    void testValidEmail() {
        assertTrue(emailDomainValidator.isValid("test.mail@university.edu", null));
    }

    @Test
    void testInvalidEmail() {
        assertFalse(emailDomainValidator.isValid("test.mail@personal.com", null));
    }

    @Test
    void testNullEmail() {
        assertThrows(NullPointerException.class, () -> emailDomainValidator.isValid(null, null));
    }
}