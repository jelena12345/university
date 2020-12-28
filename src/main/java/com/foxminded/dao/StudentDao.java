package com.foxminded.dao;

import com.foxminded.dto.Student;
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
public class StudentDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> findAll() {
        return jdbcTemplate.query("SELECT id, name, surname FROM students",
                new BeanPropertyRowMapper<>(Student.class));
    }

    public Student findById(int id) {
        return jdbcTemplate.query("SELECT id, name, surname FROM students WHERE id=?",
                new BeanPropertyRowMapper<>(Student.class), id).stream().findAny().orElse(null);
    }

    public int add(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO students(name, surname) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, student.getName());
            ps.setString(2, student.getSurname());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Student student) {
        jdbcTemplate.update("UPDATE students SET name=?, surname=? WHERE id=?",
                student.getName(), student.getSurname(), student.getId());
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM students WHERE id=?", id);
    }
}
