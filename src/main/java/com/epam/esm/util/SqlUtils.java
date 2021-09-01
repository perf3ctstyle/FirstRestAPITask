package com.epam.esm.util;

import com.epam.esm.entity.GiftCertificate;

public class SqlUtils {

    private static final String ORDER_BY = " ORDER BY ";
    private static final String WHERE = " WHERE ";
    private static final String LIKE = " LIKE ";
    private static final String ASCENDING = " ASC";
    private static final String DESCENDING = " DESC";
    private static final String WILDCARD_ANY = "%";
    private static final String SINGLE_QUOTE = "'";
    private static final String COMMA = ",";
    private static final String ID = " ID = ";
    private static final String NAME = " NAME = ";
    private static final String DESCRIPTION = " DESCRIPTION = ";
    private static final String PRICE = " PRICE = ";
    private static final String DURATION = " DURATION = ";
    private static final String CREATE_DATE = " CREATE_DATE = ";
    private static final String LAST_UPDATE_DATE = " LAST_UPDATE_DATE = ";

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

    public static String constructQueryForUpdating(String query, long id, GiftCertificate giftCertificate) {
        StringBuilder stringBuilder = new StringBuilder(query);

        if (giftCertificate.getName() != null) {
            stringBuilder
                    .append(NAME)
                    .append(SINGLE_QUOTE)
                    .append(giftCertificate.getName())
                    .append(SINGLE_QUOTE)
                    .append(COMMA);
        }

        if (giftCertificate.getDescription() != null) {
            stringBuilder
                    .append(DESCRIPTION)
                    .append(SINGLE_QUOTE)
                    .append(giftCertificate.getDescription())
                    .append(SINGLE_QUOTE)
                    .append(COMMA);
        }

        if (giftCertificate.getPrice() != null) {
            stringBuilder
                    .append(PRICE)
                    .append(giftCertificate.getPrice())
                    .append(COMMA);
        }

        if (giftCertificate.getDuration() != null) {
            stringBuilder
                    .append(DURATION)
                    .append(giftCertificate.getDuration())
                    .append(COMMA);
        }

        if (giftCertificate.getCreateDate() != null) {
            stringBuilder
                    .append(CREATE_DATE)
                    .append(SINGLE_QUOTE)
                    .append(giftCertificate.getCreateDate().toString())
                    .append(SINGLE_QUOTE)
                    .append(COMMA);
        }

        if (giftCertificate.getLastUpdateDate() != null) {
            stringBuilder
                    .append(LAST_UPDATE_DATE)
                    .append(SINGLE_QUOTE)
                    .append(giftCertificate.getLastUpdateDate().toString())
                    .append(SINGLE_QUOTE);
        }

        stringBuilder.append(WHERE).append(ID).append(id);

        return stringBuilder.toString();
    }
}
