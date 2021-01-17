package com.foxminded.services;

import com.foxminded.dao.ActivityDao;
import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Activity;
import com.foxminded.entities.Course;
import com.foxminded.entities.Professor;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityDao dao;
    private ActivityService service;

    @BeforeEach
    void setUp() {
        service = new ActivityService(new ModelMapper(), dao, Logger.getLogger(Logger.GLOBAL_LOGGER_NAME));
    }

    @Test
    void testFindAll_ShouldReturnAllActivities() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        Course course = new Course(1, "name", "description");
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        CourseDto courseDto = new CourseDto("name", "description");
        List<Activity> activities = Arrays.asList(
                new Activity(1, professor, course, new Timestamp(123), new Timestamp(456)),
                new Activity(2, professor, course, new Timestamp(789), new Timestamp(101)));
        List<ActivityDto> expected = Arrays.asList(
                new ActivityDto(1, professorDto, courseDto, new Timestamp(123), new Timestamp(456)),
                new ActivityDto(2, professorDto, courseDto, new Timestamp(789), new Timestamp(101)));
        when(dao.findAll()).thenReturn(activities);
        List<ActivityDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectActivity() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        Course course = new Course(1, "name", "description");
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        CourseDto courseDto = new CourseDto("name", "description");
        Activity activity = new Activity(2, professor, course, new Timestamp(789), new Timestamp(101));
        ActivityDto expected = new ActivityDto(2, professorDto, courseDto, new Timestamp(789), new Timestamp(101));
        when(dao.findById(anyInt())).thenReturn(activity);
        ActivityDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        Professor professor = new Professor("1", "name", "surname", "q");
        Course course = new Course("name", "description");
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        CourseDto courseDto = new CourseDto("name", "description");
        ActivityDto activity = new ActivityDto(1, professorDto, courseDto, new Timestamp(789), new Timestamp(101));
        service.add(activity);
        Activity expected = new Activity(1, professor, course, new Timestamp(789), new Timestamp(101));
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        Professor professor = new Professor("1", "name", "surname", "q");
        Course course = new Course("name", "description");
        Activity expected = new Activity(1, professor, course, new Timestamp(789), new Timestamp(101));
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        CourseDto courseDto = new CourseDto("name", "description");
        ActivityDto activity = new ActivityDto(1, professorDto, courseDto, new Timestamp(789), new Timestamp(101));
        when(dao.findById(anyInt())).thenReturn(expected);
        service.update(activity);
        verify(dao, times(1)).update(expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        Professor professor = new Professor("1", "name", "surname", "q");
        Course course = new Course("name", "description");
        Activity activity = new Activity(1, professor, course, new Timestamp(789), new Timestamp(101));
        when(dao.findById(anyInt())).thenReturn(activity);
        service.deleteById(anyInt());
        verify(dao, times(1)).deleteById(anyInt());
    }

    @Test
    void testAdd_ShouldThrowEntityAlreadyExistsException() {
        Professor professor = new Professor("1", "name", "surname", "q");
        Course course = new Course("name", "description");
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        CourseDto courseDto = new CourseDto("name", "description");
        Activity activity = new Activity(1, professor, course, new Timestamp(789), new Timestamp(101));
        when(dao.findById(anyInt())).thenReturn(activity);
        ActivityDto activityDto = new ActivityDto(1, professorDto, courseDto, new Timestamp(789), new Timestamp(101));
        assertThrows(EntityAlreadyExistsException.class, () -> service.add(activityDto));
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundException() {
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        CourseDto courseDto = new CourseDto("name", "description");
        ActivityDto activityDto = new ActivityDto(1, professorDto, courseDto, new Timestamp(789), new Timestamp(101));
        assertThrows(EntityNotFoundException.class, () -> service.update(activityDto));
    }

    @Test
    void testDeleteById_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1));
    }

}
