package com.foxminded.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import com.foxminded.dao.CourseDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.entities.Course;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseDao dao;
    private CourseService service;

    @BeforeEach
    void setUp() {
        service = new CourseService(new ModelMapper(), dao);
    }

    @Test
    void testFindAll_ShouldReturnAllCourses() {
        List<Course> courses = Arrays.asList(new Course(1, "name", "description"),
                new Course(2, "name2", "description2"));
        List<CourseDto> expected = Arrays.asList(new CourseDto("name", "description"),
                new CourseDto("name2", "description2"));
        when(dao.findAll()).thenReturn(courses);
        List<CourseDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectCourse() {
        Course course = new Course(1, "name", "description");
        CourseDto expected = new CourseDto("name", "description");
        when(dao.findById(anyInt())).thenReturn(Optional.of(course));
        CourseDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testFindByName_ShouldReturnCorrectCourse() {
        Course course = new Course(1, "name", "description");
        CourseDto expected = new CourseDto("name", "description");
        when(dao.findByName(anyString())).thenReturn(Optional.of(course));
        CourseDto actual = service.findByName(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void testSave_NewUser_ShouldCallAddMethodForDao() {
        service.save(new CourseDto("name", "description"));
        Course expected = new Course("name", "description");
        verify(dao, times(1)).save(expected);
    }

    @Test
    void testSave_ExistingUser_ShouldCallUpdateMethodForDao() {
        Course expected = new Course(1, "name", "description");
        when(dao.existsByName(anyString())).thenReturn(true);
        when(dao.findByName(anyString())).thenReturn(Optional.of(expected));
        service.save(new CourseDto("name", "description"));
        verify(dao, times(1)).save(expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        when(dao.existsById(anyInt())).thenReturn(true);
        service.deleteById(anyInt());
        verify(dao, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteByName_ShouldCallDeleteByNameMethodForDao() {
        when(dao.existsByName(anyString())).thenReturn(true);
        service.deleteByName("name");
        verify(dao, times(1)).deleteByName(anyString());
    }

    @Test
    void testSave_NewUser_ShouldThrowEntityAlreadyExistsException() {
        CourseDto courseDto = new CourseDto("name", "description");
        when(dao.existsByName(anyString())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> service.save(courseDto));
    }

    @Test
    void testSave_ExistingUser_ShouldThrowEntityNotFoundException() {
        CourseDto courseDto = new CourseDto("name", "description");
        assertThrows(EntityNotFoundException.class, () -> service.save(courseDto));
    }

    @Test
    void testDeleteById_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1));
    }

    @Test
    void testDeleteByName_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteByName("name"));
    }

}
