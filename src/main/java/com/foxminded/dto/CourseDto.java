package com.foxminded.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

public class CourseDto implements Serializable {

    @NotBlank(message = "Course name can't be blank.")
    @Pattern(regexp="^[A-Za-z0-9 ]*$", message = "Course name should be in latin alphabet.")
    private String name;

    @Pattern(regexp="^[A-Za-z0-9., ]*$", message = "Course description should be in latin alphabet.")
    private String description;

    public CourseDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CourseDto() { }

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

    @Override
    public String toString() {
        return "CourseDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
