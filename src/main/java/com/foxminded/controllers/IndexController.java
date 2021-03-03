package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService service;
    private static final String MESSAGE = "message";
    private static final String REDIRECT_INDEX = "redirect:/";
    private static final String REDIRECT_PROFILE = "redirect:/profile";

    @Autowired
    IndexController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String getIndexView(HttpSession session,
                               Model model) {
        if (session.getAttribute("user") != null) {
            return REDIRECT_PROFILE;
        }
        model.addAttribute("personalId", "");
        return "index";
    }

    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/registration";
    }

    @GetMapping("/signOut")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return REDIRECT_INDEX;
    }

    @PostMapping("/signIn")
    public String signIn(HttpSession session,
                         Model model,
                         @ModelAttribute("personalId") String personalId) {
        if (!service.existsByPersonalId(personalId)) {
            model.addAttribute(MESSAGE,
                    "User with personal id " + personalId + " not exists.");
            return "index";
        }
        session.setAttribute("user", service.findByPersonalId(personalId));
        return REDIRECT_PROFILE;
    }

    @PostMapping("/register")
    public String registerUser(Model model,
                               @Valid @ModelAttribute("user") UserDto user,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "/user/registration";
        }
        try {
            service.add(user);
        } catch (EntityAlreadyExistsException e) {
            model.addAttribute(MESSAGE,
                    "User with personal id " + user.getPersonalId() + " already exists.");
            return "/user/registration";
        }
        return REDIRECT_INDEX;
    }

}
