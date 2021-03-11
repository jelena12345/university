package com.foxminded.controllers;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@Transactional
class CourseControllerSystemTest {

    private static final String PERSONAL_ID = "1";
    private static final String ROLE = "student";
    private static final String USER_NAME = "name";
    private static final String SURNAME = "surname";
    private static final String ABOUT = "about";
    private static final String COURSE_NAME = "name";
    private static final String DESCRIPTION = "d";

    private static final String USER = "user";
    private static final String COURSE = "course";
    private static final String COURSES = "courses";
    private static final String AVAILABLE_COURSES = "availableCourses";

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetCoursesView_ShouldReturnCoursesView() throws Exception {
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        this.mockMvc.perform(get("/courses")
                .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"))
                .andExpect(model().attributeExists(COURSE))
                .andExpect(model().attributeExists(COURSES))
                .andExpect(model().attributeExists(AVAILABLE_COURSES));
    }

    @Test
    void testGetDetailsView_ShouldReturnDetailsView() throws Exception {
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        this.mockMvc.perform(get("/courses/details")
                .sessionAttr("user", user)
                .flashAttr(COURSE, course))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courseDetails"))
                .andExpect(model().attributeExists(COURSE));
    }

    @Test
    void testGetStudentsView_ValidInput_ShouldReturnStudentsView() throws Exception {
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        this.mockMvc.perform(get("/courses/students")
                .sessionAttr("user", user)
                .flashAttr(COURSE, course))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/students"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    void testGetStudentsView_InvalidInput_ShouldReturnCoursesView() throws Exception {
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(get("/courses/students")
                .flashAttr(COURSE, course))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSaveCourse_ValidInput_ShouldRedirectToCourses() throws Exception {
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        this.mockMvc.perform(post("/courses/save")
                .flashAttr(COURSE, course))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
    }

    @Test
    void testSaveCourse_InvalidInput_ShouldReturnDetailsView() throws Exception {
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(post("/courses/save")
                .flashAttr(COURSE, course))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAdd_ValidInput_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        CourseDto course = new CourseDto("name2", "description2");
        this.mockMvc.perform(post("/courses/add")
                .sessionAttr(USER, user)
                .flashAttr(COURSE, course))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
    }

    @Test
    void testAdd_InvalidInput_ShouldRedirectToCoursesPage() throws Exception {
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(post("/courses/add")
                .flashAttr(COURSE, course))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRemove_ValidInput_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        this.mockMvc.perform(post("/courses/remove")
                .sessionAttr(USER, user)
                .flashAttr(COURSE, course))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
    }

    @Test
    void testRemove_InvalidInput_ShouldRedirectToCoursesPage() throws Exception {
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(post("/courses/remove")
                .flashAttr(COURSE, course))
                .andExpect(status().isBadRequest());
    }
}
