package com.foxminded.services;

import com.foxminded.dao.CourseDao;
import com.foxminded.dao.UserDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.User;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCourseService {

    private final ModelMapper mapper;
    private final UserDao userDao;
    private final CourseDao courseDao;
    private static final Logger logger = LoggerFactory.getLogger(UserCourseService.class);

    @Autowired
    public UserCourseService(ModelMapper mapper, UserDao userDao, CourseDao courseDao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipUserIdFieldMap);
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.userDao = userDao;
        this.courseDao = courseDao;
    }

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

    public List<UserDto> findStudentsForCourse(CourseDto courseDto) {
        logger.debug("Searching students for CourseDto");
        logger.trace("Searching students for CourseDto: {}", courseDto);
        List<User> students = findCourse(courseDto).getUsersForCourse()
                .stream()
                .filter(user -> user.getRole().equals("student"))
                .collect(Collectors.toList());
        return mapper.map(students, new TypeToken<List<UserDto>>() {}.getType());
    }

    public List<CourseDto> findCoursesForUser(UserDto userDto) {
        logger.debug("Searching Courses for UserDto");
        logger.trace("Searching Courses for UserDto: {}", userDto);
        return mapper.map(findUser(userDto).getCoursesForUser(), new TypeToken<List<CourseDto>>() {}.getType());
    }

    public List<CourseDto> findAvailableCoursesForUser(UserDto userDto) {
        logger.debug("Searching available Courses for UserDto");
        logger.trace("Searching available Courses for UserDto: {}", userDto);
        List<Course> currentCourses = findUser(userDto).getCoursesForUser();
        return mapper.map(
                courseDao.findAll()
                        .stream()
                        .filter(course -> !currentCourses.contains(course))
                        .collect(Collectors.toList()),
                new TypeToken<List<CourseDto>>() {}.getType());
    }

    public void saveUserForCourse(UserDto userDto, CourseDto courseDto) {
        logger.debug("Adding UserDto for CourseDto");
        logger.trace("Adding UserDto: {} for CourseDto: {}", userDto, courseDto);
        User user = findUser(userDto);
        Course course = findCourse(courseDto);
        if (user.getCoursesForUser().contains(course)) {
            logger.warn("User {} for Course {} already exists.", userDto, courseDto);
            throw new EntityAlreadyExistsException(
                    "User " + userDto + " for Course " + courseDto + " already exists.");
        }
        user.getCoursesForUser().add(course);
        userDao.save(user);
    }

    public void deleteUserForCourse(UserDto userDto, CourseDto courseDto) {
        logger.debug("Deleting UserDto for CourseDto");
        logger.trace("Deleting UserDto: {} for CourseDto: {}", userDto, courseDto);
        User user = findUser(userDto);
        Course course = findCourse(courseDto);
        if (!user.getCoursesForUser().contains(course)) {
            logger.warn("Not found User : {} for Course {}", userDto, courseDto);
            throw new EntityNotFoundException("Not found User : " + userDto + " with Course: " + courseDto);
        }
        user.getCoursesForUser().remove(course);
        userDao.save(user);
    }

    private User findUser(UserDto userDto) {
        return userDao.findByPersonalId(userDto.getPersonalId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found User with personal id : " + userDto.getPersonalId()));
    }

    private Course findCourse(CourseDto courseDto) {
        return courseDao.findByName(courseDto.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Course with name: " + courseDto.getName()));
    }

}
