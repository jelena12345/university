package com.foxminded.services;

import com.foxminded.dao.ProfessorDao;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Professor;
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
class ProfessorServiceTest {

    @Mock
    private ProfessorDao dao;
    private ProfessorService service;

    @BeforeEach
    void setUp() {
        service = new ProfessorService(new ModelMapper(), dao);
    }

    @Test
    void testGetAll_ShouldReturnAllProfessors() {
        List<Professor> professors = Arrays.asList(new Professor(1, "name", "surname", "q"),
                new Professor(2, "name2", "surname2", "q2"));
        List<ProfessorDto> expected = Arrays.asList(new ProfessorDto("name", "surname", "q"),
                new ProfessorDto("name2", "surname2", "q2"));
        when(dao.findAll()).thenReturn(professors);
        List<ProfessorDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testGetById_ShouldReturnCorrectProfessor() {
        Professor professor = new Professor(1, "name", "surname", "q");
        ProfessorDto expected = new ProfessorDto("name", "surname", "q");
        when(dao.findById(anyInt())).thenReturn(professor);
        ProfessorDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(new ProfessorDto("name", "surname", "q"));
        Professor expected = new Professor("name", "surname", "q");
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        service.update(1, new ProfessorDto("name", "surname", "q"));
        Professor expected = new Professor("name", "surname", "q");
        verify(dao, times(1)).update(1, expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        service.deleteById(1);
        verify(dao, times(1)).deleteById(1);
    }
}
