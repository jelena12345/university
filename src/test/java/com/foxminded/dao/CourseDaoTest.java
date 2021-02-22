package com.foxminded.dao;

import com.foxminded.entities.Course;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CourseDaoTest {

    @Autowired
    private CourseDao dao;

    @Test
    void testFindAll_ShouldFindAllRecords() {
        List<Course> actual = dao.findAll();
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
    void testFindByName_ShouldFindRecord() {
        assertTrue(dao.findByName("name").isPresent());
    }

    @Test
    void testFindByName_ShouldFindNothing() {
        assertFalse(dao.findByName("").isPresent());
    }

    @Test
    void testSave_ShouldSaveCorrectRecord() {
        dao.save(new Course("name3", "description3"));
        assertTrue(dao.findByName("name3").isPresent());
    }

    @Test
    void testSave_ShouldSaveUpdatedValues() {
        Course expected = dao.findById(1).orElseThrow(EntityNotFoundException::new);
        expected.setName("name_new");
        expected.setDescription("description_new");
        Course actual = dao.save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNothing() {
        dao.deleteById(1);
        assertFalse(dao.existsById(1));
    }

    @Test
    void testDeleteByName_ShouldFindNothing() {
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
