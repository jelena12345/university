package com.foxminded.dto;

public class UserDto {

    protected String personalId;
    protected String name;
    protected String surname;

    protected UserDto(String personalId, String name, String surname) {
        this.personalId = personalId;
        this.name = name;
        this.surname = surname;
    }

    protected UserDto() { }

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
