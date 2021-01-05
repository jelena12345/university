package com.foxminded.dto;

public class UserDto {
    protected String name;
    protected String surname;

    protected UserDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    protected UserDto() {}

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

}
