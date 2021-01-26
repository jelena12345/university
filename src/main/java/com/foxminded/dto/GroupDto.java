package com.foxminded.dto;

import java.util.List;
import java.util.Objects;

public class GroupDto {
    private CourseDto course;
    private List<UserDto> users;

    public GroupDto(CourseDto course, List<UserDto> users) {
        this.course = course;
        this.users = users;
    }

    private GroupDto() { }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupDto groupDto = (GroupDto) o;
        return course.equals(groupDto.course) &&
                users.equals(groupDto.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, users);
    }

    @Override
    public String toString() {
        return "GroupDto{" +
                "course=" + course +
                ", students count=" + users.size() +
                '}';
    }
}
