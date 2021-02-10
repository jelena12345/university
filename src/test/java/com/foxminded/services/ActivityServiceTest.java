package com.foxminded.services;

import com.foxminded.dao.ActivityDao;
import com.foxminded.dao.CourseDao;
import com.foxminded.dao.UserDao;
import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Activity;
import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityDao dao;
    @Mock
    private UserDao userDao;
    @Mock
    private CourseDao courseDao;
    private ActivityService service;

    @BeforeEach
    void setUp() {
        service = new ActivityService(new ModelMapper(), dao, userDao, courseDao);
    }

    @Test
    void testFindAll_ShouldReturnAllActivities() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        List<Activity> activities = Arrays.asList(
                new Activity(1, user, course, LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
                new Activity(2, user, course, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        List<ActivityDto> expected = Arrays.asList(
                new ActivityDto(1, userDto, courseDto, LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
                new ActivityDto(2, userDto, courseDto, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(dao.findAll()).thenReturn(activities);
        List<ActivityDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectActivity() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        Activity activity = new Activity(2, user, course, from, to);
        ActivityDto expected = new ActivityDto(2, userDto, courseDto, from, to);
        when(dao.findById(anyInt())).thenReturn(activity);
        ActivityDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        User user = new User(1,"1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        ActivityDto activity = new ActivityDto(1, userDto, courseDto, from, to);
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        service.add(activity);
        Activity expected = new Activity(1, user, course, from, to);
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        Activity expected = new Activity(1, user, course, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        ActivityDto activity = new ActivityDto(1, userDto, courseDto, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        when(dao.existsById(anyInt())).thenReturn(true);
        service.update(activity);
        verify(dao, times(1)).update(expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        when(dao.existsById(anyInt())).thenReturn(true);
        service.deleteById(anyInt());
        verify(dao, times(1)).deleteById(anyInt());
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundException() {
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        ActivityDto activityDto = new ActivityDto(1, userDto, courseDto, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertThrows(EntityNotFoundException.class, () -> service.update(activityDto));
    }

    @Test
    void testDeleteById_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1));
    }

    @Test
    void testExistsById_ShouldCallExistsByIdMethodOnDao() {
        service.existsById(anyInt());
        verify(dao, times(1)).existsById(anyInt());
    }

}
