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
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new ProfessorDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldFindAllProfessors() {
        List<Professor> expected = Arrays.asList(new Professor(1, "1", "name", "surname", "q"),
                new Professor(2, "2", "name2", "surname2", "q2"));
        dao.add(expected.get(0));
        dao.add(expected.get(1));
        List<Professor> actual = dao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectProfessor() {
        Professor expected = new Professor(1, "1", "name", "surname", "q");
        dao.add(expected);
        Professor actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testFindByPersonalId_ShouldFindCorrectProfessor() {
        Professor expected = new Professor(1, "1", "name", "surname", "q");
        dao.add(expected);
        Professor actual = dao.findByPersonalId("1");
        assertEquals(expected, actual);
    }


    @Test
    void testAdd_ShouldAddCorrectProfessor() {
        Professor expected = new Professor(1, "1", "name", "surname", "q");
        int id = dao.add(expected);
        Professor actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Professor expected = new Professor(1,"1", "name", "surname", "q");
        dao.add(expected);
        expected.setPersonalId("2");
        expected.setName("name_new");
        expected.setSurname("surname_new");
        expected.setQualification("q_new");
        dao.update(1, expected);
        Professor actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        Professor professor = new Professor(1,"1", "name", "surname", "q");
        dao.add(professor);
        dao.deleteById(1);
        assertFalse(dao.existsById(1));
    }

    @Test
    void testDeleteByPersonalId_ShouldFindNull() {
        Professor professor = new Professor(1,"1", "name", "surname", "q");
        dao.add(professor);
        dao.deleteByPersonalId("1");
        assertFalse(dao.existsByPersonalId("1"));
    }

    @Test
    void testExistsById_ShouldReturnFalse() {
        assertFalse(dao.existsById(1));
    }

    @Test
    void testExistsById_ShouldReturnTrue() {
        Professor professor = new Professor(1,"1", "name", "surname", "q");
        dao.add(professor);
        assertTrue(dao.existsById(1));
    }

    @Test
    void testExistsByName_ShouldReturnFalse() {
        assertFalse(dao.existsByPersonalId("1"));
    }

    @Test
    void testExistsByName_ShouldReturnTrue() {
        Professor professor = new Professor(1,"1", "name", "surname", "q");
        dao.add(professor);
        assertTrue(dao.existsByPersonalId("1"));
    }
}
