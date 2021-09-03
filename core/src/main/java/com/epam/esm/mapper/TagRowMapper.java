package com.epam.esm.mapper;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    private static final String ID = "ID";
    private static final String NAME = "NAME";

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong(ID);
        String name = resultSet.getString(NAME);

        return new Tag(id, name);
    }
}
