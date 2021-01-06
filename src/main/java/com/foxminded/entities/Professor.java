package com.foxminded.entities;

import java.util.Objects;

public class Professor extends User {

    private String qualification;

    public Professor(Integer id, String personalId, String name, String surname, String qualification) {
        super(id, personalId, name, surname);
        this.qualification = qualification;
    }

    public Professor(String personalId, String name, String surname, String qualification) {
        this(null, personalId, name, surname, qualification);
    }

    public Professor() {}

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return Objects.equals(id, professor.id) &&
                name.equals(professor.name) &&
                surname.equals(professor.surname) &&
                qualification.equals(professor.qualification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.surname, this.qualification);
    }

}
