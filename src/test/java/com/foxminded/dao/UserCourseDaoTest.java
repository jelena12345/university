package com.foxminded.dao;

import com.foxminded.config.TestConfig;
import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:data.sql")
class UserCourseDaoTest {

    @Autowired
    private UserCourseDao dao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

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
