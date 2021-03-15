package com.foxminded.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.EventDto;
import com.foxminded.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class TimetableRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
        this.mockMvc.perform(get("/rest/timetable/events/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(event.getId()));
    }

    @Test
    void testGetFilteredEvents_ShouldReturnFilteredEvents() throws Exception {
        this.mockMvc.perform(get("/rest/timetable/1/filter/2011-02-02_2025-02-03")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void testAddEvent_ValidData_ShouldAddEvent() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        CourseDto course = new CourseDto("name", "description");
        EventDto event = new EventDto(0,
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
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        CourseDto course = new CourseDto("name", "description");
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
