package com.foxminded.services;

import com.foxminded.dao.UserDao;
import com.foxminded.dto.UserDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao dao;
    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(new ModelMapper(), dao);
    }

    @Test
    void testFindAll_ShouldReturnAllRecords() {
        List<User> users = Arrays.asList(new User(1, "1", "role","name", "surname", "q"),
                new User(2, "2", "role","name2", "surname2", "q2"));
        List<UserDto> expected = Arrays.asList(new UserDto("1", "role","name", "surname", "q"),
                new UserDto("2", "role","name2", "surname2", "q2"));
        when(dao.findAll()).thenReturn(users);
        List<UserDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectRecord() {
        User user = new User(1, "1", "role","name", "surname", "q");
        UserDto expected = new UserDto("1", "role","name", "surname", "q");
        when(dao.findById(anyInt())).thenReturn(user);
        UserDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testFindByPersonalId_ShouldReturnCorrectRecord() {
        User user = new User(1, "1", "role","name", "surname", "q");
        UserDto expected = new UserDto("1", "role","name", "surname", "q");
        when(dao.findByPersonalId(anyString())).thenReturn(user);
        UserDto actual = service.findByPersonalId(anyString());
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        service.add(new UserDto("1", "role","name", "surname", "q"));
        User expected = new User("1", "role","name", "surname", "q");
        verify(dao, times(1)).add(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        User expected = new User("1", "role","name", "surname", "q");
        when(dao.existsByPersonalId(anyString())).thenReturn(true);
        service.update(new UserDto("1", "role","name", "surname", "q"));
        verify(dao, times(1)).update(expected);
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
        UserDto userDto = new UserDto("1", "role","name", "surname", "q");
        when(dao.existsByPersonalId(anyString())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> service.add(userDto));
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundException() {
        UserDto userDto = new UserDto("1", "role","name", "surname", "q");
        assertThrows(EntityNotFoundException.class, () -> service.update(userDto));
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
