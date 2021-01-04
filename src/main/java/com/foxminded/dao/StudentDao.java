package com.foxminded.dao;

import com.foxminded.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class StudentDao {
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public StudentDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Student> findAll() {
        return template.query("SELECT id, name, surname FROM students",
                new BeanPropertyRowMapper<>(Student.class));
    }

    public Student findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return template.queryForObject("SELECT id, name, surname FROM students WHERE id=:id",
                params,
                new BeanPropertyRowMapper<>(Student.class));
    }

    public int add(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", student.getName())
                .addValue("surname", student.getSurname());

        template.update("INSERT INTO students(name, surname) VALUES(:name, :surname)",
                params,
                keyHolder,
                new String[]{"id"});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Student student) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", student.getId())
                .addValue("name", student.getName())
                .addValue("surname", student.getSurname());
        template.update("UPDATE students SET name=:name, surname=:surname WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        template.update("DELETE FROM students WHERE id=:id", params);
    }
}
