package com.foxminded.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Objects;

public class EventDto {

    @PositiveOrZero(message = "Id should be positive or zero.")
    private Integer id = 0;

    @NotNull(message = "User should not be null.")
    private UserDto user;

    @NotNull(message = "Course should not be null.")
    private CourseDto course;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime from;

    @FutureOrPresent(message = "Event end date/time should be in future or present.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime to;

    public EventDto(Integer id, UserDto user, CourseDto course, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.from = from;
        this.to = to;
    }

    public EventDto(UserDto user, CourseDto course, LocalDateTime from, LocalDateTime to) {
        this(0, user, course, from, to);
    }

    public EventDto() { }

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
        return (o instanceof EventDto)
                && (((EventDto) o).getId().equals(this.id))
                && (((EventDto) o).getUser().equals(this.user))
                && (((EventDto) o).getCourse().equals(this.course))
                && (((EventDto) o).getFrom().equals(this.from))
                && (((EventDto) o).getTo().equals(this.to));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, from, to);
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id=" + id +
                ", professor=" + user +
                ", course=" + course +
                ", startTime=" + from +
                ", endTime=" + to +
                '}';
    }
}
