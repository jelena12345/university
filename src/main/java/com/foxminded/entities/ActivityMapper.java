package com.foxminded.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityMapper implements RowMapper<Activity> {

    public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Professor professor = new Professor(
                rs.getInt("professor_id"),
                rs.getString("personal_id"),
                rs.getString("professors.name"),
                rs.getString("surname"),
                rs.getString("qualification")
        );
        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("courses.name"),
                rs.getString("description")
        );
        return new Activity(
                rs.getInt("id"),
                professor,
                course,
                rs.getTimestamp("start_time"),
                rs.getTimestamp("end_time"));
    }

}
