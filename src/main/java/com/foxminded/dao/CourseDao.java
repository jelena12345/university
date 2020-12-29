package com.foxminded.dao;

import com.foxminded.dto.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Component
public class CourseDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Course> findAll() {
        return jdbcTemplate.query("SELECT id, name, description FROM courses",
                new BeanPropertyRowMapper<>(Course.class));
    }

    public Course findById(int id) {
        return jdbcTemplate.query("SELECT id, name, description FROM courses WHERE id=?",
                new BeanPropertyRowMapper<>(Course.class), id).stream().findAny().orElse(null);
    }

    public Integer add(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO courses(name, description) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getName());
            ps.setString(2, course.getDescription());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Course course) {
        jdbcTemplate.update("UPDATE courses SET name=?, description=? WHERE id=?",
                course.getName(), course.getDescription(), course.getId());
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM courses WHERE id=?", id);
    }
}
