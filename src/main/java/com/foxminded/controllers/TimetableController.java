package com.foxminded.controllers;

import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.ActivityService;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                              @ModelAttribute("create_from") String from,
                              @ModelAttribute("create_to") String to) {
        event.setFrom(LocalDateTime.parse(from));
        event.setTo(LocalDateTime.parse(to));
        service.add(event);
        return "redirect:/timetable";
    }

    @PostMapping("/update")
    public String updateEvent(HttpSession session,
                              @ModelAttribute("event") ActivityDto event,
                              @ModelAttribute("update_from") String from,
                              @ModelAttribute("update_to") String to) {
        event.setCourse(courseService.findByName(event.getCourse().getName()));
        event.setUser((UserDto)session.getAttribute("user"));
        event.setFrom(LocalDateTime.parse(from));
        event.setTo(LocalDateTime.parse(to));
        service.update(event);
        return "redirect:/timetable";
    }

    @PostMapping("/delete")
    public String deleteEvent(@ModelAttribute("deleteId") String id) {

        service.deleteById(Integer.parseInt(id));
        return "redirect:/timetable";
    }
}
