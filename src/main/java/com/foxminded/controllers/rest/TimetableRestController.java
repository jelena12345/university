package com.foxminded.controllers.rest;

import com.foxminded.dto.EventDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.EventService;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/rest/timetable")
public class TimetableRestController {

    private final EventService service;
    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    TimetableRestController(EventService service, CourseService courseService, UserService userService) {
        this.service = service;
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/timetable")
    public List<EventDto> getTimetableForUser(@Valid @RequestBody UserDto userDto) {
        return service.findEventsForUserFromTo(userDto,
                LocalDate.now().atStartOfDay(),
                LocalDate.now().plusMonths(1).atTime(LocalTime.MAX));
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable("id") Integer id) {
        return service.findById(id);
    }

    @PostMapping("/timetable/filter")
    public List<EventDto> filterEvents(@Valid @RequestBody UserDto userDto,
                                       @RequestBody LocalDate from,
                                       @RequestBody LocalDate to) {
        return service.findEventsForUserFromTo(userDto,
                from.atStartOfDay(),
                to.atTime(LocalTime.MAX));
    }

    @PostMapping("/add")
    public void addEvent(@Valid @RequestBody EventDto event) {
        service.add(event);
    }

    @PostMapping("/update")
    public void updateEvent(@Valid @RequestBody EventDto event) {
        event.setCourse(courseService.findByName(event.getCourse().getName()));
        event.setUser(userService.findByPersonalId(event.getUser().getPersonalId()));
        service.update(event);
        service.add(event);
    }

    @PostMapping("/{id}/delete")
    public void deleteEvent(@PathVariable("id") Integer id) {
        service.deleteById(id);
    }
}
