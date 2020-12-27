package com.foxminded.dao;

import com.foxminded.dto.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void add(Course course) {
        jdbcTemplate.update("INSERT INTO courses(name, description) VALUES(?, ?)",
                course.getName(), course.getDescription());
    }

    public void update(Course course) {
        jdbcTemplate.update("UPDATE courses SET name=?, description=? WHERE id=?",
                course.getName(), course.getDescription(), course.getId());
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM courses WHERE id=?", id);
    }
}
