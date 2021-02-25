package com.foxminded.dao;

import com.foxminded.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EventDao extends JpaRepository<Event, Integer> {

}
