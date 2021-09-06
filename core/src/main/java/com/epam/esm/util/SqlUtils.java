package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;

import java.util.Map;

public class SqlUtils {

    private static final String ORDER_BY = " ORDER BY ";
    private static final String WHERE = " WHERE ";
    private static final String LIKE = " LIKE ";
    private static final String ASCENDING = " ASC";
    private static final String DESCENDING = " DESC";
    private static final String WILDCARD_ANY = "%";
    private static final String SINGLE_QUOTE = "'";
    private static final String COMMA = ",";
    private static final String EQUAL = " = ";
    private static final String ID = " ID = ";

    public static String constructQueryForSortingByFieldInOrder(String query, String fieldName, boolean isAsc) {
        StringBuilder stringBuilder = new StringBuilder(query)
                .append(ORDER_BY)
                .append(fieldName);

        if (isAsc) {
            stringBuilder.append(ASCENDING);
        } else {
            stringBuilder.append(DESCENDING);
        }

        return stringBuilder.toString();
    }

    public static String constructQueryForGettingByPartOfField(String query, String fieldName, String part) {
        return query + WHERE + fieldName + LIKE + SINGLE_QUOTE + WILDCARD_ANY + part + WILDCARD_ANY + SINGLE_QUOTE;
    }

    public static String constructQueryForUpdating(String query, long id, Map<String, String> fieldNameValueForUpdate) {
        StringBuilder stringBuilder = new StringBuilder(query);

        for (Map.Entry<String, String> entry : fieldNameValueForUpdate.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            if (fieldName != null) {
                stringBuilder
                        .append(fieldName)
                        .append(EQUAL)
                        .append(SINGLE_QUOTE)
                        .append(fieldValue)
                        .append(SINGLE_QUOTE)
                        .append(COMMA);
            }
        }

        int lastInsertedCommaIndex = stringBuilder.length() - 1;
        stringBuilder.deleteCharAt(lastInsertedCommaIndex);

        stringBuilder.append(WHERE).append(ID).append(id);

        return stringBuilder.toString();
    }
}
