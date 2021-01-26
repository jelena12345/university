package com.foxminded.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class ActivityDto {

    private Integer id;
    private UserDto user;
    private CourseDto course;
    private Timestamp startTime;
    private Timestamp endTime;

    public ActivityDto(Integer id, UserDto user, CourseDto course, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ActivityDto(UserDto user, CourseDto course, Timestamp startTime, Timestamp endTime) {
        this(null, user, course, startTime, endTime);
    }

    private ActivityDto() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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
                && (((ActivityDto) o).getUser().equals(this.user))
                && (((ActivityDto) o).getCourse().equals(this.course))
                && (((ActivityDto) o).getStartTime().equals(this.startTime))
                && (((ActivityDto) o).getEndTime().equals(this.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, startTime, endTime);
    }

    @Override
    public String toString() {
        return "ActivityDto{" +
                "id=" + id +
                ", professor=" + user +
                ", course=" + course +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
