package com.foxminded.services;

import com.foxminded.dao.EventDao;
import com.foxminded.dao.CourseDao;
import com.foxminded.dao.UserDao;
import com.foxminded.dto.EventDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Event;
import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventDao dao;
    @Mock
    private UserDao userDao;
    @Mock
    private CourseDao courseDao;
    private EventService service;

    @BeforeEach
    void setUp() {
        service = new EventService(new ModelMapper(), dao, userDao, courseDao);
    }

    @Test
    void testFindAll_ShouldReturnAllRecords() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        List<Event> events = Arrays.asList(
                new Event(1, user, course, from, to),
                new Event(2, user, course, from, to));
        List<EventDto> expected = Arrays.asList(
                new EventDto(1, userDto, courseDto, from, to),
                new EventDto(2, userDto, courseDto, from, to));
        when(dao.findAll()).thenReturn(events);
        List<EventDto> actual = service.findAll();
        assertEquals(expected, actual);
    }

    @Test
    void testFindById_ShouldReturnCorrectRecord() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        Event event = new Event(2, user, course, from, to);
        EventDto expected = new EventDto(2, userDto, courseDto, from, to);
        when(dao.findById(anyInt())).thenReturn(Optional.of(event));
        EventDto actual = service.findById(anyInt());
        assertEquals(expected, actual);
    }

    @Test
    void testFindEventsForUserFromTo_ShouldReturnCorrectRecords() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        course.setEvents(Arrays.asList(
                new Event(1, user, course, from.plusMinutes(1), to.minusMinutes(1)),
                new Event(2, user, course, from.plusMinutes(1), to.minusMinutes(1))));
        user.getCoursesForUser().add(course);
        List<EventDto> expected = Arrays.asList(
                new EventDto(1, userDto, courseDto, from.plusMinutes(1), to.minusMinutes(1)),
                new EventDto(2, userDto, courseDto, from.plusMinutes(1), to.minusMinutes(1)));
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        List<EventDto> actual = service.findEventsForUserFromTo(userDto, from, to);
        assertEquals(expected, actual);
    }

    @Test
    void testAdd_ShouldCallAddMethodForDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        EventDto eventDto = new EventDto(1, userDto, courseDto, from, to);
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        service.add(eventDto);
        Event expected = new Event(1, user, course, from, to);
        verify(dao, times(1)).save(expected);
    }

    @Test
    void testUpdate_ShouldCallUpdateMethodForDao() {
        User user = new User(1, "1", "role", "name", "surname", "about");
        Course course = new Course(1, "name", "description");
        Event expected = new Event(1, user, course, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        EventDto eventDto = new EventDto(1, userDto, courseDto, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(userDao.findByPersonalId(anyString())).thenReturn(Optional.of(user));
        when(courseDao.findByName(anyString())).thenReturn(Optional.of(course));
        when(dao.existsById(anyInt())).thenReturn(true);
        service.update(eventDto);
        verify(dao, times(1)).save(expected);
    }

    @Test
    void testDeleteById_ShouldCallDeleteByIdMethodForDao() {
        when(dao.existsById(anyInt())).thenReturn(true);
        service.deleteById(anyInt());
        verify(dao, times(1)).deleteById(anyInt());
    }

    @Test
    void testAdd_ShouldThrowEntityNotFoundException() {
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        EventDto eventDto = new EventDto(1, userDto, courseDto, from, to);
        assertThrows(EntityNotFoundException.class, () -> service.add(eventDto));
    }

    @Test
    void testUpdate_ShouldThrowEntityNotFoundException() {
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        CourseDto courseDto = new CourseDto("name", "description");
        EventDto eventDto = new EventDto(1, userDto, courseDto, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        assertThrows(EntityNotFoundException.class, () -> service.update(eventDto));
    }

    @Test
    void testDeleteById_ShouldThrowEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(1));
    }

    @Test
    void testFindEventsForUserFromTo_ShouldThrowEntityNotFoundException() {
        UserDto userDto = new UserDto("1", "role", "name", "surname", "about");
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(1);
        assertThrows(EntityNotFoundException.class, () -> service.findEventsForUserFromTo(userDto, from, to));
    }
}
