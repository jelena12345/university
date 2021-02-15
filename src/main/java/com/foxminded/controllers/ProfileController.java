package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService service;
    private static final String MESSAGE = "message";

    @Autowired
    ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String profile(Model model,
                          HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }
        UserDto user = service.findByPersonalId(((UserDto)session.getAttribute("user")).getPersonalId());
        model.addAttribute("user", user);
        session.setAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") UserDto user,
                           RedirectAttributes redirectAttributes) {
        try {
            service.update(user);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "User not found");
        }
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteUser(HttpSession session,
                             @ModelAttribute("user") UserDto user,
                             RedirectAttributes redirectAttributes) {
        try {
            service.deleteByPersonalId(user.getPersonalId());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "User not found");
        }
        session.removeAttribute("user");
        return "redirect:/";
    }

}
