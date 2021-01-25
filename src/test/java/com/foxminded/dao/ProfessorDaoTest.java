package com.foxminded.dao;

import com.foxminded.entities.Professor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfessorDaoTest {

    private EmbeddedDatabase db;
    private ProfessorDao dao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new ProfessorDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldFindAllProfessors() {
        List<Professor> actual = dao.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testFindById_ShouldFindProfessor() {
        assertNotNull(dao.findById(1));
    }

    @Test
    void testFindById_ShouldReturnNull() {
        assertNull(dao.findById(0));
    }

    @Test
    void testFindByPersonalId_ShouldFindProfessor() {
        assertNotNull(dao.findByPersonalId("1"));
    }

    @Test
    void testFindByPersonalId_ShouldReturnNull() {
        assertNull(dao.findByPersonalId(""));
    }

    @Test
    void testAdd_ShouldAddCorrectProfessor() {
        Professor expected = new Professor(3, "3", "name", "surname", "q");
        int id = dao.add(expected);
        Professor actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Professor expected = dao.findById(1);
        expected.setName("name_new");
        expected.setSurname("surname_new");
        expected.setQualification("q_new");
        dao.update(expected);
        Professor actual = dao.findById(1);
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
