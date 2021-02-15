package com.foxminded.entities;

import java.util.Objects;

public class User {

    private Integer id;
    private String personalId;
    private String role;
    private String name;
    private String surname;
    private String about;

    public User(Integer id, String personalId, String role, String name, String surname, String about) {
        this.id = id;
        this.personalId = personalId;
        this.role = role;
        this.name = name;
        this.surname = surname;
        this.about = about;
    }

    public User(String personalId, String role, String name, String surname, String about) {
        this(null, personalId, role, name, surname, about);
    }

    private User() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                personalId.equals(user.personalId) &&
                role.equals(user.role) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                about.equals(user.about);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.personalId, this.role, this.name, this.surname, this.about);
    }
}
