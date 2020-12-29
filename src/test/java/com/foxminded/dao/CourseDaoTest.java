package com.foxminded.dao;

import com.foxminded.dto.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest {

    private EmbeddedDatabase db;
    private CourseDao dao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        JdbcTemplate template = new JdbcTemplate(db);
        dao = new CourseDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldReturnAllCourses() {
        List<Course> expected = Arrays.asList(new Course(1, "name", "description"),
                new Course(2,"name2", "description2"));
        dao.add(expected.get(0));
        dao.add(expected.get(1));
        List<Course> actual = dao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectCourse() {
        Course expected = new Course(1, "name", "description");
        dao.add(expected);
        Course actual = dao.findById(1);
        assertEquals(expected, actual);
    }


    @Test
    void testAdd_ShouldInsertCourse() {
        Course expected = new Course(1, "name", "description");
        int id = dao.add(expected);
        Course actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Course expected = new Course(1,"name", "description");
        dao.add(expected);
        expected.setName("name_new");
        expected.setDescription("description_new");
        dao.update(expected);
        Course actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        Course course = new Course(1,"name", "description");
        dao.add(course);
        dao.deleteById(1);
        Course actual = dao.findById(1);
        assertNull(actual);
    }


}
