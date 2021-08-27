package com.epam.esm.utils;

import java.util.List;

public class SqlUtils {

    private static final String ORDER_BY = " ORDER BY ";
    private static final String WHERE = " WHERE ";
    private static final String LIKE = " LIKE ";
    private static final String ASCENDING = " ASC";
    private static final String DESCENDING = " DESC";
    private static final String WILDCARD_ANY = "%";

    private static final String FIELD_NOT_FOUND = " field wasn't found.";

    public static String sortInOrder(String query, String fieldName, List<String> fields, boolean isAsc) {
        if (!fields.contains(fieldName)) {
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

    public static String getByPartOfField(String query, String fieldName, List<String> fields, String part) {
        if (!fields.contains(fieldName)) {
            throw new IllegalArgumentException(fieldName + FIELD_NOT_FOUND);
        }
        return query + WHERE + fieldName + LIKE + WILDCARD_ANY + part + WILDCARD_ANY;
    }
}
