package com.epam.esm.validator;

import com.epam.esm.entity.GiftCertificate;

public class GiftCertificateValidator {

    public boolean areAllFieldsFilledForCreation(GiftCertificate giftCertificate) {
        return giftCertificate.getName() != null &&
                giftCertificate.getDescription() != null &&
                giftCertificate.getPrice() != null &&
                giftCertificate.getDuration() != null;
    }
}