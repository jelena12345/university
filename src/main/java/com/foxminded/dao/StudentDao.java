package com.foxminded.dao;

import com.foxminded.entities.Student;
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
public class StudentDao {

    private static final String ID = "id";
    private static final String PERSONAL_ID = "personal_id";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public StudentDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Student> findAll() {
        return template.query("SELECT id, personal_id, name, surname FROM students",
                new BeanPropertyRowMapper<>(Student.class));
    }

    public Student findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        try {
            return template.queryForObject("SELECT id, personal_id, name, surname FROM students WHERE id=:id",
                    params,
                    new BeanPropertyRowMapper<>(Student.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student findByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(PERSONAL_ID, personalId);
        try {
            return template.queryForObject("SELECT id, personal_id, name, surname FROM students WHERE personal_id=:personal_id",
                    params,
                    new BeanPropertyRowMapper<>(Student.class));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int add(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, student.getPersonalId())
                .addValue(NAME, student.getName())
                .addValue(SURNAME, student.getSurname());
        template.update("INSERT INTO students(personal_id, name, surname) VALUES(:personal_id, :name, :surname)",
                params,
                keyHolder,
                new String[]{ID});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(int id, Student student) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ID, id)
                .addValue(PERSONAL_ID, student.getPersonalId())
                .addValue(NAME, student.getName())
                .addValue(SURNAME, student.getSurname());
        template.update("UPDATE students SET personal_id=:personal_id, name=:name, surname=:surname WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        template.update("DELETE FROM students WHERE id=:id", params);
    }

    public void deleteByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(PERSONAL_ID, personalId);
        template.update("DELETE FROM students WHERE personal_id=:personal_id", params);
    }

    public boolean existsById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ID, id);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM students WHERE id=:id)", params, Boolean.class));
    }

    public boolean existsByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, personalId);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM students WHERE personal_id=:personal_id)", params, Boolean.class));
    }
}
