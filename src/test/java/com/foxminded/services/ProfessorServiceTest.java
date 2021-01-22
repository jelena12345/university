package com.foxminded.services;

import com.foxminded.dao.ProfessorDao;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Professor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testFindAll_ShouldReturnAllProfessors() {
        List<Professor> professors = Arrays.asList(new Professor(1, "1", "name", "surname", "q"),
                new Professor(2, "2", "name2", "surname2", "q2"));
        List<ProfessorDto> expected = Arrays.asList(new ProfessorDto("1", "name", "surname", "q"),
                new ProfessorDto("2", "name2", "surname2", "q2"));
        when(dao.findAll()).thenReturn(professors);
        List<ProfessorDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectProfessor() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        ProfessorDto expected = new ProfessorDto("1", "name", "surname", "q");
        when(dao.findById(anyInt())).thenReturn(professor);
        ProfessorDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testFindByPersonalId_ShouldReturnCorrectProfessor() {
        Professor professor = new Professor(1, "1", "name", "surname", "q");
        ProfessorDto expected = new ProfessorDto("1", "name", "surname", "q");
        when(dao.findByPersonalId(anyString())).thenReturn(professor);
        ProfessorDto actual = service.findByPersonalId(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(new ProfessorDto("1", "name", "surname", "q"));
        Professor expected = new Professor("1", "name", "surname", "q");
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        Professor expected = new Professor("1", "name", "surname", "q");
        when(dao.existsById(anyInt())).thenReturn(true);
        service.update(1, new ProfessorDto("1", "name", "surname", "q"));
        verify(dao, times(1)).update(1, expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        when(dao.existsById(anyInt())).thenReturn(true);
        service.deleteById(anyInt());
        verify(dao, times(1)).deleteById(anyInt());
    }

    @Test
    void testDeleteByPersonalId_ShouldCallDeleteByIdMethodForDao() {
        when(dao.existsByPersonalId(anyString())).thenReturn(true);
        service.deleteByPersonalId(anyString());
        verify(dao, times(1)).deleteByPersonalId(anyString());
    }

    @Test
    void testAdd_ShouldThrowEntityAlreadyExistsException() {
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        when(dao.existsByPersonalId(anyString())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> service.add(professorDto));
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundException() {
        ProfessorDto professorDto = new ProfessorDto("1", "name", "surname", "q");
        assertThrows(EntityNotFoundException.class, () -> service.update(1, professorDto));
    }

    @Test
    void testDeleteById_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1));
    }

    @Test
    void testDeleteByName_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteByPersonalId("name"));
    }

    @Test
    void testExistsById_ShouldCallExistsByIdMethodOnDao() {
        service.existsById(anyInt());
        verify(dao, times(1)).existsById(anyInt());
    }

    @Test
    void testExistsById_ShouldCallExistsByNameMethodOnDao() {
        service.existsByName(anyString());
        verify(dao, times(1)).existsByPersonalId(anyString());
    }
}
