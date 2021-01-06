package com.foxminded.dto;

import java.util.Objects;

public class StudentDto  extends UserDto {
    public StudentDto(String personalId, String name, String surname) {
        super(personalId, name, surname);
    }

    public StudentDto() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDto student = (StudentDto) o;
        return name.equals(student.name) &&
                surname.equals(student.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.surname);
    }
}
