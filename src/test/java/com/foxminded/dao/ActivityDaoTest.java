package com.foxminded.dao;

import com.foxminded.entities.Activity;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ActivityDaoTest {

    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Test
    void testFindAll_ShouldFindAllRecords() {
        List<Activity> actual = activityDao.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testFindById_ShouldFindRecord() {
        assertTrue(activityDao.findById(1).isPresent());
    }

    @Test
    void testFindById_ShouldFindNothing() {
        assertFalse(activityDao.findById(0).isPresent());
    }

    @Test
    void testSave_ShouldSaveCorrectRecord() {
        Activity expected = new Activity(
                userDao.findById(1).orElseThrow(EntityNotFoundException::new),
                courseDao.findById(1).orElseThrow(EntityNotFoundException::new),
                LocalDateTime.parse("2021-02-15T16:31"),
                LocalDateTime.parse("2021-02-15T17:31"));
        expected = activityDao.save(expected);
        assertTrue(activityDao.findById(expected.getId()).isPresent());
    }

    @Test
    void testSave_ShouldSaveUpdatedValues() {
        Activity expected = activityDao.findById(1).orElseThrow(EntityNotFoundException::new);
        expected.setUser(userDao.findById(1).orElseThrow(EntityNotFoundException::new));
        expected.setCourse(courseDao.findById(1).orElseThrow(EntityNotFoundException::new));
        expected.setFrom(LocalDateTime.parse("2021-02-15T16:31"));
        expected.setTo(LocalDateTime.parse("2021-02-15T17:31"));
        Activity actual = activityDao.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNothing() {
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
