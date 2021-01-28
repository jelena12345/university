package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService service;

    @Autowired
    IndexController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String index(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/profile";
        }
        return "index";
    }

    @GetMapping("/register")
    public String registrationPage() {
        return "user/registration";
    }

    @GetMapping("/signOut")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @PostMapping("/signIn")
    public String signIn(HttpSession session,
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute("personalId")String personalId) {
        if (service.existsByPersonalId(personalId)) {
            session.setAttribute("user", service.findByPersonalId(personalId));
            return "redirect:/profile";
        } else {
            redirectAttributes.addFlashAttribute("personalId", personalId);
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto user) {
        if (service.existsByPersonalId(user.getPersonalId())) {
            return "redirect:user/registration";
        }
        service.add(user);
        return "redirect:/";
    }

}
