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
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ModelMapper mapper;
    private final ActivityDao activityDao;
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
    public ActivityService(ModelMapper mapper, ActivityDao activityDao, UserDao userDao, CourseDao courseDao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.mapper.addMappings(skipUserIdFieldMap);
        this.activityDao = activityDao;
        this.userDao = userDao;
        this.courseDao = courseDao;
    }

    public List<ActivityDto> findAll() {
        logger.debug("Searching for all ActivityDto records");
        return activityDao.findAll().stream()
                .map(item -> mapper.map(item, ActivityDto.class))
                .sorted(Comparator.comparing(ActivityDto::getFrom))
                .collect(Collectors.toList());
    }

    public List<ActivityDto> findEventsForCourseFromTo(CourseDto courseDto, LocalDateTime from, LocalDateTime to) {
        logger.debug("Searching for ActivityDto records");
        logger.trace("Searching for ActivityDto records for CourseDto:{} from: {} to: {}", courseDto, from, to);
        Course course = courseDao.findByName(courseDto.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Course  with name: " + courseDto.getName()));
        return course.getActivities().stream()
                .filter(event -> event.getFrom().isAfter(from)
                        && event.getTo().isBefore(to))
                .map(event -> mapper.map(event, ActivityDto.class))
                .sorted(Comparator.comparing(ActivityDto::getFrom))
                .collect(Collectors.toList());
    }

    public ActivityDto findById(Integer id) {
        logger.debug("Searching for ActivityDto");
        logger.trace("Searching for ActivityDto with id: {}", id);
        return mapper.map(activityDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Activity  with id: " + id)),
                ActivityDto.class);
    }

    public void add(ActivityDto activityDto) {
        logger.debug("Adding ActivityDto");
        logger.trace("Adding ActivityDto: {}", activityDto);
        Activity activity = mapper.map(activityDto, Activity.class);
        activity.setUser(userDao.findByPersonalId(activityDto.getUser().getPersonalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found User with personal id : " + activityDto.getUser().getPersonalId())));
        activity.setCourse(courseDao.findByName(activityDto.getCourse().getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Course with name: " + activityDto.getCourse().getName())));
        activityDao.save(activity);
    }

    public void update(ActivityDto activityDto) {
        logger.debug("Updating ActivityDto");
        logger.trace("Updating ActivityDto: {}", activityDto);
        if (!activityDao.existsById(activityDto.getId())) {
            logger.warn("Not found Activity with id: {}", activityDto.getId());
            throw new EntityNotFoundException("Not found Activity with id: " + activityDto.getId());
        }
        Activity activity = mapper.map(activityDto, Activity.class);
        activity.setUser(enrich(activity.getUser()));
        activity.setCourse(enrich(activity.getCourse()));
        activityDao.save(activity);
    }

    public void deleteById(int id) {
        logger.debug("Deleting Activity");
        logger.trace("Deleting Activity with id: {}", id);
        if (!activityDao.existsById(id)) {
            logger.warn("Not found Activity with id: {}", id);
            throw new EntityNotFoundException("Not found Activity with id: " + id);
        }
        activityDao.deleteById(id);
    }

    private User enrich(User user) {
        User storedUser = userDao.findByPersonalId(user.getPersonalId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Not found User with personal id : " + user.getPersonalId()));
        user.setId(storedUser.getId());
        return user;
    }

    private Course enrich(Course course) {
        Course storedCourse = courseDao.findByName(course.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Not found Course with name: " + course.getName()));
        course.setId(storedCourse.getId());
        return course;
    }
}
