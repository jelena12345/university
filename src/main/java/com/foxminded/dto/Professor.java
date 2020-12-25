package com.foxminded.dto;

public class Professor extends User {

    String qualification;

    public Professor(String id, String name, String surname, String qualification) {
        super(id, name, surname);
        this.qualification = qualification;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

}
