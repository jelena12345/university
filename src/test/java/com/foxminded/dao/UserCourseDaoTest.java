package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.*;

class UserCourseDaoTest {

    private EmbeddedDatabase db;
    private UserCourseDao dao;
    private CourseDao courseDao;
    private UserDao userDao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new UserCourseDao(template);
        courseDao = new CourseDao(template);
        userDao = new UserDao();
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindUsersForCourse_ShouldFindCorrectRecords() {
        Course course = courseDao.findById(1);
        assertNotNull(dao.findUsersForCourse(course));
    }

    @Test
    void testFindCoursesForUser_ShouldFindCorrectRecords() {
        User user = userDao.findById(1);
        assertNotNull(dao.findCoursesForUser(user));
    }

    @Test
    void testAdd_ShouldAddCorrectUserForCourse() {
        User user = userDao.findById(2);
        Course course = courseDao.findById(1);
        dao.add(user, course);
        assertTrue(dao.existsCourseForUser(user, course));
    }

    @Test
    void testDelete_ShouldDeleteCorrectUserForCourse() {
        User user = userDao.findById(1);
        Course course = courseDao.findById(1);
        dao.delete(user, course);
        assertFalse(dao.existsCourseForUser(user, course));
    }

    @Test
    void testExistsCourseForUser_ShouldReturnTrue() {
        User user = userDao.findById(1);
        Course course = courseDao.findById(1);
        assertTrue(dao.existsCourseForUser(user, course));
    }

    @Test
    void testExistsCourseForUser_ShouldReturnFalse() {
        User user = userDao.findById(2);
        Course course = courseDao.findById(1);
        assertFalse(dao.existsCourseForUser(user, course));
    }

}
