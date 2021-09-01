package com.epam.esm.dao.src.main.java;

import com.epam.esm.constant.GiftCertificateConstants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import com.epam.esm.mapper.GiftCertificateRowMapper;
import com.epam.esm.util.SqlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Component
public class GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateRowMapper giftCertificateRowMapper;

    private static final String GET_ALL = "SELECT * FROM GIFT_CERTIFICATE";
    private static final String GET_BY_ID = "SELECT * FROM GIFT_CERTIFICATE WHERE ID = ?";
    private static final String CREATE = "INSERT INTO GIFT_CERTIFICATE(NAME, DESCRIPTION, PRICE, DURATION, CREATE_DATE, LAST_UPDATE_DATE) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE GIFT_CERTIFICATE SET";
    private static final String DELETE = "DELETE FROM GIFT_CERTIFICATE WHERE ID=?";

    private static final int FIRST_POSITION = 1;
    private static final int SECOND_POSITION = 2;
    private static final int THIRD_POSITION = 3;
    private static final int FOURTH_POSITION = 4;
    private static final int FIFTH_POSITION = 5;
    private static final int SIXTH_POSITION = 6;

    private static final String MORE_ENTITIES_THAN_EXPECTED = "Expected 1 entity, but received more.";
    private static final String FIELD_DOES_NOT_EXIST = " field doesn't exist.";

    @Autowired
    public GiftCertificateDao(JdbcTemplate jdbcTemplate, GiftCertificateRowMapper giftCertificateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateRowMapper = giftCertificateRowMapper;
    }

    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GET_ALL, giftCertificateRowMapper);
    }

    public Optional<GiftCertificate> getById(long id) {
        List<GiftCertificate> entities = jdbcTemplate.query(GET_BY_ID, giftCertificateRowMapper, id);

        if (entities.size() > 1) {
            throw new DaoException(MORE_ENTITIES_THAN_EXPECTED);
        } else if (entities.size() == 1){
            return Optional.of(entities.get(0));
        }

        return Optional.empty();
    }

    public List<GiftCertificate> getByPartOfField(String fieldName, String part) {
        checkFieldExistence(fieldName, GiftCertificateConstants.FIELDS);
        String query = SqlUtils.constructQueryForGettingByPartOfField(GET_ALL, fieldName, part);
        return jdbcTemplate.query(query, giftCertificateRowMapper);
    }

    public List<GiftCertificate> sortByFieldInGivenOrder(String fieldName, boolean isAsc) {
        checkFieldExistence(fieldName, GiftCertificateConstants.FIELDS);
        String query = SqlUtils.constructQueryForSortingByFieldInOrder(GET_ALL, fieldName, isAsc);
        return jdbcTemplate.query(query, giftCertificateRowMapper);
    }

    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(FIRST_POSITION, giftCertificate.getName());
            preparedStatement.setString(SECOND_POSITION, giftCertificate.getDescription());
            preparedStatement.setInt(THIRD_POSITION, giftCertificate.getPrice());
            preparedStatement.setLong(FOURTH_POSITION, giftCertificate.getDuration());
            preparedStatement.setString(FIFTH_POSITION, giftCertificate.getCreateDate().toString());
            preparedStatement.setString(SIXTH_POSITION, giftCertificate.getLastUpdateDate().toString());

            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void update(long id, GiftCertificate giftCertificate) {
        String query = SqlUtils.constructQueryForUpdating(UPDATE, id, giftCertificate);
        jdbcTemplate.update(query);
    }

    public void deleteById(long id) {
        jdbcTemplate.update(DELETE, id);
    }

    private void checkFieldExistence(String fieldName, List<String> fields) {
        boolean doesFieldExist = false;
        for (String field : fields) {
            if (field.equalsIgnoreCase(fieldName)) {
                doesFieldExist = true;
                break;
            }
        }

        if (!doesFieldExist) {
            throw new IllegalArgumentException(fieldName + FIELD_DOES_NOT_EXIST);
        }
    }
}
