package com.foxminded.dao;

import com.foxminded.entities.Activity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActivityDaoTest {

    private EmbeddedDatabase db;
    private ActivityDao activityDao;
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
        activityDao = new ActivityDao(template);
        courseDao = new CourseDao(template);
        userDao = new UserDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

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
        Activity expected = new Activity(3,
                userDao.findById(1),
                courseDao.findById(1),
                LocalDateTime.parse("2021-02-15T16:31"),
                LocalDateTime.parse("2021-02-15T17:31"));
        int id = activityDao.add(expected);
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
