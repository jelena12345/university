package com.foxminded.dto;

import javax.validation.Valid;

public class UserCourseDto {

    @Valid
    private UserDto userDto;
    @Valid
    private CourseDto courseDto;

    public UserCourseDto() { }

    public UserCourseDto(UserDto userDto, CourseDto courseDto) {
        this.userDto = userDto;
        this.courseDto = courseDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public CourseDto getCourseDto() {
        return courseDto;
    }

    public void setCourseDto(CourseDto courseDto) {
        this.courseDto = courseDto;
    }
}
