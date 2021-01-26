package com.foxminded.services;

import com.foxminded.dao.GroupDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
import com.foxminded.entities.User;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final ModelMapper mapper;
    private final GroupDao dao;
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Autowired
    public GroupService(ModelMapper mapper, GroupDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<UserDto, User> skipIdFieldMap = new PropertyMap<UserDto, User>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public GroupDto findGroupForCourse(CourseDto courseDto) {
        logger.debug("Searching Group for CourseDto");
        logger.trace("Searching Group for CourseDto: {}", courseDto);
        Group group =  dao.findGroupForCourse(mapper.map(courseDto, Course.class));
        return mapper.map(group, GroupDto.class);
    }

    public void add(UserDto userDto, CourseDto courseDto) {
        logger.debug("Adding UserDto for CourseDto");
        logger.trace("Adding UserDto: {} for CourseDto: {}", userDto, courseDto);
        if (dao.existsCourseForUser(mapper.map(userDto, User.class), mapper.map(courseDto, Course.class))) {
            logger.warn("User {} for Course {} already exists.", userDto, courseDto);
            throw new EntityAlreadyExistsException("User " + userDto + " for Course " + courseDto + " already exists.");
        }
        dao.add(mapper.map(userDto, User.class), mapper.map(courseDto, Course.class));
    }

    public void delete(UserDto userDto, CourseDto courseDto) {
        logger.debug("Deleting UserDto for CourseDto");
        logger.trace("Deleting UserDto: {} for CourseDto: {}", userDto, courseDto);
        if (!dao.existsCourseForUser(mapper.map(userDto, User.class), mapper.map(courseDto, Course.class))) {
            logger.warn("Not found User : {} for Course {}", userDto, courseDto);
            throw new EntityNotFoundException("Not found User : " + userDto + " with Course: " + courseDto);
        }
        dao.delete(mapper.map(userDto, User.class), mapper.map(courseDto, Course.class));
    }

    public boolean existsCourseForUser(UserDto userDto, CourseDto courseDto) {
        logger.debug("Checking if UserDto added to CourseDto");
        logger.trace("Checking if UserDto: {} added to CourseDto: {}", userDto, courseDto);
        return dao.existsCourseForUser(mapper.map(userDto, User.class), mapper.map(courseDto, Course.class));
    }
}
