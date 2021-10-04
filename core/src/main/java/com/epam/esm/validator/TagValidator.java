package com.epam.esm.validator;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.RequiredFieldsMissingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TagValidator {

    private static final String REQUIRED_FIELDS_MISSING = "Some of the required fields were missing.";
    private static final String EMPTY_VALUE = "Some of the fields didn't equal null, but had empty values";

    public void validateForCreation(Tag tag) {
        String name = tag.getName();

        if (name == null) {
            throw new RequiredFieldsMissingException(REQUIRED_FIELDS_MISSING);
        }

        if (StringUtils.isWhitespace(name)) {
            throw new IllegalArgumentException(EMPTY_VALUE);
        }
    }
}