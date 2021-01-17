package com.foxminded.services;

import com.foxminded.dao.ActivityDao;
import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Activity;
import com.foxminded.entities.Course;
import com.foxminded.entities.Professor;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ModelMapper mapper;
    private final ActivityDao dao;
    private final Logger logger;

    PropertyMap<ProfessorDto, Professor> skipProfessorIdFieldMap = new PropertyMap<ProfessorDto, Professor>() {
        protected void configure() {
            skip().setId(null);
        }
    };
    PropertyMap<CourseDto, Course> skipCourseIdFieldMap = new PropertyMap<CourseDto, Course>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    @Autowired
    public ActivityService(ModelMapper mapper, ActivityDao dao, Logger logger) {
        this.mapper = mapper;
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.mapper.addMappings(skipProfessorIdFieldMap);
        this.dao = dao;
        this.logger = logger;
    }

    public List<ActivityDto> findAll() {
        logger.log(Level.FINE, "Searching for all ActivityDto records");
        List<Activity> activities = dao.findAll();
        return activities.stream().map(item -> mapper.map(item, ActivityDto.class)).collect(Collectors.toList());
    }

    public ActivityDto findById(int id) {
        logger.log(Level.FINE, "Searching for ActivityDto with id: {0}", id);
        return mapper.map(dao.findById(id), ActivityDto.class);
    }

    public void add(ActivityDto activity) {
        logger.log(Level.FINE, "Adding ActivityDto: {0}", activity);
        if (dao.existsById(activity.getId())) {
            throw new EntityAlreadyExistsException("Activity with id " + activity.getId() + " already exists.");
        }
        dao.add(mapper.map(activity, Activity.class));
    }

    public void update(ActivityDto activity) {
        logger.log(Level.FINE, "Updating ActivityDto: {0}", activity);
        if (!dao.existsById(activity.getId())) {
            throw new EntityNotFoundException("Not found Activity with id: " + activity.getId());
        }
        dao.update(mapper.map(activity, Activity.class));
    }

    public void deleteById(int id) {
        logger.log(Level.FINE, "Deleting Activity with id: {0}", id);
        if (!dao.existsById(id)) {
            throw new EntityNotFoundException("Not found Activity with id: " + id);
        }
        dao.deleteById(id);
    }

    public boolean existsById(int id) {
        logger.log(Level.FINE, "Checking if exists Activity with id: {0}", id);
        return dao.existsById(id);
    }

}
