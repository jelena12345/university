package com.foxminded.controllers;

import com.foxminded.dto.ProfessorDto;
import com.foxminded.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfessorService service;

    @Autowired
    ProfileController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping()
    public String profile(@ModelAttribute("professor") ProfessorDto professor) {
        return "professorProfile";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("professor") ProfessorDto professor,
                           RedirectAttributes redirectAttributes) {
        service.update(professor);
        redirectAttributes.addFlashAttribute("professor", professor);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("professor") ProfessorDto professor) {
        service.deleteByPersonalId(professor.getPersonalId());
        return "redirect:/";
    }

}
