package com.foxminded.dao;

import com.foxminded.config.TestConfig;
import com.foxminded.entities.Activity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:data.sql")
class ActivityDaoTest {

    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Test
    void testFindAll_ShouldFindAllActivities() {
        List<Activity> actual = activityDao.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testFindById_ShouldFindActivity() {
        assertNotNull(activityDao.findById(1));
    }

    @Test
    void testFindById_ShouldReturnNull() {
        assertNull(activityDao.findById(0));
    }

    @Test
    void testAdd_ShouldAddCorrectActivity() {
        Activity expected = new Activity(userDao.findById(1),
                courseDao.findById(1),
                LocalDateTime.parse("2021-02-15T16:31"),
                LocalDateTime.parse("2021-02-15T17:31"));
        int id = activityDao.add(expected);
        expected.setId(id);
        Activity actual = activityDao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Activity expected = activityDao.findById(1);
        expected.setUser(userDao.findById(1));
        expected.setCourse(courseDao.findById(1));
        expected.setFrom(LocalDateTime.parse("2021-02-15T16:31"));
        expected.setTo(LocalDateTime.parse("2021-02-15T17:31"));
        activityDao.update(expected);
        Activity actual = activityDao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldDeleteSuccessfully() {
        activityDao.deleteById(1);
        assertFalse(activityDao.existsById(1));
    }

    @Test
    void testExistsById_ShouldReturnFalse() {
        assertFalse(activityDao.existsById(0));
    }

    @Test
    void testExistsById_ShouldReturnTrue() {
        assertTrue(activityDao.existsById(1));
    }

}
