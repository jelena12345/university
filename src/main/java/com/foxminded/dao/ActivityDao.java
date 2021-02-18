package com.foxminded.dao;

import com.foxminded.entities.Activity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class ActivityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Activity> findAll() {
        return entityManager.createQuery("from Activity", Activity.class).getResultList();
    }

    public Activity findById(int id) {
        return entityManager.find(Activity.class, id);
    }

    public Integer add(Activity activity) {
        entityManager.persist(activity);
        entityManager.flush();
        return activity.getId();
    }

    public void update(Activity activity) {
        entityManager.merge(activity);
    }

    public void deleteById(int id) {
        entityManager.createQuery("delete from Activity where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public boolean existsById(int id) {
        return findById(id) != null;
    }
}
