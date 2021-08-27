package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.mapper.TagRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class TagDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String GET_ALL = "SELECT * FROM TAG";
    private static final String GET_BY_ID = "SELECT * FROM TAG WHERE ID = ?";
    private static final String GET_BY_NAME = "SELECT * FROM TAG WHERE NAME = ?";
    private static final String CREATE = "INSERT INTO TAG(NAME) VALUES(?)";
    private static final String DELETE = "DELETE FROM TAG WHERE ID=?";

    public TagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, new TagRowMapper());
    }

    public Optional<Tag> getById(long id) {
        List<Tag> entities = jdbcTemplate.query(GET_BY_ID, new TagRowMapper(), id);

        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Tag> getByName(String name) {
        List<Tag> entities = jdbcTemplate.query(GET_BY_NAME, new TagRowMapper(), name);

        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        } else {
            return Optional.empty();
        }
    }

    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(long id) {
        jdbcTemplate.update(DELETE, id);
    }

    public void showMetadata() {
        System.out.println("Check");
        jdbcTemplate.query("SELECT * FROM TAG", (resultSet) -> {
            System.out.println(resultSet.getMetaData().getColumnName(0));
            System.out.println(resultSet.getMetaData().getColumnTypeName(0));
            System.out.println(resultSet.toString());
        });
    }
}
