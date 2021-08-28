package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.mapper.GiftCertificateRowMapper;
import com.epam.esm.utils.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL = "SELECT * FROM GIFT_CERTIFICATE";
    private static final String GET_BY_ID = "SELECT * FROM GIFT_CERTIFICATE WHERE ID = ?";
    private static final String CREATE = "INSERT INTO GIFT_CERTIFICATE(NAME, DESCRIPTION, PRICE, DURATION, CREATE_DATE, LAST_UPDATE_DATE) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE GIFT_CERTIFICATE SET NAME=?, DESCRIPTION=?, PRICE=?, DURATION=?, CREATE_DATE=?, LAST_UPDATE_DATE=? WHERE ID=?";
    private static final String DELETE = "DELETE FROM GIFT_CERTIFICATE WHERE ID=?";

    private static final List<String> FIELDS = Arrays.asList("NAME", "DESCRIPTION", "DURATION", "CREATE_DATE", "LAST_UPDATE_DATE");

    @Autowired
    public GiftCertificateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GET_ALL, new GiftCertificateRowMapper());
    }

    public Optional<GiftCertificate> getById(long id) {
        List<GiftCertificate> entities = jdbcTemplate.query(GET_BY_ID, new GiftCertificateRowMapper(), id);

        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        } else {
            return Optional.empty();
        }
    }

    public List<GiftCertificate> getByPartOfField(String fieldName, String part) {
        String query = SqlUtils.constructQueryForGettingByPartOfField(GET_ALL, fieldName, FIELDS, part);
        return jdbcTemplate.query(query, new GiftCertificateRowMapper());
    }

    public List<GiftCertificate> sortByFieldInGivenOrder(String fieldName, boolean isAsc) {
        String query = SqlUtils.constructQueryForSortingInOrder(GET_ALL, fieldName, FIELDS, isAsc);
        return jdbcTemplate.query(query, new GiftCertificateRowMapper());
    }

    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setInt(3, giftCertificate.getPrice());
            preparedStatement.setLong(4, giftCertificate.getDuration());
            preparedStatement.setString(5, giftCertificate.getCreateDate().toString());
            preparedStatement.setString(6, giftCertificate.getLastUpdateDate().toString());

            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void update(GiftCertificate giftCertificate) {
        jdbcTemplate.update(UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate().toString(),
                giftCertificate.getLastUpdateDate().toString(),
                giftCertificate.getId());
    }

    public void deleteById(long id) {
        jdbcTemplate.update(DELETE, id);
    }
}
