package com.foxminded.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

public class UserDto implements Serializable {

    @NotBlank(message = "Personal id can't be blank.")
    private String personalId;

    @Pattern(regexp="^(student|professor)$",message = "Role should be student or professor.")
    @NotBlank(message = "Role can't be blank.")
    private String role;

    @Pattern(regexp="^[A-Za-z]*$", message = "User name should be in latin alphabet.")
    @NotBlank(message = "Name can't be blank.")
    private String name;

    @Pattern(regexp="^[A-Za-z]*$", message = "User surname should be in latin alphabet.")
    @NotBlank(message = "Surname can't be blank.")
    private String surname;

    @Pattern(regexp="^[A-Za-z0-9., ]*$", message = "User about text should be in latin alphabet.")
    private String about;

    public UserDto(String personalId, String role, String name, String surname, String about) {
        this.personalId = personalId;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.about = about;
    }

    public UserDto() { }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDto user = (UserDto) o;
        return personalId.equals(user.personalId) &&
                role.equals(user.role) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                about.equals((user.about));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.personalId, this.role, this.name, this.surname, this.about);
    }


    @Override
    public String toString() {
        return "ProfessorDto{" +
                "personalId='" + personalId + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
