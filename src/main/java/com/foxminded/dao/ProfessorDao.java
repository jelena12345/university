package com.foxminded.dao;

import com.foxminded.dto.Professor;
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
public class ProfessorDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProfessorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Professor> findAll() {
        return jdbcTemplate.query("SELECT id, name, surname, qualification FROM professors",
                new BeanPropertyRowMapper<>(Professor.class));
    }

    public Professor findById(int id) {
        return jdbcTemplate.query("SELECT id, name, surname, qualification FROM professors WHERE id=?",
                new BeanPropertyRowMapper<>(Professor.class), id).stream().findAny().orElse(null);
    }

    public int add(Professor professor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO professors(name, surname, qualification) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, professor.getName());
            ps.setString(2, professor.getSurname());
            ps.setString(3, professor.getQualification());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Professor professor) {
        jdbcTemplate.update("UPDATE professors SET name=?, surname=?, qualification=? WHERE id=?",
                professor.getName(), professor.getSurname(), professor.getQualification(), professor.getId());
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM professors WHERE id=?", id);
    }

}
