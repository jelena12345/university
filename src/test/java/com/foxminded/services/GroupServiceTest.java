package com.foxminded.services;

import com.foxminded.dao.GroupDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupDao dao;
    private GroupService service;

    @BeforeEach
    void setUp() {
        service = new GroupService(new ModelMapper(), dao);
    }

    @Test
    void testFindGroupForCourse_ShouldFindCorrectGroup() {
        CourseDto courseDto = new CourseDto("name", "description");
        Group group = new Group(new Course(1, "name", "description"),
                Arrays.asList(new User(1,  "1", "role","name", "surname", "about"),
                        new User(2,  "2", "role","name2", "surname2", "about2")));
        GroupDto expected = new GroupDto(courseDto,
                Arrays.asList(new UserDto("1", "role","name", "surname", "about"),
                        new UserDto("2", "role", "name2", "surname2", "about2")));
        when(dao.findGroupForCourse(any())).thenReturn(group);
        GroupDto actual = service.findGroupForCourse(courseDto);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(
                new UserDto("1", "role", "name", "surname", "about"),
                new CourseDto("name", "description"));
        verify(dao, times(1)).add(
                new User("1", "role", "name", "surname", "about"),
                new Course("name", "description"));
    }

    @Test
    void testDelete_ShouldCallDeleteMethodForDao() {
        when(dao.existsCourseForUser(any(), any())).thenReturn(true);
        service.delete(
                new UserDto("1", "role", "name", "surname", "about"),
                new CourseDto("name", "description"));
        verify(dao, times(1)).delete(
                new User("1", "role", "name", "surname", "about"),
                new Course("name", "description"));
    }

    @Test
    void testExistsGroupForCourse_ShouldCallExistsByNameMethodOnDao() {
        User user = new User("1", "role", "name", "surname", "about");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        Course course = new Course("name", "description");
        CourseDto courseDto = new CourseDto("name", "description");
        service.existsCourseForUser(userDto, courseDto);
        verify(dao, times(1)).existsCourseForUser(user, course);
    }

    @Test
    void testAdd_ShouldThrowEntityAlreadyExistsException() {
        CourseDto courseDto = new CourseDto("name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        when(dao.existsCourseForUser(any(), any())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> service.add(userDto, courseDto));
    }

    @Test
    void testDelete_ShouldThrowEntityNotFoundException() {
        CourseDto courseDto = new CourseDto("name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        assertThrows(EntityNotFoundException.class, () -> service.delete(userDto, courseDto));
    }
}
