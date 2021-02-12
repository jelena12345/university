package com.foxminded.controllers;

import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void testTimetablePage_WithoutFilter_ShouldReturnTimetablePage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        this.mockMvc.perform(get("/timetable")
                .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/timetable"))
                .andExpect(model().attributeExists("filter_from"))
                .andExpect(model().attributeExists("filter_to"))
                .andExpect(model().attributeExists("events"));
        verify(userCourseService, times(1)).findCoursesForUser(user);
    }

    @Test
    void testTimetablePage_WithFilter_ShouldReturnTimetablePage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        this.mockMvc.perform(get("/timetable")
                .sessionAttr("user", user)
                .flashAttr("filter_from", LocalDate.now().toString())
                .flashAttr("filter_to", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/timetable"))
                .andExpect(model().attributeExists("filter_from"))
                .andExpect(model().attributeExists("filter_to"))
                .andExpect(model().attributeExists("events"));
        verify(userCourseService, times(1)).findCoursesForUser(user);
    }

    @Test
    void testCreationPage_ShouldReturnNewEventPage() throws Exception {
        this.mockMvc.perform(get("/timetable/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/newEvent"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("event"));
        verify(courseService, times(1)).findAll();
    }

    @Test
    void testCreateEvent_ShouldRedirectToTimetablePage() throws Exception {
        ActivityDto activity = new ActivityDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now());
        this.mockMvc.perform(post("/timetable/new")
                .flashAttr("event", activity))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timetable"));
        verify(activityService, times(1)).add(activity);
    }

    @Test
    void testUpdatePage() throws Exception {
        int id = 1;
        when(activityService.findById(anyInt())).thenReturn(new ActivityDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now()));
        this.mockMvc.perform(get("/timetable/update")
                .flashAttr("updateId", id))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/updateEvent"))
                .andExpect(model().attributeExists("event"));
        verify(activityService, times(1)).findById(id);
    }

    @Test
    void testUpdateEvent_ShouldRedirectToTimetablePage() throws Exception {
        ActivityDto activity = new ActivityDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now());
        this.mockMvc.perform(post("/timetable/update")
                .flashAttr("event", activity))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timetable"));
        verify(activityService, times(1)).update(activity);
    }

    @Test
    void testDeleteEvent_ShouldRedirectToTimetablePage() throws Exception {
        ActivityDto activity = new ActivityDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now());
        this.mockMvc.perform(post("/timetable/delete")
                .flashAttr("event", activity))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timetable"));
        verify(activityService, times(1)).deleteById(activity.getId());
    }
}
