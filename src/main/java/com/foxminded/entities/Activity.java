package com.foxminded.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class Activity {

    private Integer id;
    private Professor professor;
    private Course course;
    private Timestamp startTime;
    private Timestamp endTime;

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

    private Activity() { }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        return (o instanceof Activity)
                && (((Activity) o).getId().equals(this.id))
                && (((Activity) o).getProfessor().equals(this.professor))
                && (((Activity) o).getCourse().equals(this.course))
                && (((Activity) o).getStartTime().equals(this.startTime))
                && (((Activity) o).getEndTime().equals(this.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, professor, course, startTime, endTime);
    }

}
