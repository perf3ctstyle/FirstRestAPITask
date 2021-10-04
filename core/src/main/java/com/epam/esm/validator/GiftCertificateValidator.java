package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.RequiredFieldsMissingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateValidator {

    private static final String REQUIRED_FIELDS_MISSING = "Some of the required fields were missing.";
    private static final String NEGATIVE_VALUE_PROHIBITED = "Negative value was found in a field that is supposed to be positive.";
    private static final String EMPTY_VALUE = "Some of the fields didn't equal null, but had empty values";

    public void validateForCreation(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        Integer price = giftCertificate.getPrice();
        Long duration = giftCertificate.getDuration();

        if (!StringUtils.isNoneBlank(name, description)
                || price       == null
                || duration    == null) {
            throw new RequiredFieldsMissingException(REQUIRED_FIELDS_MISSING);
        }

        if (price <= 0 || duration <= 0) {
            throw new IllegalArgumentException(NEGATIVE_VALUE_PROHIBITED);
        }
    }

    public void validateForUpdate(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        String description = giftCertificate.getDescription();
        Integer price = giftCertificate.getPrice();
        Long duration = giftCertificate.getDuration();

        if (StringUtils.isWhitespace(name) || StringUtils.isWhitespace(description)) {
            throw new IllegalArgumentException(EMPTY_VALUE);
        }

        if ((price != null && price <= 0) || (duration != null && duration <= 0))  {
            throw new IllegalArgumentException(NEGATIVE_VALUE_PROHIBITED);
        }
    }
}