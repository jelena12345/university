package com.foxminded.controllers;

import com.foxminded.dto.EventDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.EventService;
import com.foxminded.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Controller
@RequestMapping("/timetable")
public class TimetableController {

    private final EventService eventService;
    private final CourseService courseService;
    private static final String REDIRECT_TIMETABLE = "redirect:/timetable";
    private static final String NEW_VIEW = "timetable/newEvent";
    private static final String TIMETABLE_VIEW = "timetable/timetable";
    private static final String UPDATE_VIEW = "timetable/updateEvent";

    @Autowired
    TimetableController(EventService eventService, CourseService courseService) {
        this.eventService = eventService;
        this.courseService = courseService;
    }

    @GetMapping()
    public String timetable(Model model,
                            HttpSession session) {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusMonths(1);
        List<EventDto> events = eventService.findEventsForUserFromTo(
                (UserDto)session.getAttribute("user"),
                from.atStartOfDay(),
                to.atTime(LocalTime.MAX));
        model.addAttribute("filter_from", from);
        model.addAttribute("filter_to", to);
        model.addAttribute("events", events);
        return TIMETABLE_VIEW;
    }

    @PostMapping("/filter")
    public String filterEvents(Model model,
                            HttpSession session,
                            @ModelAttribute("filter_from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                            @ModelAttribute("filter_to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        List<EventDto> events = eventService.findEventsForUserFromTo(
                (UserDto)session.getAttribute("user"),
                from.atStartOfDay(),
                to.atTime(LocalTime.MAX));
        model.addAttribute("events", events);
        return TIMETABLE_VIEW;
    }

    @GetMapping("/new")
    public String getCreateView(Model model,
                               HttpSession session) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("event", new EventDto((UserDto)session.getAttribute("user"),
                new CourseDto("", ""),
                LocalDateTime.now().withNano(0).withSecond(0),
                LocalDateTime.now().plusHours(1).withNano(0).withSecond(0)));
        return NEW_VIEW;
    }

    @GetMapping("/update")
    public String getUpdateView(Model model,
                             @ModelAttribute("updateId") int id) {
        model.addAttribute("event", eventService.findById(id));
        return UPDATE_VIEW;
    }

    @PostMapping("/new")
    public String createEvent(@Valid @ModelAttribute("event") EventDto event) {
        eventService.add(event);
        return REDIRECT_TIMETABLE;
    }

    @PostMapping("/update")
    public String updateEvent(HttpSession session,
                              @Valid @ModelAttribute("event") EventDto event) {
        event.setCourse(courseService.findByName(event.getCourse().getName()));
        event.setUser((UserDto)session.getAttribute("user"));
        eventService.update(event);
        return REDIRECT_TIMETABLE;
    }

    @PostMapping("/delete")
    public String deleteEvent(@Valid @ModelAttribute("event") EventDto event) {
        eventService.deleteById(event.getId());
        return REDIRECT_TIMETABLE;
    }
}
