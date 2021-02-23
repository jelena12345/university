package com.foxminded.services;

import com.foxminded.dao.CourseDao;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCourseServiceTest {

    @Mock
    private UserDao userDao;
    @Mock
    private CourseDao courseDao;
    private UserCourseService service;

    @BeforeEach
    void setUp() {
        service = new UserCourseService(new ModelMapper(), userDao, courseDao);
    }

    @Test
    void testFindStudentsForCourse_ShouldFindCorrectRecords() {
        CourseDto courseDto = new CourseDto("name", "description");
        Course course = new Course("name", "description");
        course.setUsersForCourse(Arrays.asList(
                new User(1, "1", "student", "name", "surname", "about"),
                new User(2, "2", "professor", "name1", "surname1", "about1")));
        List<UserDto> expected = Collections.singletonList(
                new UserDto("1", "student", "name", "surname", "about"));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        List<UserDto> actual = service.findStudentsForCourse(courseDto);
        assertEquals(expected, actual);
    }

    @Test
    void testFindCoursesForUser_ShouldFindCorrectRecords() {
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        User user = new User(1, "1", "role", "name", "surname", "about");
        user.setCoursesForUser(Collections.singletonList(new Course("name", "description")));
        List<CourseDto> expected = Collections.singletonList(
                new CourseDto("name", "description"));
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        List<CourseDto> actual = service.findCoursesForUser(userDto);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAvailableCoursesForUser_ShouldFindCorrectRecords() {
        UserDto userDto = new UserDto("1", "r", "n", "s", "a");
        User user = new User(1, "1", "r", "n", "s", "a");
        Course course = new Course("n", "d");
        user.setCoursesForUser(Collections.singletonList(course));
        List<CourseDto> expected = Collections.singletonList(
                new CourseDto("n2", "d4"));
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findAll()).thenReturn(Arrays.asList(course, new Course("n2", "d4")));
        List<CourseDto> actual = service.findAvailableCoursesForUser(userDto);
        assertEquals(expected, actual);
    }

    @Test
    void testSaveUserForCourse_ShouldCallSaveMethodForUserDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        service.saveUserForCourse(
                new UserDto("1", "role", "name", "surname", "about"),
                new CourseDto("name", "description"));
        verify(userDao, times(1)).save(user);
    }

    @Test
    void testDeleteUserForCourse_ShouldCallSaveMethodForDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        user.getCoursesForUser().add(course);
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        service.deleteUserForCourse(
                new UserDto("1", "role", "name", "surname", "about"),
                new CourseDto("name", "description"));
        verify(userDao, times(1)).save(user);
    }

    @Test
    void testSaveUserForCourse_ShouldThrowEntityAlreadyExistsException() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        user.getCoursesForUser().add(course);
        CourseDto courseDto = new CourseDto("name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        assertThrows(EntityAlreadyExistsException.class, () -> service.saveUserForCourse(userDto, courseDto));
    }

    @Test
    void testDelete_ShouldThrowEntityNotFoundException() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        CourseDto courseDto = new CourseDto("name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        assertThrows(EntityNotFoundException.class, () -> service.deleteUserForCourse(userDto, courseDto));
    }
}
