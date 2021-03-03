package com.foxminded.controllers;

import com.foxminded.dto.EventDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.EventService;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/timetable")
public class TimetableController {

    private final EventService service;
    private final CourseService courseService;
    private final UserCourseService userCourseService;
    private static final String MESSAGE = "message";
    private static final String REDIRECT_TIMETABLE = "redirect:/timetable";
    private static final String NEW_VIEW = "timetable/newEvent";
    private static final String TIMETABLE_VIEW = "timetable/timetable";
    private static final String UPDATE_VIEW = "timetable/updateEvent";

    @Autowired
    TimetableController(EventService service, CourseService courseService, UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
        this.service = service;
        this.courseService = courseService;
    }

    @GetMapping()
    public String timetable(Model model,
                            HttpSession session,
                            @ModelAttribute("filter_from") String fromStr,
                            @ModelAttribute("filter_to") String toStr) {
        LocalDate from;
        LocalDate to;
        if (fromStr.isEmpty()) {
            from = LocalDate.now();
            to = LocalDate.now().plusMonths(1);
        } else {
            from = LocalDate.parse(fromStr);
            to = LocalDate.parse(toStr);
        }
        List<EventDto> events = new ArrayList<>();
        userCourseService.findCoursesForUser((UserDto)session.getAttribute("user"))
                .stream()
                .map(course -> service.findEventsForCourseFromTo(course,
                        from.atStartOfDay(),
                        to.atTime(LocalTime.MAX)))
                .forEach(events::addAll);
        model.addAttribute("filter_from", from);
        model.addAttribute("filter_to", to);
        model.addAttribute("events", events);
        return TIMETABLE_VIEW;
    }

    @GetMapping("/new")
    public String creationPage(Model model,
                               HttpSession session) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("event", new EventDto((UserDto)session.getAttribute("user"),
                new CourseDto("", ""),
                LocalDateTime.now().withNano(0).withSecond(0),
                LocalDateTime.now().plusHours(1).withNano(0).withSecond(0)));
        return NEW_VIEW;
    }

    @GetMapping("/update")
    public String updatePage(Model model,
                             @ModelAttribute("updateId") int id) {
        model.addAttribute("event", service.findById(id));
        return UPDATE_VIEW;
    }

    @PostMapping("/new")
    public String createEvent(@Valid @ModelAttribute("event") EventDto event,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return NEW_VIEW;
        }
        try {
            service.add(event);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Event already exists.");
            return NEW_VIEW;
        }
        return REDIRECT_TIMETABLE;
    }

    @PostMapping("/update")
    public String updateEvent(HttpSession session,
                              @Valid @ModelAttribute("event") EventDto event,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return UPDATE_VIEW;
        }
        event.setCourse(courseService.findByName(event.getCourse().getName()));
        event.setUser((UserDto)session.getAttribute("user"));
        try {
            service.update(event);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Event doesn't exists.");
            return UPDATE_VIEW;
        }
        return REDIRECT_TIMETABLE;
    }

    @PostMapping("/delete")
    public String deleteEvent(@Valid @ModelAttribute("event") EventDto event,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return TIMETABLE_VIEW;
        }
        try {
            service.deleteById(event.getId());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Event doesn't exists.");
        }
        return REDIRECT_TIMETABLE;
    }
}
