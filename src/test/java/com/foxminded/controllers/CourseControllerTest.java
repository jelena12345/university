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

    private static final String PERSONAL_ID = "1";
    private static final String ROLE = "student";
    private static final String USER_NAME = "name";
    private static final String SURNAME = "surname";
    private static final String ABOUT = "a";
    private static final String COURSE_NAME = "name";
    private static final String DESCRIPTION = "d";

    private static final String USER = "user";
    private static final String COURSE = "course";
    private static final String COURSES = "courses";
    private static final String AVAILABLE_COURSES = "availableCourses";

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new CourseController(courseService, userCourseService)).build();
    }

    @Test
    void testGetCoursesView_ShouldReturnCoursesView() throws Exception {
        this.mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"))
                .andExpect(model().attributeExists(COURSE))
                .andExpect(model().attributeExists(COURSES))
                .andExpect(model().attributeExists(AVAILABLE_COURSES));
    }

    @Test
    void testGetDetailsView_ShouldReturnDetailsView() throws Exception {
        this.mockMvc.perform(get("/courses/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courseDetails"))
                .andExpect(model().attributeExists(COURSE));
    }

    @Test
    void testGetStudentsView_ValidInput_ShouldReturnStudentsView() throws Exception {
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        this.mockMvc.perform(get("/courses/students")
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
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"))
                .andExpect(model().attributeHasFieldErrors(COURSE, "name", "description"));
    }

    @Test
    void testSaveCourse_ValidInput_ShouldRedirectToCourses() throws Exception {
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        this.mockMvc.perform(post("/courses/save")
                .flashAttr(COURSE, course))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
        verify(courseService, times(1)).save(course);
    }

    @Test
    void testSaveCourse_InvalidInput_ShouldReturnDetailsView() throws Exception {
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(post("/courses/save")
                .flashAttr(COURSE, course))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors(COURSE, "name", "description"))
                .andExpect(view().name("courses/courseDetails"));
    }

    @Test
    void testAdd_ValidInput_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        CourseDto course = new CourseDto(COURSE_NAME, DESCRIPTION);
        this.mockMvc.perform(post("/courses/add")
                .sessionAttr(USER, user)
                .flashAttr(COURSE, course))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/courses"));
        verify(userCourseService, times(1)).saveUserForCourse(user, course);
    }

    @Test
    void testAdd_InvalidInput_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(post("/courses/add")
                .sessionAttr(USER, user)
                .flashAttr(COURSE, course))
                .andExpect(model().attributeHasFieldErrors(COURSE, "name", "description"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"));
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
        verify(userCourseService, times(1)).deleteUserForCourse(user, course);
    }

    @Test
    void testRemove_InvalidInput_ShouldRedirectToCoursesPage() throws Exception {
        UserDto user = new UserDto(PERSONAL_ID, ROLE, USER_NAME, SURNAME, ABOUT);
        CourseDto course = new CourseDto("", "??");
        this.mockMvc.perform(post("/courses/remove")
                .sessionAttr(USER, user)
                .flashAttr(COURSE, course))
                .andExpect(model().attributeHasFieldErrors(COURSE, "name", "description"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses/courses"));
    }

}
