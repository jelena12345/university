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
    void testFindAll_ShouldReturnAllStudents() {
        List<Student> students = Arrays.asList(new Student(1, "1", "name", "surname"),
                new Student(2, "2", "name2", "surname2"));
        List<StudentDto> expected = Arrays.asList(new StudentDto("1", "name", "surname"),
                new StudentDto("2", "name2", "surname2"));
        when(dao.findAll()).thenReturn(students);
        List<StudentDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectStudent() {
        Student student = new Student(1, "1", "name", "surname");
        StudentDto expected = new StudentDto("1", "name", "surname");
        when(dao.findById(anyInt())).thenReturn(student);
        StudentDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testFindByPersonalId_ShouldReturnCorrectStudent() {
        Student student = new Student(1, "1", "name", "surname");
        StudentDto expected = new StudentDto("1", "name", "surname");
        when(dao.findByPersonalId(anyString())).thenReturn(student);
        StudentDto actual = service.findByPersonalId(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(new StudentDto("1", "name", "surname"));
        Student expected = new Student("1", "name", "surname");
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        service.update(1, new StudentDto("1", "name", "surname"));
        Student expected = new Student("1", "name", "surname");
        verify(dao, times(1)).update(1, expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        service.deleteById(anyInt());
        verify(dao, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteByPersonalId_ShouldCallDeleteByIdMethodForDao() {
        service.deleteByPersonalId(anyString());
        verify(dao, times(1)).deleteByPersonalId(anyString());
    }
}
