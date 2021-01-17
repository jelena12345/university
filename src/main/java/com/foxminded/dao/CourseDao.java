package com.foxminded.dao;

import com.foxminded.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CourseDao {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public CourseDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Course> findAll() {
        return template.query("SELECT id, name, description FROM courses",
                new BeanPropertyRowMapper<>(Course.class));
    }

    public Course findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        try {
            return template.queryForObject("SELECT id, name, description FROM courses WHERE id=:id",
                    params,
                    new BeanPropertyRowMapper<>(Course.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Course findByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(NAME, name);
        try {
            return template.queryForObject("SELECT id, name, description FROM courses WHERE name=:name",
                    params,
                    new BeanPropertyRowMapper<>(Course.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer add(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(NAME, course.getName())
                .addValue(DESCRIPTION, course.getDescription());

        template.update("INSERT INTO courses(name, description) VALUES(:name, :description)",
                params,
                keyHolder,
                new String[]{ID});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(int id, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ID, id)
                .addValue(NAME, course.getName())
                .addValue(DESCRIPTION, course.getDescription());
        template.update("UPDATE courses SET name=:name, description=:description WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        template.update("DELETE FROM courses WHERE id=:id", params);
    }

    public void deleteByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(NAME, name);
        template.update("DELETE FROM courses WHERE name=:name", params);
    }

    public boolean existsById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ID, id);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM courses WHERE id=:id)", params, Boolean.class));
    }

    public boolean existsByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(NAME, name);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM courses WHERE name=:name)", params, Boolean.class));
    }
}
