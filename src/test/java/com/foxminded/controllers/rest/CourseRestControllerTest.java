package com.foxminded.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserCourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class CourseRestControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CourseService courseService;
    @Mock
    private UserCourseService userCourseService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new CourseRestController(courseService, userCourseService)).build();
    }

    @Test
    void testGetCourses_ShouldReturnCourses() throws Exception {
        CourseDto course = new CourseDto("a", "b");
        List<CourseDto> courses = Collections.singletonList(course);
        when(courseService.findAll()).thenReturn(courses);
        this.mockMvc.perform(get("/rest/courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(course.getName()))
                .andExpect(jsonPath("$[0].description").value(course.getDescription()));
    }

    @Test
    void testGetCourseDetails_ShouldReturnCourseDetails() throws Exception {
        CourseDto course = new CourseDto("a", "b");
        when(courseService.findByName(anyString())).thenReturn(course);
        this.mockMvc.perform(get("/rest/courses/a/details")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(jsonPath("$.description").value(course.getDescription()));
    }

    @Test
    void testGetStudentsForCourse_ShouldReturnStudentsForCourse() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        List<UserDto> students = Collections.singletonList(user);
        when(userCourseService.findStudentsForCourse(any())).thenReturn(students);
        this.mockMvc.perform(get("/rest/courses/n/students")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalId").value(user.getPersonalId()))
                .andExpect(jsonPath("$[0].role").value(user.getRole()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].surname").value(user.getSurname()))
                .andExpect(jsonPath("$[0].about").value(user.getAbout()));
    }

    @Test
    void testSaveCourse_ShouldSaveCourse() throws Exception {
        CourseDto course = new CourseDto("a", "b");
        this.mockMvc.perform(post("/rest/courses/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddUserForCourse_ShouldAddUserForCourse() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        UserCourseDto userCourse = new UserCourseDto(user, course);
        this.mockMvc.perform(post("/rest/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourse)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemovedUserForCourse_ShouldRemoveUserForCourse() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        CourseDto course = new CourseDto("a", "b");
        UserCourseDto userCourse = new UserCourseDto(user, course);
        this.mockMvc.perform(post("/rest/courses/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourse)))
                .andExpect(status().isOk());
    }
}
