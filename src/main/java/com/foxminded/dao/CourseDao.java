package com.foxminded.dao;

import com.foxminded.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CourseDao extends JpaRepository<Course, Integer> {

    Optional<Course> findByName(String name);

    void deleteByName(String name);

    boolean existsByName(String name);

}
