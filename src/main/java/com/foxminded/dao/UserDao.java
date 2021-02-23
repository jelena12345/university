package com.foxminded.dao;

import com.foxminded.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    Optional<User> findByPersonalId(String personalId);

    boolean existsByPersonalId(String personalId);

    @Transactional
    @Modifying
    @Query("delete from User u where u.personalId = :personalId")
    void deleteByPersonalId(@Param("personalId") String personalId);

}
