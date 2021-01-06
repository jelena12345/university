package com.foxminded.entities;

import java.util.List;
import java.util.Objects;

public class Group {

    private Course course;
    private List<Student> students;

    public Group(Course course, List<Student> students) {
        this.course = course;
        this.students = students;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return course.equals(group.course) &&
                students.equals(group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, students);
    }
}
