package com.foxminded.controllers.rest;

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
                new TimetableRestController(service, courseService, userService)).build();
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
    void testAddEvent_ShouldAddEvent() throws Exception {
        this.mockMvc.perform(post("/rest/timetable/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"course\": " +
                        "{ \"description\": \"string\", " +
                        "\"name\": \"string\" }, " +
                        "\"from\": \"2022-03-07T17:37:34.353Z\", " +
                        "\"id\": 0, " +
                        "\"to\": \"2022-03-07T17:37:34.353Z\", " +
                        "\"user\": { " +
                        "\"about\": \"string\", " +
                        "\"name\": \"string\", " +
                        "\"personalId\": \"string\", " +
                        "\"role\": \"student\", " +
                        "\"surname\": \"string\" }}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateEvent_ShouldUpdateEvent() throws Exception {
        this.mockMvc.perform(post("/rest/timetable/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"course\": " +
                        "{ \"description\": \"string\", " +
                        "\"name\": \"string\" }, " +
                        "\"from\": \"2022-03-07T17:37:34.353Z\", " +
                        "\"id\": 0, " +
                        "\"to\": \"2022-03-07T17:37:34.353Z\", " +
                        "\"user\": { " +
                        "\"about\": \"string\", " +
                        "\"name\": \"string\", " +
                        "\"personalId\": \"string\", " +
                        "\"role\": \"student\", " +
                        "\"surname\": \"string\" }}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEvent_ShouldDeleteEvent() throws Exception {
        this.mockMvc.perform(post("/rest/timetable/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"course\": " +
                        "{ \"description\": \"string\", " +
                        "\"name\": \"string\" }, " +
                        "\"from\": \"2022-03-07T17:37:34.353Z\", " +
                        "\"id\": 0, " +
                        "\"to\": \"2022-03-07T17:37:34.353Z\", " +
                        "\"user\": { " +
                        "\"about\": \"string\", " +
                        "\"name\": \"string\", " +
                        "\"personalId\": \"string\", " +
                        "\"role\": \"student\", " +
                        "\"surname\": \"string\" }}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
