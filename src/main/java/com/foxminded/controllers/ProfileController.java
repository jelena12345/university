package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService service;
    private static final String MESSAGE = "message";
    private static final String USER = "user";
    private static final String REDIRECT_PROFILE = "redirect:/profile";
    private static final String REDIRECT_INDEX = "redirect:/";

    @Autowired
    ProfileController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String profile(Model model,
                          HttpSession session) {
        if (session.getAttribute(USER) == null) {
            return REDIRECT_INDEX;
        }
        UserDto user = service.findByPersonalId(((UserDto)session.getAttribute(USER)).getPersonalId());
        model.addAttribute(USER, user);
        session.setAttribute(USER, user);
        return "user/profile";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute(USER) UserDto user,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
        } else {
            try {
                service.update(user);
            } catch (EntityNotFoundException e) {
                redirectAttributes.addFlashAttribute(MESSAGE, "User not found");
            }
        }
        return REDIRECT_PROFILE;
    }

    @PostMapping("/delete")
    public String deleteUser(HttpSession session,
                             @Valid @ModelAttribute(USER) UserDto user,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_PROFILE;
        }
        try {
            service.deleteByPersonalId(user.getPersonalId());
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute(MESSAGE, "User not found");
        }
        session.removeAttribute(USER);
        return REDIRECT_INDEX;
    }

}
