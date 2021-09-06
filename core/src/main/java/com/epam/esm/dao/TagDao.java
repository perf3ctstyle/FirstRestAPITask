package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.mapper.TagRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDao {

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    private static final String GET_ALL = "SELECT * FROM TAG";
    private static final String GET_BY_ID = "SELECT * FROM TAG WHERE ID = ?";
    private static final String GET_BY_NAME = "SELECT * FROM TAG WHERE NAME = ?";
    private static final String CREATE = "INSERT INTO TAG(NAME) VALUES(?)";
    private static final String DELETE = "DELETE FROM TAG WHERE ID=?";

    private static final int NAME_POSITION = 1;

    private static final String MORE_ENTITIES_THAN_EXPECTED = "Expected 1 entity, but received more.";

    public TagDao(JdbcTemplate jdbcTemplate, TagRowMapper tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, tagRowMapper);
    }

    public Optional<Tag> getById(long id) {
        return getTagByUniqueParameters(GET_BY_ID, id);
    }

    public Optional<Tag> getByName(String name) {
        return getTagByUniqueParameters(GET_BY_NAME, name);
    }

    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(NAME_POSITION, tag.getName());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(long id) {
        jdbcTemplate.update(DELETE, id);
    }

    private Optional<Tag> getTagByUniqueParameters(String query, Object... uniqueParameters) {
        List<Tag> entities = jdbcTemplate.query(query, tagRowMapper, uniqueParameters);

        if (entities.size() > 1) {
            throw new DaoException(MORE_ENTITIES_THAN_EXPECTED);
        } else if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        return Optional.empty();
    }
}
