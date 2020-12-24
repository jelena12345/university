package com.foxminded.dto;

import java.sql.Timestamp;

public class Activity {

    Professor professor;
    Group group;
    Timestamp startTime;
    Integer duration;

    public Activity(Professor professor, Group group, Timestamp startTime, Integer duration) {
        this.professor = professor;
        this.group = group;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

}
