package com.foxminded.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class ActivityDto {

    private Integer id;
    private UserDto user;
    private CourseDto course;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime from;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime to;

    public ActivityDto(Integer id, UserDto user, CourseDto course, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.from = from;
        this.to = to;
    }

    public ActivityDto(UserDto user, CourseDto course, LocalDateTime from, LocalDateTime to) {
        this(null, user, course, from, to);
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

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime duration) {
        this.to = duration;
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
                && (((ActivityDto) o).getFrom().equals(this.from))
                && (((ActivityDto) o).getTo().equals(this.to));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, from, to);
    }

    @Override
    public String toString() {
        return "ActivityDto{" +
                "id=" + id +
                ", professor=" + user +
                ", course=" + course +
                ", startTime=" + from +
                ", endTime=" + to +
                '}';
    }
}
