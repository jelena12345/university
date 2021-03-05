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
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new CourseRestController(courseService, userCourseService)).build();
    }

    @Test
    void testGetCourses_ShouldReturnCourses() throws Exception {
        List<CourseDto> courses = Collections.singletonList(
                new CourseDto("a", "b"));
        when(courseService.findAll()).thenReturn(courses);
        this.mockMvc.perform(get("/rest/courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("a"))
                .andExpect(jsonPath("$[0].description").value("b"));
    }

    @Test
    void testGetCourseDetails_ShouldReturnCourseDetails() throws Exception {
        CourseDto course = new CourseDto("a", "b");
        when(courseService.findByName(anyString())).thenReturn(course);
        this.mockMvc.perform(get("/rest/courses/a/details")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("a"))
                .andExpect(jsonPath("$.description").value("b"));
    }

    @Test
    void testGetStudentsForCourse_ShouldReturnStudentsForCourse() throws Exception {
        List<UserDto> students = Collections.singletonList(
                new UserDto("1", "student", "n", "s", "a"));
        when(userCourseService.findStudentsForCourse(any())).thenReturn(students);
        this.mockMvc.perform(get("/rest/courses/n/students")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalId").value("1"))
                .andExpect(jsonPath("$[0].role").value("student"))
                .andExpect(jsonPath("$[0].name").value("n"))
                .andExpect(jsonPath("$[0].surname").value("s"))
                .andExpect(jsonPath("$[0].about").value("a"));
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
