package com.foxminded.dao;

import com.foxminded.dto.Student;
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

class StudentDaoTest {

    private EmbeddedDatabase db;
    private StudentDao dao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new StudentDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldReturnAllStudents() {
        List<Student> expected = Arrays.asList(new Student(1, "name", "surname"),
                new Student(2,"name2", "surname2"));
        dao.add(expected.get(0));
        dao.add(expected.get(1));
        List<Student> actual = dao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectStudent() {
        Student expected = new Student(1, "name", "surname");
        dao.add(expected);
        Student actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldInsertStudent() {
        Student expected = new Student(1, "name", "surname");
        int id = dao.add(expected);
        Student actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Student expected = new Student(1,"name", "surname");
        dao.add(expected);
        expected.setName("name_new");
        expected.setSurname("surname_new");
        dao.update(expected);
        Student actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        Student student = new Student(1,"name", "surname");
        dao.add(student);
        dao.deleteById(1);
        Student actual = dao.findById(1);
        assertNull(actual);
    }

}
