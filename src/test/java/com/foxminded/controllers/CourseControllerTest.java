package com.foxminded.controllers;

import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CourseService courseService;
    @Mock
    private UserCourseService userCourseService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new CourseController(courseService, userCourseService)).build();
    }

    @Test
    void testCoursesPage() throws Exception {
        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"));
    }

    @Test
    void testAddCoursePage() throws Exception {
        this.mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/addCourse"));
    }

    @Test
    void testStudentsPage() throws Exception {
        this.mockMvc.perform(get("/courses/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/students"));
    }

    @Test
    void testUpdateCoursePage() throws Exception {
        this.mockMvc.perform(get("/courses/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/updateCourse"));
    }
}
