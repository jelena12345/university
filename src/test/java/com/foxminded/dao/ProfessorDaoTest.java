package com.foxminded.dao;

import com.foxminded.dto.Professor;
import com.foxminded.dto.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProfessorDaoTest {

    private EmbeddedDatabase db;
    private ProfessorDao dao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        JdbcTemplate template = new JdbcTemplate(db);
        dao = new ProfessorDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldReturnAllProfessors() {
        List<Professor> expected = Arrays.asList(new Professor(1, "name", "surname", "q"),
                new Professor(2,"name2", "surname2", "q2"));
        dao.add(expected.get(0));
        dao.add(expected.get(1));
        List<Professor> actual = dao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectProfessor() {
        Professor expected = new Professor(1, "name", "surname", "q");
        dao.add(expected);
        Professor actual = dao.findById(1);
        assertEquals(expected, actual);
    }


    @Test
    void testAdd_ShouldInsertProfessor() {
        Professor expected = new Professor(1, "name", "surname", "q");
        int id = dao.add(expected);
        Professor actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Professor expected = new Professor(1,"name", "surname", "q");
        dao.add(expected);
        expected.setName("name_new");
        expected.setSurname("surname_new");
        expected.setQualification("q_new");
        dao.update(expected);
        Professor actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        Professor professor = new Professor(1,"name", "surname", "q");
        dao.add(professor);
        dao.deleteById(1);
        Professor actual = dao.findById(1);
        assertNull(actual);
    }
}