package com.foxminded.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class Activity {

    private Integer id;
    private Integer professorId;
    private Integer courseId;
    private Timestamp startTime;
    private Timestamp endTime;

    public Activity(Integer id, Integer professorId, Integer courseId, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.professorId = professorId;
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Activity(Integer professorId, Integer courseId, Timestamp startTime, Timestamp endTime) {
        this(null, professorId, courseId, startTime, endTime);
    }

    public Activity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
                && (((Activity) o).getProfessorId().equals(this.professorId))
                && (((Activity) o).getCourseId().equals(this.courseId))
                && (((Activity) o).getStartTime().equals(this.startTime))
                && (((Activity) o).getEndTime().equals(this.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, professorId, courseId, startTime, endTime);
    }

}
