package com.foxminded.controllers;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void testCoursesPage_ShouldReturnCoursesPage() throws Exception {
        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testNewCoursePage_ShouldReturnNewCoursePage() throws Exception {
        this.mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/addCourse"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("courses"));
    }

    @Test
    void testCreateCourse_ShouldRedirectToCourses() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        CourseDto course = new CourseDto("name", "description");
        this.mockMvc.perform(post("/courses/new")
                .sessionAttr("user", user)
                .flashAttr("course", course))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
        verify(courseService, times(1)).add(course);
        verify(userCourseService, times(1)).saveUserForCourse(user, course);
    }

    @Test
    void testAdd_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        CourseDto course = new CourseDto("name", "description");
        this.mockMvc.perform(post("/courses/add")
                .sessionAttr("user", user)
                .flashAttr("course", course))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
        verify(userCourseService, times(1)).saveUserForCourse(user, course);
    }

    @Test
    void testRemove_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        CourseDto course = new CourseDto("name", "description");
        when(courseService.findByName(anyString())).thenReturn(course);
        this.mockMvc.perform(post("/courses/remove")
                .sessionAttr("user", user)
                .flashAttr("course_remove", "name"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
        verify(userCourseService, times(1)).deleteUserForCourse(user, course);
    }

    @Test
    void testUpdateCoursePage_ShouldReturnUpdateCoursePage() throws Exception {
        CourseDto course = new CourseDto("name", "description");
        when(courseService.findByName(anyString())).thenReturn(course);
        this.mockMvc.perform(get("/courses/update")
                .flashAttr("updateName", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/updateCourse"))
                .andExpect(model().attributeExists("course"));
    }

    @Test
    void testUpdate_ShouldRedirectToCoursesPage() throws Exception {
        CourseDto course = new CourseDto("name", "description");
        this.mockMvc.perform(post("/courses/update")
                .flashAttr("course", course))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
        verify(courseService, times(1)).update(course);
    }

    @Test
    void testStudentsPage_ShouldReturnStudentsPage() throws Exception {
        this.mockMvc.perform(get("/courses/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/students"))
                .andExpect(model().attributeExists("students"));
    }

}
