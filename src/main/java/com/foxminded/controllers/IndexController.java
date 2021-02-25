package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService service;
    private static final String MESSAGE = "message";
    private static final String REDIRECT_INDEX = "redirect:/";
    private static final String REDIRECT_REGISTER = "redirect:/register";
    private static final String REDIRECT_PROFILE = "redirect:/profile";

    @Autowired
    IndexController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public String index(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return REDIRECT_PROFILE;
        }
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
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute("personalId")
                             @NotBlank(message = "Personal id can't be blank.")
                                     String personalId,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_INDEX;
        } else if (!service.existsByPersonalId(personalId)) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    "User with personal id " + personalId + " not exists.");
            return REDIRECT_INDEX;
        }
        session.setAttribute("user", service.findByPersonalId(personalId));
        return REDIRECT_PROFILE;
    }

    @PostMapping("/register")
    public String registerUser(RedirectAttributes redirectAttributes,
                               @Valid @ModelAttribute("user") UserDto user,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return REDIRECT_REGISTER;
        }
        try {
            service.add(user);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute(MESSAGE,
                    "User with personal id " + user.getPersonalId() + " not exists.");
            return REDIRECT_REGISTER;
        }
        return REDIRECT_INDEX;
    }

}
