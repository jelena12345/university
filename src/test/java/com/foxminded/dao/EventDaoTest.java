package com.foxminded.dao;

import com.foxminded.entities.Event;
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
class EventDaoTest {

    @Autowired
    private EventDao eventDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Test
    void testFindAll_ShouldFindAllRecords() {
        List<Event> actual = eventDao.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testFindById_ShouldFindRecord() {
        assertTrue(eventDao.findById(1).isPresent());
    }

    @Test
    void testFindById_ShouldFindNothing() {
        assertFalse(eventDao.findById(0).isPresent());
    }

    @Test
    void testSave_ShouldSaveCorrectRecord() {
        Event expected = new Event(
                userDao.findById(1).orElseThrow(EntityNotFoundException::new),
                courseDao.findById(1).orElseThrow(EntityNotFoundException::new),
                LocalDateTime.parse("2021-02-15T16:31"),
                LocalDateTime.parse("2021-02-15T17:31"));
        expected = eventDao.save(expected);
        assertTrue(eventDao.findById(expected.getId()).isPresent());
    }

    @Test
    void testSave_ShouldSaveUpdatedValues() {
        Event expected = eventDao.findById(1).orElseThrow(EntityNotFoundException::new);
        expected.setUser(userDao.findById(1).orElseThrow(EntityNotFoundException::new));
        expected.setCourse(courseDao.findById(1).orElseThrow(EntityNotFoundException::new));
        expected.setFrom(LocalDateTime.parse("2021-02-15T16:31"));
        expected.setTo(LocalDateTime.parse("2021-02-15T17:31"));
        Event actual = eventDao.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNothing() {
        eventDao.deleteById(1);
        assertFalse(eventDao.existsById(1));
    }

    @Test
    void testExistsById_ShouldReturnFalse() {
        assertFalse(eventDao.existsById(0));
    }

    @Test
    void testExistsById_ShouldReturnTrue() {
        assertTrue(eventDao.existsById(1));
    }

}
