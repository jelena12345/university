package com.foxminded.services;

import com.foxminded.dao.EventDao;
import com.foxminded.dao.CourseDao;
import com.foxminded.dao.UserDao;
import com.foxminded.dto.EventDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Event;
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
public class EventService {

    private final ModelMapper mapper;
    private final EventDao eventDao;
    private final UserDao userDao;
    private final CourseDao courseDao;
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

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
    public EventService(ModelMapper mapper, EventDao eventDao, UserDao userDao, CourseDao courseDao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.mapper.addMappings(skipUserIdFieldMap);
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.courseDao = courseDao;
    }

    public List<EventDto> findAll() {
        logger.debug("Searching for all EventDto records");
        return eventDao.findAll().stream()
                .map(item -> mapper.map(item, EventDto.class))
                .sorted(Comparator.comparing(EventDto::getFrom))
                .collect(Collectors.toList());
    }

    public List<EventDto> findEventsForUserFromTo(UserDto userDto, LocalDateTime from, LocalDateTime to) {
        logger.debug("Searching for EventDto records");
        logger.trace("Searching for EventDto records for UserDto:{} from: {} to: {}", userDto, from, to);
        User user = userDao.findByPersonalId(userDto.getPersonalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found User  with personal id: " + userDto.getPersonalId()));
        return user.getCoursesForUser()
                .stream()
                .flatMap(c -> c.getEvents().stream())
                .filter(event -> event.getFrom().isAfter(from)
                        && event.getTo().isBefore(to))
                .map(event -> mapper.map(event, EventDto.class))
                .collect(Collectors.toList());
    }

    public EventDto findById(Integer id) {
        logger.debug("Searching for EventDto");
        logger.trace("Searching for EventDto with id: {}", id);
        return mapper.map(eventDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found Event  with id: " + id)),
                EventDto.class);
    }

    public void add(EventDto eventDto) {
        logger.debug("Adding EventDto");
        logger.trace("Adding EventDto: {}", eventDto);
        Event event = mapper.map(eventDto, Event.class);
        event.setUser(userDao.findByPersonalId(eventDto.getUser().getPersonalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found User with personal id : " + eventDto.getUser().getPersonalId())));
        event.setCourse(courseDao.findByName(eventDto.getCourse().getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Course with name: " + eventDto.getCourse().getName())));
        eventDao.save(event);
    }

    public void update(EventDto eventDto) {
        logger.debug("Updating EventDto");
        logger.trace("Updating EventDto: {}", eventDto);
        if (!eventDao.existsById(eventDto.getId())) {
            logger.warn("Not found Event with id: {}", eventDto.getId());
            throw new EntityNotFoundException("Not found Event with id: " + eventDto.getId());
        }
        Event event = mapper.map(eventDto, Event.class);
        event.setUser(enrich(event.getUser()));
        event.setCourse(enrich(event.getCourse()));
        eventDao.save(event);
    }

    public void deleteById(int id) {
        logger.debug("Deleting Event");
        logger.trace("Deleting Event with id: {}", id);
        if (!eventDao.existsById(id)) {
            logger.warn("Not found Event with id: {}", id);
            throw new EntityNotFoundException("Not found Event with id: " + id);
        }
        eventDao.deleteById(id);
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
