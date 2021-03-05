package com.foxminded.controllers.rest;

import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserCourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.services.CourseService;
import com.foxminded.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/courses")
public class CourseRestController {

    private final CourseService service;
    private final UserCourseService userCourseService;

    @Autowired
    CourseRestController(CourseService service, UserCourseService userCourseService) {
        this.service = service;
        this.userCourseService = userCourseService;
    }

    @GetMapping()
    public List<CourseDto> getCourses() {
        return service.findAll();
    }

    @GetMapping("/{name}/details")
    public CourseDto getCourseDetails(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @GetMapping("/{name}/students")
    public List<UserDto> getStudentsForCourse(@PathVariable("name") String name) {
        return userCourseService.findStudentsForCourse(service.findByName(name));
    }

    @PostMapping("/save")
    public void saveCourse(@Valid @RequestBody CourseDto course) {
        service.save(course);
    }

    @PostMapping("/add")
    public void addUserForCourse(@RequestBody @Valid UserCourseDto userCourse) {
        userCourseService.saveUserForCourse(userCourse.getUserDto(),
                userCourse.getCourseDto());
    }

    @PostMapping("/remove")
    public void deleteUserForCourse(@RequestBody @Valid UserCourseDto userCourse) {
        userCourseService.deleteUserForCourse(userCourse.getUserDto(),
                userCourse.getCourseDto());
    }
}
