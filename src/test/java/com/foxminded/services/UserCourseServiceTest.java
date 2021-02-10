package com.foxminded.services;

import com.foxminded.dao.CourseDao;
import com.foxminded.dao.UserCourseDao;
import com.foxminded.dao.UserDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCourseServiceTest {

    @Mock
    private UserCourseDao dao;
    @Mock
    private UserDao userDao;
    @Mock
    private CourseDao courseDao;
    private UserCourseService service;

    @BeforeEach
    void setUp() {
        service = new UserCourseService(new ModelMapper(), dao, userDao, courseDao);
    }

    @Test
    void testFindCoursesForUser_ShouldFindCorrectRecords() {
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        User user = new User(1, "1", "role", "name", "surname", "about");
        List<CourseDto> expected = Collections.singletonList(
                new CourseDto("name", "description"));
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(dao.findCoursesForUser(any())).thenReturn(
                Collections.singletonList(
                        new Course("name", "description")));
        List<CourseDto> actual = service.findCoursesForUser(userDto);
        assertEquals(expected, actual);
    }

    @Test
    void testFindUsersForCourse_ShouldFindCorrectRecords() {
        CourseDto courseDto = new CourseDto("name", "description");
        Course course = new Course("name", "description");
        List<UserDto> expected = Collections.singletonList(
                new UserDto("1", "role", "name", "surname", "about"));
        when(courseDao.findByName(anyString())).thenReturn(course);
        when(dao.findUsersForCourse(any())).thenReturn(
                Collections.singletonList(
                        new User(1, "1", "role", "name", "surname", "about")));
        List<UserDto> actual = service.findUsersForCourse(courseDto);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        service.add(
                new UserDto("1", "role", "name", "surname", "about"),
                new CourseDto("name", "description"));
        verify(dao, times(1)).add(user, course);
    }

    @Test
    void testDelete_ShouldCallDeleteMethodForDao() {
        when(dao.existsCourseForUser(any(), any())).thenReturn(true);
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        service.delete(
                new UserDto("1", "role", "name", "surname", "about"),
                new CourseDto("name", "description"));
        verify(dao, times(1)).delete(user, course);
    }

    @Test
    void testExistsUserForCourse_ShouldCallExistsByNameMethodOnDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        CourseDto courseDto = new CourseDto("name", "description");
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        service.existsCourseForUser(userDto, courseDto);
        verify(dao, times(1)).existsCourseForUser(user, course);
    }

    @Test
    void testAdd_ShouldThrowEntityAlreadyExistsException() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        CourseDto courseDto = new CourseDto("name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        when(dao.existsCourseForUser(any(), any())).thenReturn(true);
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        assertThrows(EntityAlreadyExistsException.class, () -> service.add(userDto, courseDto));
    }

    @Test
    void testDelete_ShouldThrowEntityNotFoundException() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        CourseDto courseDto = new CourseDto("name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        when(userDao.findByPersonalId(anyString())).thenReturn(user);
        when(courseDao.findByName(anyString())).thenReturn(course);
        assertThrows(EntityNotFoundException.class, () -> service.delete(userDto, courseDto));
    }
}
