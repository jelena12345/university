package com.foxminded.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.UserCourseService;
import com.foxminded.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService service;
    @Mock
    private UserCourseService userCourseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new UserRestController(service, userCourseService)).build();
    }

    @Test
    void testGetCourses_ShouldReturnCourses() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        List<UserDto> users = Collections.singletonList(user);
        when(service.findAll()).thenReturn(users);
        this.mockMvc.perform(get("/rest/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalId").value(user.getPersonalId()))
                .andExpect(jsonPath("$[0].role").value(user.getRole()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].surname").value(user.getSurname()))
                .andExpect(jsonPath("$[0].about").value(user.getAbout()));
    }

    @Test
    void testGetUserByPersonalId_ShouldReturnUser() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        when(service.findByPersonalId(anyString())).thenReturn(user);
        this.mockMvc.perform(get("/rest/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalId").value(user.getPersonalId()))
                .andExpect(jsonPath("$.role").value(user.getRole()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.surname").value(user.getSurname()))
                .andExpect(jsonPath("$.about").value(user.getAbout()));
    }

    @Test
    void testGetAvailableCourses_ShouldReturnCourses() throws Exception {
        CourseDto course = new CourseDto("a", "b");
        List<CourseDto> courses = Collections.singletonList(course);
        when(userCourseService.findAvailableCoursesForUser(any())).thenReturn(courses);
        this.mockMvc.perform(get("/rest/users/1/available")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(course.getName()))
                .andExpect(jsonPath("$[0].description").value(course.getDescription()));
    }

    @Test
    void testSaveUser_ShouldSaveUser() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        this.mockMvc.perform(post("/rest/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_ShouldDeleteUser() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        this.mockMvc.perform(post("/rest/users/1/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
