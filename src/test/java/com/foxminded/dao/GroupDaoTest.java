package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
import com.foxminded.entities.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoTest {

    private EmbeddedDatabase db;
    private GroupDao dao;
    private CourseDao courseDao;
    private StudentDao studentDao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new GroupDao(template);
        courseDao = new CourseDao(template);
        studentDao = new StudentDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindGroupForCourse_ShouldFindCorrectGroup() {
        List<Student> students = Arrays.asList(new Student(1, "1", "name", "surname"),
                new Student(2, "2", "name2", "surname2"));
        Course course = new Course(1, "name", "description");
        Group expected = new Group(course, students);
        studentDao.add(students.get(0));
        studentDao.add(students.get(1));
        courseDao.add(course);
        dao.add(students.get(0), course);
        dao.add(students.get(1), course);
        Group actual = dao.findGroupForCourse(course);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldAddCorrectStudentForCourse() {
        Student expected = new Student(1, "1", "name", "surname");
        Course course = new Course(1, "name", "description");
        studentDao.add(expected);
        courseDao.add(course);
        dao.add(expected, course);
        Student actual = dao.findGroupForCourse(course).getStudents().stream().findFirst().orElse(null);
        assertEquals(expected, actual);
    }

    @Test
    void testDelete_ShouldDeleteCorrectStudentForCourse() {
        Student student = new Student(1, "1", "name", "surname");
        studentDao.add(student);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        dao.add(student, course);
        dao.delete(student, course);
        assertFalse(dao.existsCourseForStudent(student, course));
    }

    @Test
    void testExistsCourseForStudent_ShouldReturnTrue() {
        Student student = new Student(1, "1", "name", "surname");
        studentDao.add(student);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        dao.add(student, course);
        assertTrue(dao.existsCourseForStudent(student, course));
    }

    @Test
    void testExistsCourseForStudent_ShouldReturnFalse() {
        Student student = new Student(1, "1", "name", "surname");
        Course course = new Course(1, "name", "description");
        assertFalse(dao.existsCourseForStudent(student, course));
    }

}
