package com.foxminded.dto;

import java.util.List;
import java.util.Objects;

public class GroupDto {
    private CourseDto course;
    private List<StudentDto> students;

    public GroupDto(CourseDto course, List<StudentDto> students) {
        this.course = course;
        this.students = students;
    }

    public GroupDto() {}

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public List<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupDto groupDto = (GroupDto) o;
        return course.equals(groupDto.course) &&
                students.equals(groupDto.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, students);
    }
}
