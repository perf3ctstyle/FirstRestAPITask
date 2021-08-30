package com.epam.esm.util;

public class SqlUtils {

    private static final String ORDER_BY = " ORDER BY ";
    private static final String WHERE = " WHERE ";
    private static final String LIKE = " LIKE ";
    private static final String ASCENDING = " ASC";
    private static final String DESCENDING = " DESC";
    private static final String WILDCARD_ANY = "%";
    private static final String SINGLE_QUOTE = "'";

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
}
