package com.foxminded.dao;

import com.foxminded.entities.User;
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
public class UserDao {

    private static final String ID = "id";
    private static final String PERSONAL_ID = "personal_id";
    private static final String ROLE = "role";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String ABOUT = "about";

    private final NamedParameterJdbcTemplate template;
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    public UserDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<User> findAll() {
        return template.query("SELECT id, personal_id, role, name, surname, about FROM users",
                new BeanPropertyRowMapper<>(User.class));
    }

    public User findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        try {
            return template.queryForObject("SELECT id, personal_id, role, name, surname, about " +
                            "FROM users WHERE id=:id",
                    params,
                    new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error trying to find User with id = {}", id, e);
            return null;
        }
    }

    public User findByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(PERSONAL_ID, personalId);
        try {
            return template.queryForObject("SELECT id, personal_id, role, name, surname, about " +
                            "FROM users WHERE personal_id=:personal_id",
                    params,
                    new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error trying to find User with personalId = {}", personalId, e);
            return null;
        }
    }

    public int add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, user.getPersonalId())
                .addValue(ROLE, user.getRole())
                .addValue(NAME, user.getName())
                .addValue(SURNAME, user.getSurname())
                .addValue(ABOUT, user.getAbout());
        template.update("INSERT INTO users(personal_id, role, name, surname, about) " +
                        "VALUES(:personal_id, :role, :name, :surname, :about)",
                params,
                keyHolder,
                new String[]{ID});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, user.getPersonalId())
                .addValue(ROLE, user.getRole())
                .addValue(NAME, user.getName())
                .addValue(SURNAME, user.getSurname())
                .addValue(ABOUT, user.getAbout());
        template.update("UPDATE users " +
                "SET name=:name, surname=:surname, about=:about " +
                "WHERE personal_id=:personal_id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(ID, id);
        template.update("DELETE FROM users WHERE id=:id", params);
    }

    public void deleteByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue(PERSONAL_ID, personalId);
        template.update("DELETE FROM users WHERE personal_id=:personal_id", params);
    }

    public boolean existsById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(ID, id);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM users WHERE id=:id)",
                        params, Boolean.class));
    }

    public boolean existsByPersonalId(String personalId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(PERSONAL_ID, personalId);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM users WHERE personal_id=:personal_id)",
                        params, Boolean.class));
    }

}
