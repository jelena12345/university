package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService service;

    @Autowired
    ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String profile(Model model,
                          HttpSession session) {
        UserDto user = service.findByPersonalId(((UserDto)session.getAttribute("user")).getPersonalId());
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") UserDto user) {
        service.update(user);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") UserDto user) {
        service.deleteByPersonalId(user.getPersonalId());
        return "redirect:/";
    }

}
