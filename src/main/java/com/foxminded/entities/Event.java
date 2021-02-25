package com.foxminded.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    @ManyToOne(fetch= FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch= FetchType.EAGER, targetEntity = Course.class)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @Column(name="start_time", nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime from;

    @Column(name="end_time", nullable=false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime to;

    public Event(Integer id, User user, Course course, LocalDateTime from, LocalDateTime to) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.from = from;
        this.to = to;
    }

    public Event(User user, Course course, LocalDateTime from, LocalDateTime to) {
        this(0, user, course, from, to);
    }

    public Event() { }

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
        return (o instanceof Event)
                && (((Event) o).getId().equals(this.id))
                && (((Event) o).getUser().equals(this.user))
                && (((Event) o).getCourse().equals(this.course))
                && (((Event) o).getFrom().equals(this.from))
                && (((Event) o).getTo().equals(this.to));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, from, to);
    }

}
