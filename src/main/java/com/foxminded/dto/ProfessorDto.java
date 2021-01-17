package com.foxminded.dto;

import java.util.Objects;

public class ProfessorDto extends UserDto {

    private String qualification;

    public ProfessorDto(String personalId, String name, String surname, String qualification) {
        super(personalId, name, surname);
        this.qualification = qualification;
    }

    private ProfessorDto() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfessorDto professor = (ProfessorDto) o;
        return name.equals(professor.name) &&
                surname.equals(professor.surname) &&
                qualification.equals((professor.qualification));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.surname, this.qualification);
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public String toString() {
        return "ProfessorDto{" +
                "personalId='" + personalId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
