package com.foxminded.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/test")
    public String testPage() {
        return "test";
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

}
