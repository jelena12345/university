package com.foxminded.dao;

import com.foxminded.dto.Course;
import com.foxminded.dto.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StudentCourseDaoTest {

    private EmbeddedDatabase db;
    private StudentCourseDao dao;
    private CourseDao courseDao;
    private StudentDao studentDao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new StudentCourseDao(template);
        courseDao = new CourseDao(template);
        studentDao = new StudentDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAllStudentsForCourse_ShouldFindAllStudentsForCourse() {
        List<Student> expected = Arrays.asList(new Student(1, "name", "surname"),
                new Student(2, "name2", "surname2"));
        studentDao.add(expected.get(0));
        studentDao.add(expected.get(1));
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        dao.add(expected.get(0), course);
        dao.add(expected.get(1), course);
        List<Student> actual = dao.findAllStudentsForCourse(course);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldAddCorrectStudentForCourse() {
        Student expected = new Student(1, "name", "surname");
        studentDao.add(expected);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        dao.add(expected, course);
        Student actual = dao.findAllStudentsForCourse(course).stream().findFirst().orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete_ShouldDeleteCorrectStudentForCourse() {
        Student expected = new Student(1, "name", "surname");
        studentDao.add(expected);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        dao.add(expected, course);
        dao.delete(expected, course);
        Student actual = dao.findAllStudentsForCourse(course).stream().findFirst().orElse(null);
        assertNull(actual);
    }

}
