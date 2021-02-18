package com.foxminded.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name", nullable=false, unique=true)
    private String name;

    @Column(name="description", nullable=false)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "coursesForUser", cascade = CascadeType.REMOVE)
    List<User> usersForCourse;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="course")
    @OnDelete(action = OnDeleteAction.CASCADE)
    List<Activity> activities;

    public Course(String name, String description) {
        this(null, name, description);
    }

    public Course() { }

    public Course(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsersForCourse() {
        return usersForCourse;
    }

    public void setUsersForCourse(List<User> usersForCourse) {
        this.usersForCourse = usersForCourse;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(id, course.id) &&
                name.equals(course.name) &&
                description.equals(course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
