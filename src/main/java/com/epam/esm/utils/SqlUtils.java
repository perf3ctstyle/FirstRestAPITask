package com.epam.esm.utils;

import java.util.List;

public class SqlUtils {

    private static final String ORDER_BY = " ORDER BY ";
    private static final String WHERE = " WHERE ";
    private static final String LIKE = " LIKE ";
    private static final String ASCENDING = " ASC";
    private static final String DESCENDING = " DESC";
    private static final String WILDCARD_ANY = "%";
    private static final String SINGLE_QUOTE = "'";

    private static final String FIELD_NOT_FOUND = " field wasn't found.";

    public static String constructQueryForSortingInOrder(String query, String fieldName, List<String> fields, boolean isAsc) {
        boolean doesFieldExist = false;
        for (String field : fields) {
            if (field.equalsIgnoreCase(fieldName)) {
                doesFieldExist = true;
                break;
            }
        }

        if (!doesFieldExist) {
            throw new IllegalArgumentException(fieldName + FIELD_NOT_FOUND);
        }

        StringBuilder stringBuilder = new StringBuilder(query);

        stringBuilder.append(ORDER_BY);
        stringBuilder.append(fieldName);
        if (isAsc) {
            stringBuilder.append(ASCENDING);
        } else {
            stringBuilder.append(DESCENDING);
        }

        return stringBuilder.toString();
    }

    public static String constructQueryForGettingByPartOfField(String query, String fieldName, List<String> fields, String part) {
        boolean doesFieldExist = false;
        for (String field : fields) {
            if (field.equalsIgnoreCase(fieldName)) {
                doesFieldExist = true;
                break;
            }
        }

        if (!doesFieldExist) {
            throw new IllegalArgumentException(fieldName + FIELD_NOT_FOUND);
        }

        return query + WHERE + fieldName + LIKE + SINGLE_QUOTE + WILDCARD_ANY + part + WILDCARD_ANY + SINGLE_QUOTE;
    }
}
