package com.foxminded.dao;

import com.foxminded.entities.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class CourseDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(CourseDao.class);

    public List<Course> findAll() {
        return entityManager.createQuery("from Course", Course.class).getResultList();
    }

    public Course findById(int id) {
        return entityManager.find(Course.class, id);
    }

    public Course findByName(String name) {
        try {
            return entityManager
                    .createQuery("SELECT c FROM Course c WHERE c.name = :name", Course.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.error("Error trying to find Course with name = {}", name, e);
            return null;
        }
    }

    public Integer add(Course course) {
        entityManager.persist(course);
        entityManager.flush();
        return course.getId();
    }

    public void update(Course course) {
        entityManager.merge(course);
    }

    public void deleteById(int id) {
        entityManager.createQuery("delete from Course where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public void deleteByName(String name) {
        entityManager.createQuery("delete from Course where name = :name")
                .setParameter("name", name)
                .executeUpdate();
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }

    public boolean existsByName(String name) {
        return findByName(name) != null;
    }
}
