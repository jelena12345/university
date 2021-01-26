package com.foxminded.dao;

import com.foxminded.entities.Activity;
import com.foxminded.entities.ActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ActivityDao {

    private final NamedParameterJdbcTemplate template;
    private static final Logger logger = LoggerFactory.getLogger(ActivityDao.class);

    @Autowired
    public ActivityDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Activity> findAll() {
        return template.query("SELECT activities.id, " +
                        "user_id, personal_id, role, users.name, surname, about, " +
                        "course_id, courses.name, description, " +
                        "start_time, end_time " +
                        "FROM activities " +
                        "INNER JOIN users ON user_id=users.id " +
                        "INNER JOIN courses ON course_id=courses.id",
                new ActivityMapper());
    }

    public Activity findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        try {
            return template.queryForObject("SELECT activities.id, " +
                    "user_id, personal_id, role, users.name, surname, about, " +
                    "course_id, courses.name, description, " +
                    "start_time, end_time " +
                    "FROM activities " +
                    "INNER JOIN users ON user_id=users.id " +
                    "INNER JOIN courses ON course_id=courses.id WHERE activities.id=:id",
                    params,
                    new ActivityMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error trying to find Activity with id = {}", id, e);
            return null;
        }
    }

    public Integer add(Activity activity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", activity.getUser().getId())
                .addValue("course_id", activity.getCourse().getId())
                .addValue("start_time", activity.getStartTime())
                .addValue("end_time", activity.getEndTime());

        template.update("INSERT INTO activities(user_id, course_id, start_time, end_time) " +
                        "VALUES(:user_id, :course_id, :start_time, :end_time)",
                params,
                keyHolder,
                new String[]{"id"});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Activity activity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", activity.getId())
                .addValue("user_id", activity.getUser().getId())
                .addValue("course_id", activity.getCourse().getId())
                .addValue("start_time", activity.getStartTime())
                .addValue("end_time", activity.getEndTime());
        template.update("UPDATE activities SET user_id=:user_id, course_id=:course_id, " +
                "start_time=:start_time, end_time=:end_time WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        template.update("DELETE FROM activities WHERE id=:id", params);
    }

    public boolean existsById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM activities WHERE id=:id)", params, Boolean.class));
    }
}
