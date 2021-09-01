package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RequiredFieldsMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class GiftCertificateValidatorTest {

    private final GiftCertificateValidator validator = new GiftCertificateValidator();

    private static final String NAME = "someName";
    private static final String DESCRIPTION = "someDescription";
    private static final Integer PRICE = 100;
    private static final Long DURATION = 10L;

    @Test
    public void testShouldWorkCorrectlyWhenAllFieldsAreFilledForCreation() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);

        validator.checkFieldsRequiredForCreation(giftCertificate);
    }

    @Test
    public void testShouldThrowExceptionWhenNoFieldsAreFilledForCreation() {
        assertThrows(RequiredFieldsMissingException.class, () -> {
            GiftCertificate giftCertificate = new GiftCertificate();
            validator.checkFieldsRequiredForCreation(giftCertificate);
        });
    }

    @Test
    public void testShouldThrowExceptionWhenNameFieldNotFilledForCreation() {
        assertThrows(RequiredFieldsMissingException.class, () -> {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setDescription(DESCRIPTION);
            giftCertificate.setPrice(PRICE);
            giftCertificate.setDuration(DURATION);

            validator.checkFieldsRequiredForCreation(giftCertificate);
        });
    }

    @Test
    public void testShouldThrowExceptionWhenDescriptionFieldNotFilledForCreation() {
        assertThrows(RequiredFieldsMissingException.class, () -> {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setName(NAME);
            giftCertificate.setPrice(PRICE);
            giftCertificate.setDuration(DURATION);

            validator.checkFieldsRequiredForCreation(giftCertificate);
        });
    }

    @Test
    public void testShouldThrowExceptionWhenPriceFieldNotFilledForCreation() {
        assertThrows(RequiredFieldsMissingException.class, () -> {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setName(NAME);
            giftCertificate.setDescription(DESCRIPTION);
            giftCertificate.setDuration(DURATION);

            validator.checkFieldsRequiredForCreation(giftCertificate);
        });
    }

    @Test
    public void testShouldThrowExceptionWhenDurationFieldNotFilledForCreation() {
        assertThrows(RequiredFieldsMissingException.class, () -> {
            GiftCertificate giftCertificate = new GiftCertificate();
            giftCertificate.setName(NAME);
            giftCertificate.setDescription(DESCRIPTION);
            giftCertificate.setPrice(PRICE);

            validator.checkFieldsRequiredForCreation(giftCertificate);
        });
    }

    @Test
    public void testShouldWorkCorrectlyWhenValueIsPositive() {
        int value = 2;

        validator.checkValueIsPositive(value);
    }

    @Test
    public void testShouldThrowExceptionWhenValueIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            int value = -1;

            validator.checkValueIsPositive(value);
        });
    }

}
