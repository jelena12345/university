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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ModelMapper mapper;
    private final ActivityDao dao;
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

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
    public ActivityService(ModelMapper mapper, ActivityDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.mapper.addMappings(skipProfessorIdFieldMap);
        this.dao = dao;
    }

    public List<ActivityDto> findAll() {
        logger.debug("Searching for all ActivityDto records");
        List<Activity> activities = dao.findAll();
        return activities.stream().map(item -> mapper.map(item, ActivityDto.class)).collect(Collectors.toList());
    }

    public ActivityDto findById(int id) {
        logger.debug("Searching for ActivityDto");
        logger.trace("Searching for ActivityDto with id: {}", id);
        return mapper.map(dao.findById(id), ActivityDto.class);
    }

    public void add(ActivityDto activity) {
        logger.debug("Adding ActivityDto");
        logger.trace("Adding ActivityDto: {}", activity);
        if (dao.existsById(activity.getId())) {
            logger.warn("Activity with id {} already exists.", activity.getId());
            throw new EntityAlreadyExistsException("Activity with id " + activity.getId() + " already exists.");
        }
        dao.add(mapper.map(activity, Activity.class));
    }

    public void update(ActivityDto activity) {
        logger.debug("Updating ActivityDto");
        logger.trace("Updating ActivityDto: {}", activity);
        if (!dao.existsById(activity.getId())) {
            logger.warn("Not found Activity with id: {}", activity.getId());
            throw new EntityNotFoundException("Not found Activity with id: " + activity.getId());
        }
        dao.update(mapper.map(activity, Activity.class));
    }

    public void deleteById(int id) {
        logger.debug("Deleting Activity");
        logger.trace("Deleting Activity with id: {}", id);
        if (!dao.existsById(id)) {
            logger.warn("Not found Activity with id: {}", id);
            throw new EntityNotFoundException("Not found Activity with id: " + id);
        }
        dao.deleteById(id);
    }

    public boolean existsById(int id) {
        logger.debug("Checking if Activity exists");
        logger.trace("Checking if Activity exists with id: {}", id);
        return dao.existsById(id);
    }

}
