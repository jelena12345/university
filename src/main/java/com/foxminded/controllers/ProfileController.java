package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService service;
    private static final String USER = "user";

    @Autowired
    ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String profile(Model model,
                          HttpSession session) {
        if (session.getAttribute(USER) == null) {
            return "redirect:/";
        }
        UserDto user = service.findByPersonalId(((UserDto)session.getAttribute(USER)).getPersonalId());
        model.addAttribute(USER, user);
        session.setAttribute(USER, user);
        return "user/profile";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute(USER) UserDto user) {
        service.update(user);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteUser(HttpSession session,
                             @Valid @ModelAttribute(USER) UserDto user) {
        service.deleteByPersonalId(user.getPersonalId());
        session.removeAttribute(USER);
        return "redirect:/";
    }

}
