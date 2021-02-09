package com.foxminded.services;

import com.foxminded.dao.ActivityDao;
import com.foxminded.dao.CourseDao;
import com.foxminded.dao.UserDao;
import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Activity;
import com.foxminded.entities.Course;
import com.foxminded.entities.User;
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
    private final UserDao userDao;
    private final CourseDao courseDao;
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    PropertyMap<UserDto, User> skipUserIdFieldMap = new PropertyMap<UserDto, User>() {
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
    public ActivityService(ModelMapper mapper, ActivityDao dao, UserDao userDao, CourseDao courseDao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.mapper.addMappings(skipUserIdFieldMap);
        this.dao = dao;
        this.userDao = userDao;
        this.courseDao = courseDao;
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

    public void add(ActivityDto activityDto) {
        logger.debug("Adding ActivityDto");
        logger.trace("Adding ActivityDto: {}", activityDto);
        if (dao.existsById(activityDto.getId())) {
            logger.warn("Activity with id {} already exists.", activityDto.getId());
            throw new EntityAlreadyExistsException("Activity with id " + activityDto.getId() + " already exists.");
        }
        Activity activity = mapper.map(activityDto, Activity.class);
        activity.setUser(enrich(activity.getUser()));
        activity.setCourse(enrich(activity.getCourse()));
        dao.add(activity);
    }

    public void update(ActivityDto activityDto) {
        logger.debug("Updating ActivityDto");
        logger.trace("Updating ActivityDto: {}", activityDto);
        if (!dao.existsById(activityDto.getId())) {
            logger.warn("Not found Activity with id: {}", activityDto.getId());
            throw new EntityNotFoundException("Not found Activity with id: " + activityDto.getId());
        }
        Activity activity = mapper.map(activityDto, Activity.class);
        activity.setUser(enrich(activity.getUser()));
        activity.setCourse(enrich(activity.getCourse()));
        dao.update(activity);
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

    private User enrich(User user) {
        user.setId(userDao.findByPersonalId(user.getPersonalId()).getId());
        return user;
    }

    private Course enrich(Course course) {
        course.setId(courseDao.findByName(course.getName()).getId());
        return course;
    }
}
