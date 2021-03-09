package com.foxminded.controllers.rest;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.UserCourseService;
import com.foxminded.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserRestController {

    private final UserService service;
    private final UserCourseService userCourseService;

    @Autowired
    UserRestController(UserService service, UserCourseService userCourseService) {
        this.service = service;
        this.userCourseService = userCourseService;
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        return service.findAll();
    }

    @GetMapping("/{personalId}")
    public UserDto getUserByPersonalId(@PathVariable("personalId") String personalId) {
        return service.findByPersonalId(personalId);
    }

    @GetMapping("/{personalId}/available")
    public List<CourseDto> getAvailableCourses(@PathVariable("personalId") String personalId) {
        return userCourseService.findAvailableCoursesForUser(service.findByPersonalId(personalId));
    }

    @PostMapping("/register")
    public void saveUser(@Valid @RequestBody UserDto user) {
        service.add(user);
    }

    @PostMapping("/update")
    public void updateUser(@Valid @RequestBody UserDto user) {
        service.update(user);
    }

    @PostMapping("/{personalId}/delete")
    public void deleteUser(@PathVariable("personalId") String personalId) {
        service.deleteByPersonalId(personalId);
    }
}
