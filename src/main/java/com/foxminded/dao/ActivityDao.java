package com.foxminded.dao;

import com.foxminded.dto.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ActivityDao {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public ActivityDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Activity> findAll() {
        return template.query("SELECT id, professor_id, course_id, start_time, end_time FROM activities",
                new BeanPropertyRowMapper<>(Activity.class));
    }

    public Activity findById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return template.query("SELECT id, professor_id, course_id, start_time, end_time FROM activities WHERE id=:id",
                params,
                new BeanPropertyRowMapper<>(Activity.class))
                .stream()
                .findAny()
                .orElse(null);
    }

    public Integer add(Activity activity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("professor_id", activity.getProfessorId())
                .addValue("course_id", activity.getCourseId())
                .addValue("start_time", activity.getStartTime())
                .addValue("end_time", activity.getEndTime());

        template.update("INSERT INTO activities(professor_id, course_id, start_time, end_time) " +
                        "VALUES(:professor_id, :course_id, :start_time, :end_time)",
                params,
                keyHolder,
                new String[]{"id"});

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public void update(Activity activity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", activity.getId())
                .addValue("professor_id", activity.getProfessorId())
                .addValue("course_id", activity.getCourseId())
                .addValue("start_time", activity.getStartTime())
                .addValue("end_time", activity.getEndTime());
        template.update("UPDATE activities SET professor_id=:professor_id, course_id=:course_id, " +
                "start_time=:start_time, end_time=:end_time WHERE id=:id", params);
    }

    public void deleteById(int id) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        template.update("DELETE FROM activities WHERE id=:id", params);
    }
}
