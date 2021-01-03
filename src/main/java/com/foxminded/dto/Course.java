package com.foxminded.dto;

import java.util.Objects;

public class Course {
    Integer id;
    String name;
    String description;

    public Course(String name, String description) {
        this(null, name, description);
    }

    public Course() {
        this(null, null, null);
    }

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
        if (o == null) {
            return false;
        }
        return (o instanceof Course)
                && (((Course) o).getId().equals(this.id))
                && (((Course) o).getName().equals(this.name))
                && (((Course) o).getDescription().equals(this.description));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
