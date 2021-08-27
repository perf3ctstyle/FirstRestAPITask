package com.epam.esm.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class GiftAndTagDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_BY_CERTIFICATE_ID = "SELECT TAG_ID FROM GIFTS_AND_TAGS WHERE CERTIFICATE_ID = ?";
    private static final String GET_BY_TAG_ID = "SELECT CERTIFICATE_ID FROM GIFTS_AND_TAGS WHERE TAG_ID = ?";
    private static final String CREATE = "INSERT INTO GIFTS_AND_TAGS(CERTIFICATE_ID, TAG_ID) VALUES(?, ?)";

    @Autowired
    public GiftAndTagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Long> getTagIdsByCertificateId(Long certificateId) {
        return jdbcTemplate.queryForList(GET_BY_CERTIFICATE_ID, Long.class, certificateId);
    }

    public List<Long> getCertificateIdsByTagId(Long tagId) {
        return jdbcTemplate.queryForList(GET_BY_TAG_ID, Long.class, tagId);
    }

    public void create(Long certificateId, Long tagId) {
        jdbcTemplate.update(CREATE, certificateId, tagId);
    }
}
