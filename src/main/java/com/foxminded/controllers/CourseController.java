package com.foxminded.controllers;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
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

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;
    private final UserCourseService userCourseService;

    @Autowired
    CourseController(CourseService service, UserCourseService userCourseService) {
        this.service = service;
        this.userCourseService = userCourseService;
    }

    @GetMapping()
    public String courses(Model model,
                          HttpSession session) {
        model.addAttribute("course", new CourseDto());
        model.addAttribute("courses",
                userCourseService.findCoursesForUser((UserDto)session.getAttribute("user")));
        return "courses/courses";
    }

    @GetMapping("/new")
    public String creationPage(Model model,
                               HttpSession session) {
        model.addAttribute("course", new CourseDto());
        model.addAttribute("courses",
                userCourseService.findAvailableCoursesForUser((UserDto)session.getAttribute("user")));
        return "courses/addCourse";
    }

    @GetMapping("/update")
    public String creationPage(Model model,
                               @ModelAttribute("updateName") String name) {
        model.addAttribute("course", service.findByName(name));
        return "courses/updateCourse";
    }

    @GetMapping("/students")
    public String students(Model model,
                           @ModelAttribute("course_users") String course) {
        model.addAttribute("students", userCourseService.findStudentsForCourse(service.findByName(course)));
        return "courses/students";
    }

    @PostMapping("/new")
    public String createCourse(@ModelAttribute("course") CourseDto course,
                               HttpSession session) {
        service.add(course);
        userCourseService.add((UserDto)session.getAttribute("user"), course);
        return "redirect:/courses";
    }

    @PostMapping("/connect")
    public String connect(@ModelAttribute("course") CourseDto course,
                          HttpSession session) {
        userCourseService.add((UserDto)session.getAttribute("user"), course);
        return "redirect:/courses";
    }

    @PostMapping("/disconnect")
    public String remove(@ModelAttribute("course_remove") String course,
                          HttpSession session) {
        userCourseService.delete((UserDto)session.getAttribute("user"), service.findByName(course));
        return "redirect:/courses";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("course") CourseDto course) {
        service.update(course);
        return "redirect:/courses";
    }
}