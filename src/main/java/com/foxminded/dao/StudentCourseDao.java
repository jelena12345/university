package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentCourseDao {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public StudentCourseDao(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public List<Student> findAllStudentsForCourse(Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", course.getId());
        return template.query("SELECT students.id, students.name, students.surname " +
                "FROM student_course " +
                "INNER JOIN students ON student_course.student_id=students.id " +
                "WHERE student_course.course_id=:id " +
                "ORDER BY student_course.student_id;",
                params,
                new BeanPropertyRowMapper<>(Student.class));
    }

    public void add(Student student, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("course_id", course.getId())
                .addValue("student_id", student.getId());
        template.update("INSERT INTO student_course(student_id, course_id) VALUES(:student_id, :course_id);",
                params);
    }

    public void delete(Student student, Course course) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("course_id", course.getId())
                .addValue("student_id", student.getId());
        template.update("DELETE FROM student_course WHERE student_id=:student_id AND course_id=:course_id;", params);
    }
}
