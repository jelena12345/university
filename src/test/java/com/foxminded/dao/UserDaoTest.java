package com.foxminded.dao;

import com.foxminded.entities.User;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Test
    void testFindAll_ShouldFindAllRecords() {
        List<User> actual = dao.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testFindById_ShouldFindRecord() {
        assertTrue(dao.findById(1).isPresent());
    }

    @Test
    void testFindById_ShouldFindNothing() {
        assertFalse(dao.findById(0).isPresent());
    }

    @Test
    void testFindByPersonalId_ShouldFindRecord() {
        assertTrue(dao.findByPersonalId("1").isPresent());
    }

    @Test
    void testFindByPersonalId_ShouldFindNothing() {
        assertFalse(dao.findByPersonalId("").isPresent());
    }

    @Test
    void testSave_ShouldSaveCorrectRecord() {
        dao.save(new User("3", "role", "name", "surname", "about"));
        assertTrue(dao.findByPersonalId("3").isPresent());
    }

    @Test
    void testSave_ShouldSaveUpdatedValues() {
        User expected = dao.findById(1).orElseThrow(EntityNotFoundException::new);
        expected.setName("name_new");
        expected.setSurname("surname_new");
        expected.setAbout("about_new");
        User actual = dao.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNothing() {
        dao.deleteById(1);
        assertFalse(dao.existsById(1));
    }

    @Test
    void testDeleteByPersonalId_ShouldFindNothing() {
        dao.deleteByPersonalId("1");
        assertFalse(dao.existsByPersonalId("1"));
    }

    @Test
    void testExistsById_ShouldReturnFalse() {
        assertFalse(dao.existsById(0));
    }

    @Test
    void testExistsById_ShouldReturnTrue() {
        assertTrue(dao.existsById(1));
    }

    @Test
    void testExistsByPersonalId_ShouldReturnFalse() {
        assertFalse(dao.existsByPersonalId(""));
    }

    @Test
    void testExistsByPersonalId_ShouldReturnTrue() {
        assertTrue(dao.existsByPersonalId("1"));
    }
}
