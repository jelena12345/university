package com.foxminded.entities;

import javax.persistence.*;
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
