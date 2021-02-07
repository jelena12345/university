package com.foxminded.dao;

import com.foxminded.entities.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest {

    private EmbeddedDatabase db;
    private CourseDao dao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new CourseDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldFindAllCourses() {
        List<Course> actual = dao.findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void testFindById_ShouldFindCourse() {
        assertNotNull(dao.findById(1));
    }

    @Test
    void testFindById_ShouldReturnNull() {
        assertNull(dao.findById(0));
    }

    @Test
    void testFindByName_ShouldFindCourse() {
        assertNotNull(dao.findByName("name"));
    }

    @Test
    void testFindByName_ShouldReturnNull() {
        assertNull(dao.findByName(""));
    }

    @Test
    void testAdd_ShouldAddCorrectCourse() {
        Course expected = new Course(3, "name3", "description3");
        int id = dao.add(expected);
        Course actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Course expected = dao.findById(1);
        expected.setName("name_new");
        expected.setDescription("description_new");
        dao.update(expected);
        Course actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldDeleteSuccessfully() {
        dao.deleteById(1);
        assertFalse(dao.existsById(1));
    }

    @Test
    void testDeleteByName_ShouldDeleteSuccessfully() {
        dao.deleteByName("name");
        assertFalse(dao.existsByName("name"));
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
        assertFalse(dao.existsByName(""));
    }

    @Test
    void testExistsByName_ShouldReturnTrue() {
        assertTrue(dao.existsByName("name"));
    }

}
