package com.foxminded.dao;

import com.foxminded.entities.Professor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProfessorDao {

    private static final String ID = "id";
    private static final String PERSONAL_ID = "personal_id";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String QUALIFICATION = "qualification";

    private final NamedParameterJdbcTemplate template;
    private static final Logger logger = LoggerFactory.getLogger(ProfessorDao.class);

    @Autowired
    public ProfessorDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Professor> findAll() {
        return template.query("SELECT id, personal_id, name, surname, qualification FROM professors",
                new BeanPropertyRowMapper<>(Professor.class));
    }

    public Professor findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        try {
            return template.queryForObject("SELECT id, personal_id, name, surname, qualification " +
                            "FROM professors WHERE id=:id",
                    params,
                    new BeanPropertyRowMapper<>(Professor.class));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error trying to find Professor with id = {}", id, e);
            return null;
        }
    }

    public Professor findByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(PERSONAL_ID, personalId);
        try {
            return template.queryForObject("SELECT id, personal_id, name, surname, qualification " +
                            "FROM professors WHERE personal_id=:personal_id",
                    params,
                    new BeanPropertyRowMapper<>(Professor.class));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error trying to find Professor with personalId = {}", personalId, e);
            return null;
        }
    }

    public int add(Professor professor) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, professor.getPersonalId())
                .addValue(NAME, professor.getName())
                .addValue(SURNAME, professor.getSurname())
                .addValue(QUALIFICATION, professor.getQualification());
        template.update("INSERT INTO professors(personal_id, name, surname, qualification) " +
                        "VALUES(:personal_id, :name, :surname, :qualification)",
                params,
                keyHolder,
                new String[]{ID});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Professor professor) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, professor.getPersonalId())
                .addValue(NAME, professor.getName())
                .addValue(SURNAME, professor.getSurname())
                .addValue(QUALIFICATION, professor.getQualification());
        template.update("UPDATE professors " +
                "SET name=:name, surname=:surname, qualification=:qualification " +
                "WHERE personal_id=:personal_id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        template.update("DELETE FROM professors WHERE id=:id", params);
    }

    public void deleteByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(PERSONAL_ID, personalId);
        template.update("DELETE FROM professors WHERE personal_id=:personal_id", params);
    }

    public boolean existsById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ID, id);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM professors WHERE id=:id)", params, Boolean.class));
    }

    public boolean existsByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, personalId);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM professors WHERE personal_id=:personal_id)", params, Boolean.class));
    }

}
