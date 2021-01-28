package com.foxminded.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping()
    public String courses() {
        return "courses/courses";
    }

    @GetMapping("/new")
    public String creationPage() {
        return "courses/newCourse";
    }

    @GetMapping("/students")
    public String students() {
        return "courses/students";
    }

    @PostMapping("/new")
    public String createEvent() {
        return "redirect:/courses";
    }

    @PostMapping("/connect")
    public String connect() {
        return "redirect:/courses";
    }
}
