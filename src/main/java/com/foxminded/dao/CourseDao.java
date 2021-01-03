package com.foxminded.dao;

import com.foxminded.dto.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CourseDao {

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
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return template.query("SELECT id, name, description FROM courses WHERE id=:id",
                params,
                new BeanPropertyRowMapper<>(Course.class))
                .stream()
                .findAny()
                .orElse(null);
    }

    public Integer add(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", course.getName())
                .addValue("description", course.getDescription());

        template.update("INSERT INTO courses(name, description) VALUES(:name, :description)",
                params,
                keyHolder,
                new String[]{"id"});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", course.getId())
                .addValue("name", course.getName())
                .addValue("description", course.getDescription());
        template.update("UPDATE courses SET name=:name, description=:description WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        template.update("DELETE FROM courses WHERE id=:id", params);
    }
}
