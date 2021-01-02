package com.foxminded.dto;

import java.sql.Timestamp;
import java.util.List;

public class Activity {

    Integer id;
    Professor professor;
    Course course;
    Timestamp startTime;
    Timestamp endTime;
    List<Student> students;


    public Activity(Integer id, Professor professor, Course course, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.professor = professor;
        this.course = course;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Activity(Professor professor, Course course, Timestamp startTime, Timestamp endTime) {
        this(null, professor, course, startTime, endTime);
    }

    public Activity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp duration) {
        this.endTime = duration;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}
