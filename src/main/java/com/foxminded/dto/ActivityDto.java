package com.foxminded.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class ActivityDto {

    private Integer id;
    private ProfessorDto professor;
    private CourseDto course;
    private Timestamp startTime;
    private Timestamp endTime;

    public ActivityDto(Integer id, ProfessorDto professor, CourseDto course, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.professor = professor;
        this.course = course;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ActivityDto(ProfessorDto professor, CourseDto course, Timestamp startTime, Timestamp endTime) {
        this(null, professor, course, startTime, endTime);
    }

    private ActivityDto() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProfessorDto getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDto professor) {
        this.professor = professor;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
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
        return (o instanceof ActivityDto)
                && (((ActivityDto) o).getId().equals(this.id))
                && (((ActivityDto) o).getProfessor().equals(this.professor))
                && (((ActivityDto) o).getCourse().equals(this.course))
                && (((ActivityDto) o).getStartTime().equals(this.startTime))
                && (((ActivityDto) o).getEndTime().equals(this.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, professor, course, startTime, endTime);
    }

}
