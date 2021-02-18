package com.foxminded.dao;

import com.foxminded.entities.User;
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
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    public User findByPersonalId(String personalId) {
        try {
            return entityManager
                    .createQuery("SELECT u FROM User u WHERE u.personalId = :personalId", User.class)
                    .setParameter("personalId", personalId)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.error("Error trying to find User with id = {}", personalId, e);
            return null;
        }
    }

    public Integer add(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user.getId();
    }

    public void update(User user) {
        entityManager.merge(user);
    }

    public void deleteById(int id) {
        entityManager.createQuery("delete from User where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public void deleteByPersonalId(String personalId) {
        entityManager.createQuery("delete from User where personalId = :personalId")
                .setParameter("personalId", personalId)
                .executeUpdate();
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }

    public boolean existsByPersonalId(String personalId) {
        return findByPersonalId(personalId) != null;
    }
}
