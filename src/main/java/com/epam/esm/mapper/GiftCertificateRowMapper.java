package com.epam.esm.mapper;

import com.epam.esm.utils.DateTimeUtils;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String PRICE = "PRICE";
    private static final String DURATION = "DURATION";
    private static final String CREATE_DATE = "CREATE_DATE";
    private static final String LAST_UPDATE_DATE = "LAST_UPDATE_DATE";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong(ID);
        String name = resultSet.getString(NAME);
        String description = resultSet.getString(DESCRIPTION);
        Integer price = resultSet.getInt(PRICE);

        long duration = resultSet.getLong(DURATION);

        String createDateString = resultSet.getString(CREATE_DATE);
        LocalDateTime createDate = DateTimeUtils.of(createDateString, DATE_TIME_PATTERN);

        String lastUpdateDateString = resultSet.getString(LAST_UPDATE_DATE);
        LocalDateTime lastUpdateDate = DateTimeUtils.of(lastUpdateDateString, DATE_TIME_PATTERN);

        return new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
    }
}
