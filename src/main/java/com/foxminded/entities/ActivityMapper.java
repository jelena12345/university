package com.foxminded.entities;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityMapper implements RowMapper<Activity> {

    public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(
                rs.getInt("user_id"),
                rs.getString("personal_id"),
                rs.getString("role"),
                rs.getString("users.name"),
                rs.getString("surname"),
                rs.getString("about")
        );
        Course course = new Course(
                rs.getInt("course_id"),
                rs.getString("courses.name"),
                rs.getString("description")
        );
        return new Activity(
                rs.getInt("id"),
                user,
                course,
                rs.getTimestamp("start_time"),
                rs.getTimestamp("end_time"));
    }

}
