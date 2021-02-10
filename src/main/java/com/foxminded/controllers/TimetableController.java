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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
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
                        Timestamp.valueOf(from.atStartOfDay()),
                        Timestamp.valueOf(to.atTime(LocalTime.MAX))))
                .forEach(activities::addAll);
        model.addAttribute("filter_from", from);
        model.addAttribute("filter_to", to);
        model.addAttribute("events", activities);
        return "timetable/timetable";
    }

    @GetMapping("/new")
    public String creationPage(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "timetable/newEvent";
    }

    @GetMapping("/update")
    public String updatePage(Model model,
                             @ModelAttribute("updateId") int id) {
        model.addAttribute("event", service.findById(id));
        return "timetable/updateEvent";
    }

    @PostMapping("/new")
    public String createEvent(HttpSession session,
                              @ModelAttribute("courseName") String courseName,
                              @ModelAttribute("from") String from,
                              @ModelAttribute("to") String to) {

        service.add(new ActivityDto(0,
                (UserDto)session.getAttribute("user"),
                new CourseDto(courseName, ""),
                Timestamp.valueOf(LocalDateTime.parse(from)),
                Timestamp.valueOf(LocalDateTime.parse(to))));
        return "redirect:/timetable";
    }

    @PostMapping("/update")
    public String updateEvent(HttpSession session,
                              @ModelAttribute("event") ActivityDto event,
                              @ModelAttribute("from") String from,
                              @ModelAttribute("to") String to) {
        event.setCourse(courseService.findByName(event.getCourse().getName()));
        event.setUser((UserDto)session.getAttribute("user"));
        event.setStartTime(Timestamp.valueOf(LocalDateTime.parse(from)));
        event.setEndTime(Timestamp.valueOf(LocalDateTime.parse(to)));
        service.update(event);
        return "redirect:/timetable";
    }

    @PostMapping("/delete")
    public String deleteEvent(@ModelAttribute("deleteId") String id) {

        service.deleteById(Integer.parseInt(id));
        return "redirect:/timetable";
    }
}
