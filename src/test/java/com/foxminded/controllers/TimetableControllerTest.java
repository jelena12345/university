package com.foxminded.controllers;

import com.foxminded.dto.EventDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.EventService;
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
    private EventService eventService;
    @Mock
    private CourseService courseService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new TimetableController(eventService, courseService)).build();
    }

    @Test
    void testTimetablePage_ShouldReturnTimetablePage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        this.mockMvc.perform(get("/timetable")
                .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/timetable"))
                .andExpect(model().attributeExists("filter_from"))
                .andExpect(model().attributeExists("filter_to"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void testFilter_ShouldReturnTimetablePage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusMonths(1);
        this.mockMvc.perform(get("/timetable")
                .sessionAttr("user", user)
                .flashAttr("filter_from", from)
                .flashAttr("filter_to", to))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/timetable"))
                .andExpect(model().attributeExists("filter_from"))
                .andExpect(model().attributeExists("filter_to"))
                .andExpect(model().attributeExists("events"));
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
    void testCreateEvent_ValidInput_ShouldRedirectToTimetablePage() throws Exception {
        EventDto eventDto = new EventDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        this.mockMvc.perform(post("/timetable/new")
                .flashAttr("event", eventDto))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timetable"));
        verify(eventService, times(1)).add(eventDto);
    }

    @Test
    void testCreateEvent_InvalidInput_ShouldReturnTimetablePage() throws Exception {
        EventDto eventDto = new EventDto();
        this.mockMvc.perform(post("/timetable/new")
                .flashAttr("event", eventDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePage_ShouldReturnUpdatePage() throws Exception {
        int id = 1;
        when(eventService.findById(anyInt())).thenReturn(new EventDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now()));
        this.mockMvc.perform(get("/timetable/update")
                .flashAttr("updateId", id))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/updateEvent"))
                .andExpect(model().attributeExists("event"));
        verify(eventService, times(1)).findById(id);
    }

    @Test
    void testUpdateEvent_ValidInput_ShouldRedirectToTimetablePage() throws Exception {
        EventDto eventDto = new EventDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        this.mockMvc.perform(post("/timetable/update")
                .flashAttr("event", eventDto))
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/timetable"));
        verify(eventService, times(1)).update(eventDto);
    }

    @Test
    void testUpdateEvent_InvalidInput_ShouldReturnUpdatePage() throws Exception {
        EventDto eventDto = new EventDto();
        this.mockMvc.perform(post("/timetable/update")
                .flashAttr("event", eventDto))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteEvent_ValidInput_ShouldRedirectToTimetablePage() throws Exception {
        EventDto eventDto = new EventDto(1,
                new UserDto(),
                new CourseDto("", ""),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        this.mockMvc.perform(post("/timetable/delete")
                .flashAttr("event", eventDto))
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/timetable"));
        verify(eventService, times(1)).deleteById(eventDto.getId());
    }

    @Test
    void testDeleteEvent_InvalidInput_ShouldRedirectToTimetablePage() throws Exception {
        EventDto eventDto = new EventDto();
        this.mockMvc.perform(post("/timetable/delete")
                .flashAttr("event", eventDto))
                .andExpect(status().isBadRequest());
    }
}
