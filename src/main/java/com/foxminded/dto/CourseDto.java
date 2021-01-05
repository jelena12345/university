package com.foxminded.dto;

import java.util.Objects;

public class CourseDto {

    private String name;
    private String description;

    public CourseDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CourseDto() {}

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
        return (o instanceof CourseDto)
                && (((CourseDto) o).getName().equals(this.name))
                && (((CourseDto) o).getDescription().equals(this.description));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
