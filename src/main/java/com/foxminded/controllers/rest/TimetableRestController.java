package com.foxminded.controllers.rest;

import com.foxminded.dto.EventDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.EventService;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
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

    @GetMapping("/all")
    public List<EventDto> getAllEvents() {
        return service.findAll();
    }

    @GetMapping("/events/{id}")
    public EventDto getEventById(@PathVariable("id") Integer id) {
        return service.findById(id);
    }

    @GetMapping("{personalId}/filter/{from}_{to}")
    public List<EventDto> getFilteredEvents(@PathVariable("personalId") String personalId,
                                            @PathVariable("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                            @PathVariable("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return service.findEventsForUserFromTo(userService.findByPersonalId(personalId),
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
