package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService service;

    @Autowired
    ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String profile(@ModelAttribute("user") UserDto professor) {
        return "profile";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") UserDto professor,
                           RedirectAttributes redirectAttributes) {
        service.update(professor);
        redirectAttributes.addFlashAttribute("user", professor);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") UserDto professor) {
        service.deleteByPersonalId(professor.getPersonalId());
        return "redirect:/";
    }

}
