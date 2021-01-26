package com.foxminded.entities;

import java.util.List;
import java.util.Objects;

public class Group {

    private Course course;
    private List<User> users;

    public Group(Course course, List<User> users) {
        this.course = course;
        this.users = users;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
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
        Group group = (Group) o;
        return course.equals(group.course) &&
                users.equals(group.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, users);
    }
}
