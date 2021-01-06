package com.foxminded.entities;

import java.util.Objects;

public class Student extends User {

    public Student(Integer id, String personalId, String name, String surname) {
        super(id, personalId, name, surname);
    }

    public Student(String personalId, String name, String surname) {
        this(null, personalId, name, surname);
    }

    private Student() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                name.equals(student.name) &&
                surname.equals(student.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.surname);
    }

}
