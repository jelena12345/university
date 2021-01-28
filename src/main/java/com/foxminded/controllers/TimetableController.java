package com.foxminded.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/timetable")
public class TimetableController {

    @GetMapping()
    public String timetable() {
        return "timetable/timetable";
    }

    @GetMapping("/new")
    public String creationPage() {
        return "timetable/newEvent";
    }

    @PostMapping("/new")
    public String createEvent() {
        return "redirect:/timetable";
    }
}
