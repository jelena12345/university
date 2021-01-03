package com.foxminded.dao;

import com.foxminded.dto.Activity;
import com.foxminded.dto.Course;
import com.foxminded.dto.Professor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActivityDaoTest {

    private EmbeddedDatabase db;
    private ActivityDao dao;
    private CourseDao courseDao;
    private ProfessorDao professorDao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        dao = new ActivityDao(template);
        courseDao = new CourseDao(template);
        professorDao = new ProfessorDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldFindAllActivities() {
        Professor professor = new Professor(1,"name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        List<Activity> expected = Arrays.asList(
                new Activity(1, 1, 1, new Timestamp(123), new Timestamp(456)),
                new Activity(2, 1, 1, new Timestamp(789), new Timestamp(101)));
        dao.add(expected.get(0));
        dao.add(expected.get(1));
        List<Activity> actual = dao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectActivity() {
        Professor professor = new Professor(1,"name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity expected = new Activity(1, 1, 1, new Timestamp(123), new Timestamp(456));
        dao.add(expected);
        Activity actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldAddCorrectActivity() {
        Professor professor = new Professor(1,"name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity expected = new Activity(1, 1, 1, new Timestamp(123), new Timestamp(456));
        int id = dao.add(expected);
        Activity actual = dao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Professor professor = new Professor(1,"name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity expected = new Activity(1, 1, 1, new Timestamp(123), new Timestamp(456));
        dao.add(expected);
        expected.setStartTime(new Timestamp(236));
        dao.update(expected);
        Activity actual = dao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        Professor professor = new Professor(1,"name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity activity = new Activity(1, 1, 1, new Timestamp(123), new Timestamp(456));
        dao.add(activity);
        dao.deleteById(1);
        Activity actual = dao.findById(1);
        assertNull(actual);
    }

}
