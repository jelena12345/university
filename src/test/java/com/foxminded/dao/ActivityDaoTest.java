package com.foxminded.dao;

import com.foxminded.entities.Activity;
import com.foxminded.entities.Course;
import com.foxminded.entities.Professor;
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
    private ActivityDao activityDao;
    private CourseDao courseDao;
    private ProfessorDao professorDao;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql").build();
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
        activityDao = new ActivityDao(template);
        courseDao = new CourseDao(template);
        professorDao = new ProfessorDao(template);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    void testFindAll_ShouldFindAllActivities() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        List<Activity> expected = Arrays.asList(
                new Activity(1, professor, course, new Timestamp(123), new Timestamp(456)),
                new Activity(2, professor, course, new Timestamp(789), new Timestamp(101)));
        activityDao.add(expected.get(0));
        activityDao.add(expected.get(1));
        List<Activity> actual = activityDao.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldFindCorrectActivity() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity expected = new Activity(1, professor, course, new Timestamp(123), new Timestamp(456));
        activityDao.add(expected);
        Activity actual = activityDao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldAddCorrectActivity() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity expected = new Activity(1, professor, course, new Timestamp(123), new Timestamp(456));
        int id = activityDao.add(expected);
        Activity actual = activityDao.findById(id);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ShouldUpdateValues() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        Professor newProfessor = new Professor(2, "2", "name2", "surname2", "q2");
        professorDao.add(professor);
        professorDao.add(newProfessor);
        Course course = new Course(1, "name", "description");
        Course newCourse = new Course(2, "name2", "description2");
        courseDao.add(course);
        courseDao.add(newCourse);
        Activity expected = new Activity(1, professor, course, new Timestamp(123), new Timestamp(456));
        activityDao.add(expected);
        expected.setProfessor(newProfessor);
        expected.setCourse(newCourse);
        expected.setStartTime(new Timestamp(236));
        expected.setEndTime(new Timestamp(565));
        activityDao.update(expected);
        Activity actual = activityDao.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteById_ShouldFindNull() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        professorDao.add(professor);
        Course course = new Course(1, "name", "description");
        courseDao.add(course);
        Activity activity = new Activity(1, professor, course, new Timestamp(123), new Timestamp(456));
        activityDao.add(activity);
        activityDao.deleteById(1);
        Activity actual = activityDao.findById(1);
        assertNull(actual);
    }

}
