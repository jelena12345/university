package com.foxminded.services;

import com.foxminded.dao.StudentDao;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDao dao;
    private StudentService service;

    @BeforeEach
    void setUp() {
        service = new StudentService(new ModelMapper(), dao);
    }

    @Test
    void testGetAll_ShouldReturnAllStudents() {
        List<Student> students = Arrays.asList(new Student(1, "name", "surname"),
                new Student(2, "name2", "surname2"));
        List<StudentDto> expected = Arrays.asList(new StudentDto("name", "surname"),
                new StudentDto("name2", "surname2"));
        when(dao.findAll()).thenReturn(students);
        List<StudentDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testGetById_ShouldReturnCorrectStudent() {
        Student student = new Student(1, "name", "surname");
        StudentDto expected = new StudentDto("name", "surname");
        when(dao.findById(anyInt())).thenReturn(student);
        StudentDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(new StudentDto("name", "surname"));
        Student expected = new Student("name", "surname");
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        service.update(1, new StudentDto("name", "surname"));
        Student expected = new Student("name", "surname");
        verify(dao, times(1)).update(1, expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        service.deleteById(1);
        verify(dao, times(1)).deleteById(1);
    }
}
