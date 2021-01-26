package com.foxminded.dao;

import com.foxminded.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoTest {

    private EmbeddedDatabase db;
    private UserDao dao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new UserDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

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
        User expected = new User(3, "3", "role", "name", "surname", "about");
        int id = dao.add(expected);
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
