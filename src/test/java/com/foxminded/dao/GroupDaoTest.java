package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.*;

class GroupDaoTest {

    private EmbeddedDatabase db;
    private GroupDao dao;
    private CourseDao courseDao;
    private StudentDao studentDao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
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
        Course course = courseDao.findById(1);
        assertNotNull(dao.findGroupForCourse(course));
    }

    @Test
    void testAdd_ShouldAddCorrectStudentForCourse() {
        Student student = studentDao.findById(2);
        Course course = courseDao.findById(1);
        dao.add(student, course);
        assertTrue(dao.existsCourseForStudent(student, course));
    }

    @Test
    void testDelete_ShouldDeleteCorrectStudentForCourse() {
        Student student = studentDao.findById(1);
        Course course = courseDao.findById(1);
        dao.delete(student, course);
        assertFalse(dao.existsCourseForStudent(student, course));
    }

    @Test
    void testExistsCourseForStudent_ShouldReturnTrue() {
        Student student = studentDao.findById(1);
        Course course = courseDao.findById(1);
        assertTrue(dao.existsCourseForStudent(student, course));
    }

    @Test
    void testExistsCourseForStudent_ShouldReturnFalse() {
        Student student = studentDao.findById(2);
        Course course = courseDao.findById(1);
        assertFalse(dao.existsCourseForStudent(student, course));
    }

}
