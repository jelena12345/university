package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class UserCourseDao {

    private final NamedParameterJdbcTemplate template;
    private static final String COURSE_ID = "course_id";
    private static final String USER_ID = "user_id";

    @Autowired
    public UserCourseDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<User> findUsersForCourse(Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", course.getId());
        return template.query("SELECT users.id, personal_id, role, name, surname, about " +
                        "FROM user_course " +
                        "INNER JOIN users ON user_course.user_id=users.id " +
                        "WHERE user_course.course_id=:id " +
                        "ORDER BY user_course.user_id;",
                params,
                new BeanPropertyRowMapper<>(User.class));
    }

    public List<Course> findCoursesForUser(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", user.getId());
        return template.query("SELECT courses.id, courses.name, courses.description " +
                        "FROM user_course " +
                        "INNER JOIN courses ON user_course.course_id=courses.id " +
                        "WHERE user_course.user_id=:id " +
                        "ORDER BY user_course.course_id;",
                params,
                new BeanPropertyRowMapper<>(Course.class));
    }

    public void add(User student, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COURSE_ID, course.getId())
                .addValue(USER_ID, student.getId());
        template.update("INSERT INTO user_course(user_id, course_id) VALUES(:user_id, :course_id);",
                params);
    }

    public void delete(User user, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COURSE_ID, course.getId())
                .addValue(USER_ID, user.getId());
        template.update("DELETE FROM user_course WHERE user_id=:user_id AND course_id=:course_id;", params);
    }

    public boolean existsCourseForUser(User user, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COURSE_ID, course.getId())
                .addValue(USER_ID, user.getId());
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM user_course " +
                        "WHERE user_id=:user_id AND course_id=:course_id)", params, Boolean.class));
    }
}
