package com.foxminded.entities;

import java.util.Objects;

public class Student extends User {

    public Student(Integer id, String name, String surname) {
        super(id, name, surname);
    }

    public Student(String name, String surname) {
        this(null, name, surname);
    }

    public Student() {}

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return (o instanceof Student)
                && (((Student) o).getId().equals(this.id))
                && (((Student) o).getName().equals(this.name))
                && (((Student) o).getSurname().equals(this.surname));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.surname);
    }

}
