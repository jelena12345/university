package com.foxminded.services;

import com.foxminded.dao.GroupDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
import com.foxminded.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                Arrays.asList(new Student(1,  "1", "name", "surname"),
                        new Student(2,  "2", "name2", "surname2")));
        GroupDto expected = new GroupDto(courseDto,
                Arrays.asList(new StudentDto("1", "name", "surname"),
                        new StudentDto("2", "name2", "surname2")));
        when(dao.findGroupForCourse(any())).thenReturn(group);
        GroupDto actual = service.findGroupForCourse(courseDto);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(
                new StudentDto("1", "name", "surname"),
                new CourseDto("name", "description"));
        verify(dao, times(1)).add(
                new Student("1", "name", "surname"),
                new Course("name", "description"));
    }

    @Test
    void testDelete_ShouldCallDeleteMethodForDao() {
        service.delete(
                new StudentDto("1", "name", "surname"),
                new CourseDto("name", "description"));
        verify(dao, times(1)).delete(
                new Student("1", "name", "surname"),
                new Course("name", "description"));
    }
}
