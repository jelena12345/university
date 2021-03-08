package com.foxminded.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.EventDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.EventService;
import com.foxminded.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TimetableRestControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock
    private EventService service;
    @Mock
    private CourseService courseService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new TimetableRestController(service, courseService, userService))
                .build();
    }

    @Test
    void testGetAllEvents_ShouldReturnAllEvents() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        EventDto event = new EventDto(1,
                user,
                course,
                LocalDateTime.now(),
                LocalDateTime.now());
        List<EventDto> events = Collections.singletonList(event);
        when(service.findAll()).thenReturn(events);
        this.mockMvc.perform(get("/rest/timetable/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(event.getId()));
    }

    @Test
    void testGetEventById_ShouldReturnEventById() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        EventDto event = new EventDto(1,
                user,
                course,
                LocalDateTime.now(),
                LocalDateTime.now());
        when(service.findById(anyInt())).thenReturn(event);
        this.mockMvc.perform(get("/rest/timetable/events/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(event.getId()));
    }

    @Test
    void testGetFilteredEvents_ShouldReturnFilteredEvents() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        EventDto event = new EventDto(1,
                user,
                course,
                LocalDateTime.now(),
                LocalDateTime.now());
        List<EventDto> events = Collections.singletonList(event);
        when(userService.findByPersonalId(anyString())).thenReturn(user);
        when(service.findEventsForUserFromTo(any(), any(), any())).thenReturn(events);
        this.mockMvc.perform(get("/rest/timetable/1/filter/2011-02-02_2011-02-03")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(event.getId()));
    }

    @Test
    void testAddEvent_ValidData_ShouldAddEvent() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        EventDto event = new EventDto(1,
                user,
                course,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1));
        this.mockMvc.perform(post("/rest/timetable/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddEvent_InvalidData_ShouldAddEvent() throws Exception {
        EventDto event = new EventDto();
        this.mockMvc.perform(post("/rest/timetable/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateEvent_ValidData_ShouldUpdateEvent() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        EventDto event = new EventDto(1,
                user,
                course,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1));
        this.mockMvc.perform(post("/rest/timetable/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEvent_InvalidData_ShouldUpdateEvent() throws Exception {
        EventDto event = new EventDto();
        this.mockMvc.perform(post("/rest/timetable/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEvent_ShouldDeleteEvent() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        EventDto event = new EventDto(1,
                user,
                course,
                LocalDateTime.now(),
                LocalDateTime.now());
        this.mockMvc.perform(post("/rest/timetable/1/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
