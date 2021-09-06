package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RequiredFieldsMissingException;
import org.apache.commons.lang3.StringUtils;

public class TagValidator {

    private static final String REQUIRED_FIELDS_MISSING = "Some of the required fields were missing.";

    public void validateForCreation(Tag tag) {
        String name = tag.getName();

        if (StringUtils.isBlank(name)) {
            throw new RequiredFieldsMissingException(REQUIRED_FIELDS_MISSING);
        }
    }
}
