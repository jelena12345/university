package com.foxminded.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserCourseDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class CourseRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetCourses_ShouldReturnCourses() throws Exception {
        this.mockMvc.perform(get("/rest/courses")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    void testGetCourseDetails_ShouldReturnCourseDetails() throws Exception {
        this.mockMvc.perform(get("/rest/courses/name/details")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"));
    }

    @Test
    void testGetStudentsForCourse_ShouldReturnStudentsForCourse() throws Exception {
        this.mockMvc.perform(get("/rest/courses/name/students")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalId").value("1"))
                .andExpect(jsonPath("$[0].role").value("student"))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].surname").value("surname"))
                .andExpect(jsonPath("$[0].about").value("about"));
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
        UserDto user = new UserDto("2", "student", "name", "surname", "about");
        CourseDto course = new CourseDto("name", "description");
        UserCourseDto userCourse = new UserCourseDto(user, course);
        this.mockMvc.perform(post("/rest/courses/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourse)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemovedUserForCourse_ShouldRemoveUserForCourse() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        CourseDto course = new CourseDto("name", "description");
        UserCourseDto userCourse = new UserCourseDto(user, course);
        this.mockMvc.perform(post("/rest/courses/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCourse)))
                .andExpect(status().isOk());
    }
}
