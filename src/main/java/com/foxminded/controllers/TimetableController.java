package com.foxminded.controllers;

import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.ActivityService;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/timetable")
public class TimetableController {

    private final ActivityService service;
    private final CourseService courseService;
    private final UserCourseService userCourseService;
    private static final String MESSAGE = "message";

    @Autowired
    TimetableController(ActivityService service, CourseService courseService, UserCourseService userCourseService) {
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
        List<ActivityDto> activities = new ArrayList<>();
        userCourseService.findCoursesForUser((UserDto)session.getAttribute("user"))
                .stream()
                .map(course -> service.findEventsForCourseFromTo(course,
                        from.atStartOfDay(),
                        to.atTime(LocalTime.MAX)))
                .forEach(activities::addAll);
        model.addAttribute("filter_from", from);
        model.addAttribute("filter_to", to);
        model.addAttribute("events", activities);
        return "timetable/timetable";
    }

    @GetMapping("/new")
    public String creationPage(Model model,
                               HttpSession session) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("event", new ActivityDto((UserDto)session.getAttribute("user"),
                new CourseDto("", ""),
                LocalDateTime.now().withNano(0).withSecond(0),
                LocalDateTime.now().plusHours(1).withNano(0).withSecond(0)));
        return "timetable/newEvent";
    }

    @GetMapping("/update")
    public String updatePage(Model model,
                             @ModelAttribute("updateId") int id) {
        model.addAttribute("event", service.findById(id));
        return "timetable/updateEvent";
    }

    @PostMapping("/new")
    public String createEvent(@ModelAttribute("event") ActivityDto event,
                              RedirectAttributes redirectAttributes) {
        try {
            service.add(event);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Event already exists.");
            return "timetable/newEvent";
        }
        return "redirect:/timetable";
    }

    @PostMapping("/update")
    public String updateEvent(HttpSession session,
                              @ModelAttribute("event") ActivityDto event,
                              RedirectAttributes redirectAttributes) {
        event.setCourse(courseService.findByName(event.getCourse().getName()));
        event.setUser((UserDto)session.getAttribute("user"));
        try {
            service.update(event);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Event doesn't exists.");
        }
        return "redirect:/timetable";
    }

    @PostMapping("/delete")
    public String deleteEvent(@ModelAttribute("event") ActivityDto event,
                              RedirectAttributes redirectAttributes) {
        try {
            service.deleteById(event.getId());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Event doesn't exists.");
        }
        return "redirect:/timetable";
    }
}
