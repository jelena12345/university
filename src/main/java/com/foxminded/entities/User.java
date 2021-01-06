package com.foxminded.entities;

public abstract class User {
    protected Integer id;
    protected String personalId;
    protected String name;
    protected String surname;

    protected User(Integer id, String personalId, String name, String surname) {
        this.id = id;
        this.personalId = personalId;
        this.name = name;
        this.surname = surname;
    }

    protected User() { }

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
}
