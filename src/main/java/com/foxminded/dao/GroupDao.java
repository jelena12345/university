package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
import com.foxminded.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class GroupDao {

    private final NamedParameterJdbcTemplate template;
    private static final String COURSE_ID = "course_id";
    private static final String STUDENT_ID = "student_id";

    @Autowired
    public GroupDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Group findGroupForCourse(Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", course.getId());
        List<Student> students = template.query("SELECT students.id, students.name, students.surname " +
                        "FROM student_course " +
                        "INNER JOIN students ON student_course.student_id=students.id " +
                        "WHERE student_course.course_id=:id " +
                        "ORDER BY student_course.student_id;",
                params,
                new BeanPropertyRowMapper<>(Student.class));
        return new Group(course, students);
    }

    public void add(Student student, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COURSE_ID, course.getId())
                .addValue(STUDENT_ID, student.getId());
        template.update("INSERT INTO student_course(student_id, course_id) VALUES(:student_id, :course_id);",
                params);
    }

    public void delete(Student student, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COURSE_ID, course.getId())
                .addValue(STUDENT_ID, student.getId());
        template.update("DELETE FROM student_course WHERE student_id=:student_id AND course_id=:course_id;", params);
    }

    public boolean existsCourseForStudent(Student student, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(COURSE_ID, course.getId())
                .addValue(STUDENT_ID, student.getId());
        return Objects.requireNonNull(
                template.queryForObject("SELECT EXISTS(SELECT * FROM student_course " +
                        "WHERE student_id=:student_id AND course_id=:course_id)", params, Boolean.class));
    }
}
