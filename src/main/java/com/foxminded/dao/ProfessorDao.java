package com.foxminded.dao;

import com.foxminded.dto.Professor;
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
public class ProfessorDao {
    private final NamedParameterJdbcTemplate template;

    @Autowired
    public ProfessorDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Professor> findAll() {
        return template.query("SELECT id, name, surname, qualification FROM professors",
                new BeanPropertyRowMapper<>(Professor.class));
    }

    public Professor findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return template.queryForObject("SELECT id, name, surname, qualification FROM professors WHERE id=:id",
                params,
                new BeanPropertyRowMapper<>(Professor.class));
    }

    public int add(Professor professor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", professor.getName())
                .addValue("surname", professor.getSurname())
                .addValue("qualification", professor.getQualification());
        template.update("INSERT INTO professors(name, surname, qualification) VALUES(:name, :surname, :qualification)",
                params,
                keyHolder,
                new String[]{"id"});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Professor professor) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", professor.getId())
                .addValue("name", professor.getName())
                .addValue("surname", professor.getSurname())
                .addValue("qualification", professor.getQualification());
        template.update("UPDATE professors SET name=:name, surname=:surname, qualification=:qualification " +
                "WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        template.update("DELETE FROM professors WHERE id=:id", params);
    }

}
