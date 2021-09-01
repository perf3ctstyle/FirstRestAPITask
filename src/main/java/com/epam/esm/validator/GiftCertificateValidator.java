package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RequiredFieldsMissingException;

public class GiftCertificateValidator {

    private static final String REQUIRED_FIELDS_MISSING = "Some of the required fields were missing.";
    private static final String NEGATIVE_VALUE_PROHIBITED = "Negative value was found in a field that is supposed to be positive.";

    public void checkFieldsRequiredForCreation(GiftCertificate giftCertificate) {
        if (giftCertificate.getName() == null
                || giftCertificate.getDescription() == null
                || giftCertificate.getPrice() == null
                || giftCertificate.getDuration() == null) {
            throw new RequiredFieldsMissingException(REQUIRED_FIELDS_MISSING);
        }
    }

    public void checkValueIsPositive(Integer value) {
        if (value != null) {
            if (value <= 0) {
                throw new IllegalArgumentException(NEGATIVE_VALUE_PROHIBITED);
            }
        }
    }

    public void checkValueIsPositive(Long value) {
        if (value != null) {
            if (value <= 0) {
                throw new IllegalArgumentException(NEGATIVE_VALUE_PROHIBITED);
            }
        }
    }
}