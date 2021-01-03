package com.foxminded.dto;

import java.util.Objects;

public class Professor extends User {

    String qualification;

    public Professor(Integer id, String name, String surname, String qualification) {
        super(id, name, surname);
        this.qualification = qualification;
    }

    public Professor(String name, String surname, String qualification) {
        this(null, name, surname, qualification);
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
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return (o instanceof Professor)
                && (((Professor) o).getId().equals(this.id))
                && (((Professor) o).getName().equals(this.name))
                && (((Professor) o).getSurname().equals(this.surname))
                && (((Professor) o).getQualification().equals(this.qualification));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.surname, this.qualification);
    }

}
