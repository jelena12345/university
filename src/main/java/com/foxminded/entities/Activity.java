package com.foxminded.entities;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class Activity {

    private Integer id;
    private User user;
    private Course course;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime from;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime to;

    public Activity(Integer id, User user, Course course, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.from = from;
        this.to = to;
    }

    public Activity(User user, Course course, LocalDateTime from, LocalDateTime to) {
        this(null, user, course, from, to);
    }

    private Activity() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
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
        return (o instanceof Activity)
                && (((Activity) o).getId().equals(this.id))
                && (((Activity) o).getUser().equals(this.user))
                && (((Activity) o).getCourse().equals(this.course))
                && (((Activity) o).getFrom().equals(this.from))
                && (((Activity) o).getTo().equals(this.to));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, from, to);
    }

}
