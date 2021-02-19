package com.foxminded.dao;

import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UserCourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findUsersForCourse(Course course) {
        return entityManager.find(Course.class, course.getId()).getUsersForCourse();
    }

    public List<Course> findCoursesForUser(User user) {
        return entityManager.find(User.class, user.getId()).getCoursesForUser();
    }

    public void add(User user, Course course) {
        user.getCoursesForUser().add(course);
        entityManager.merge(user);
    }

    public void delete(User user, Course course) {
        user.getCoursesForUser().remove(course);
        entityManager.merge(user);
    }

    public boolean existsCourseForUser(User user, Course course) {
        return entityManager.find(User.class, user.getId()).getCoursesForUser().contains(course);
    }
}
