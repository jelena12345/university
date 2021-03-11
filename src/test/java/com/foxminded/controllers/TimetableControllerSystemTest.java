package com.foxminded.controllers;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.EventDto;
import com.foxminded.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class TimetableControllerSystemTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        this.mockMvc.perform(get("/timetable/new").sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/newEvent"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    void testCreateEvent_ValidInput_ShouldRedirectToTimetablePage() throws Exception {
        EventDto eventDto = new EventDto(1,
                new UserDto("1", "student", "name", "surname", "about"),
                new CourseDto("name", "description"),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        this.mockMvc.perform(post("/timetable/new")
                .flashAttr("event", eventDto))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timetable"));
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
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        int id = 2;
        this.mockMvc.perform(get("/timetable/update")
                .flashAttr("updateId", id)
                .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("timetable/updateEvent"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    void testUpdateEvent_ValidInput_ShouldRedirectToTimetablePage() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        EventDto eventDto = new EventDto(2,
                user,
                new CourseDto("name", "description"),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1));
        this.mockMvc.perform(post("/timetable/update")
                .flashAttr("event", eventDto)
                .sessionAttr("user", user))
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/timetable"));
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
    }

    @Test
    void testDeleteEvent_InvalidInput_ShouldRedirectToTimetablePage() throws Exception {
        EventDto eventDto = new EventDto();
        this.mockMvc.perform(post("/timetable/delete")
                .flashAttr("event", eventDto))
                .andExpect(status().isBadRequest());
    }
}
