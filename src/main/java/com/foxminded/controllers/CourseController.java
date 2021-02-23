package com.foxminded.controllers;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;
    private final UserCourseService userCourseService;
    private static final String REDIRECT_COURSES = "redirect:/courses";
    private static final String COURSE = "course";
    private static final String USER = "user";
    private static final String MESSAGE = "message";

    @Autowired
    CourseController(CourseService service, UserCourseService userCourseService) {
        this.service = service;
        this.userCourseService = userCourseService;
    }

    @GetMapping()
    public String courses(Model model,
                          HttpSession session) {
        model.addAttribute(COURSE, new CourseDto());
        model.addAttribute("courses",
                userCourseService.findCoursesForUser((UserDto)session.getAttribute(USER)));
        return "courses/courses";
    }

    @GetMapping("/new")
    public String newCoursePage(Model model,
                               HttpSession session) {
        model.addAttribute(COURSE, new CourseDto());
        model.addAttribute("courses",
                userCourseService.findAvailableCoursesForUser((UserDto)session.getAttribute(USER)));
        return "courses/addCourse";
    }

    @GetMapping("/update")
    public String updateCoursePage(Model model,
                               @ModelAttribute("updateName") String name) {
        model.addAttribute(COURSE, service.findByName(name));
        return "courses/updateCourse";
    }

    @GetMapping("/students")
    public String students(Model model,
                           @ModelAttribute("course_users") String course) {
        model.addAttribute("students", userCourseService.findStudentsForCourse(service.findByName(course)));
        return "courses/students";
    }

    @PostMapping("/new")
    public String createCourse(@ModelAttribute(COURSE) CourseDto course,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            service.add(course);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Course already exists.");
        }
        try {
            userCourseService.saveUserForCourse((UserDto)session.getAttribute(USER), course);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "User already on course.");
        }
        return REDIRECT_COURSES;
    }

    @PostMapping("/add")
    public String add(@ModelAttribute(COURSE) CourseDto course,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        try {
            userCourseService.saveUserForCourse((UserDto)session.getAttribute(USER), course);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "User already on course.");
        }
        return REDIRECT_COURSES;
    }

    @PostMapping("/remove")
    public String remove(@ModelAttribute("course_remove") String course,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        try {
            userCourseService.deleteUserForCourse((UserDto)session.getAttribute(USER), service.findByName(course));
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "User not on the course.");
        }
        return REDIRECT_COURSES;
    }

    @PostMapping("/update")
    public String update(@ModelAttribute(COURSE) CourseDto course,
                         RedirectAttributes redirectAttributes) {
        try {
            service.update(course);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "Course not found");
        }
        return REDIRECT_COURSES;
    }
}
