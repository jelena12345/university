package com.foxminded.controllers;

import com.foxminded.services.ActivityService;
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
class TimetableControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ActivityService activityService;
    @Mock
    private CourseService courseService;
    @Mock
    private UserCourseService userCourseService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new TimetableController(activityService, courseService, userCourseService)).build();
    }

    @Test
    void testTimetablePage() throws Exception {
        this.mockMvc.perform(get("/timetable"))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/timetable"));
    }

    @Test
    void testCreationPage() throws Exception {
        this.mockMvc.perform(get("/timetable/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/newEvent"));
    }

    @Test
    void testUpdatePage() throws Exception {
        this.mockMvc.perform(get("/timetable/update").flashAttr("updateId", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/updateEvent"));
    }
}
