package com.foxminded.controllers;

import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService service;

    @Autowired
    IndexController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String index() {
        return "index";
    }

    @PostMapping("signIn")
    public String signIn(RedirectAttributes redirectAttributes,
                         @ModelAttribute("personalId")String personalId) {
        redirectAttributes.addFlashAttribute("user", service.findByPersonalId(personalId));
        return "redirect:/profile";
    }

}
