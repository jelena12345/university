package com.foxminded.dao;

import com.foxminded.config.TestConfig;
import com.foxminded.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:data.sql")
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
        assertNotNull(dao.findById(1));
    }

    @Test
    void testFindById_ShouldReturnNull() {
        assertNull(dao.findById(0));
    }

    @Test
    void testFindByPersonalId_ShouldFindRecord() {
        assertNotNull(dao.findByPersonalId("1"));
    }

    @Test
    void testFindByPersonalId_ShouldReturnNull() {
        assertNull(dao.findByPersonalId(""));
    }

    @Test
    void testAdd_ShouldAddCorrectRecord() {
        User expected = new User("3", "role", "name", "surname", "about");
        int id = dao.add(expected);
        expected.setId(id);
        User actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        User expected = dao.findById(1);
        expected.setName("name_new");
        expected.setSurname("surname_new");
        expected.setAbout("about_new");
        dao.update(expected);
        User actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        dao.deleteById(1);
        assertFalse(dao.existsById(1));
    }

    @Test
    void testDeleteByPersonalId_ShouldFindNull() {
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
    void testExistsByName_ShouldReturnFalse() {
        assertFalse(dao.existsByPersonalId(""));
    }

    @Test
    void testExistsByName_ShouldReturnTrue() {
        assertTrue(dao.existsByPersonalId("1"));
    }
}
