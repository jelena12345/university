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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;
    private final UserCourseService userCourseService;

    private static final String REDIRECT_COURSES = "redirect:/courses";
    private static final String DETAILS_VIEW = "courses/courseDetails";
    private static final String COURSES_VIEW = "courses/courses";
    private static final String STUDENTS_VIEW = "courses/students";

    private static final String COURSE = "course";
    private static final String USER = "user";
    private static final String MESSAGE = "message";

    @Autowired
    CourseController(CourseService service, UserCourseService userCourseService) {
        this.service = service;
        this.userCourseService = userCourseService;
    }

    @GetMapping()
    public String getCoursesView(Model model,
                          HttpSession session) {
        model.addAttribute(COURSE, new CourseDto());
        UserDto user = (UserDto)session.getAttribute(USER);
        model.addAttribute("courses", userCourseService.findCoursesForUser(user));
        model.addAttribute("availableCourses", userCourseService.findAvailableCoursesForUser(user));
        return COURSES_VIEW;
    }

    @GetMapping("/details")
    public String getDetailsView(Model model,
                                @ModelAttribute("course") CourseDto course) {
        model.addAttribute(COURSE, course);
        return DETAILS_VIEW;
    }

    @GetMapping("/students")
    public String getStudentsView(Model model,
                                  @Valid @ModelAttribute("course") CourseDto course,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return COURSES_VIEW;
        }
        model.addAttribute("students", userCourseService.findStudentsForCourse(course));
        return STUDENTS_VIEW;
    }

    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute(COURSE) CourseDto course,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return DETAILS_VIEW;
        }
        service.save(course);
        return REDIRECT_COURSES;
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute(COURSE) CourseDto course,
                      BindingResult bindingResult,
                      HttpSession session,
                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return COURSES_VIEW;
        }
        try {
            userCourseService.saveUserForCourse((UserDto)session.getAttribute(USER), course);
        } catch (EntityAlreadyExistsException e) {
            model.addAttribute(MESSAGE, "You are already on course.");
            return COURSES_VIEW;
        }
        return REDIRECT_COURSES;
    }

    @PostMapping("/remove")
    public String remove(@Valid @ModelAttribute("course") CourseDto course,
                         BindingResult bindingResult,
                         HttpSession session,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return COURSES_VIEW;
        } else {
            try {
                userCourseService.deleteUserForCourse((UserDto)session.getAttribute(USER), course);
            } catch (EntityNotFoundException e) {
                model.addAttribute(MESSAGE, "You are not on the course.");
                return COURSES_VIEW;
            }
        }
        return REDIRECT_COURSES;
    }

}
