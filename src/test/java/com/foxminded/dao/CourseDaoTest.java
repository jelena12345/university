package com.foxminded.dao;

import com.foxminded.config.TestConfig;
import com.foxminded.entities.Course;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class })
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:data.sql")
class CourseDaoTest {

    @Autowired
    private CourseDao dao;

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
        Course expected = new Course("name3", "description3");
        int id = dao.add(expected);
        expected.setId(id);
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
