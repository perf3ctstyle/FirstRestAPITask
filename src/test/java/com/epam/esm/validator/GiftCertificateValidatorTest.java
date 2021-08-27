package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class GiftCertificateValidatorTest {

    private final GiftCertificateValidator validator = new GiftCertificateValidator();

    private static final String NAME = "someName";
    private static final String DESCRIPTION = "someDescription";
    private static final Integer PRICE = 100;
    private static final Long DURATION = 10L;

    @Test
    public void testShouldReturnTrueWhenAllFieldsAreFilledForCreation() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);

        boolean result = validator.areAllFieldsFilledForCreation(giftCertificate);

        Assertions.assertTrue(result);
    }

    @Test
    public void testShouldReturnFalseWhenNotEveryFieldFilledForCreation() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(NAME);
        giftCertificate.setDescription(DESCRIPTION);

        boolean result = validator.areAllFieldsFilledForCreation(giftCertificate);

        Assertions.assertFalse(result);
    }

}
