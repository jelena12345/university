package com.foxminded.services;

import com.foxminded.dao.CourseDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.entities.Course;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import com.foxminded.services.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final ModelMapper mapper;
    private final CourseDao dao;
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    public CourseService(ModelMapper mapper, CourseDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<CourseDto, Course> skipIdFieldMap = new PropertyMap<CourseDto, Course>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<CourseDto> findAll() {
        logger.debug("Searching for all CourseDto records");
        List<Course> courses = dao.findAll();
        return courses.stream().map(item -> mapper.map(item, CourseDto.class)).collect(Collectors.toList());
    }

    public CourseDto findById(int id) {
        logger.debug("Searching for CourseDto by id");
        logger.trace("Searching for CourseDto with id: {}", id);
        return mapper.map(dao.findById(id), CourseDto.class);
    }

    public CourseDto findByName(String name) {
        logger.debug("Searching for CourseDto by name");
        logger.trace("Searching for CourseDto with name: {}", name);
        return mapper.map(dao.findByName(name), CourseDto.class);
    }

    public void add(CourseDto course) {
        logger.debug("Adding CourseDto");
        logger.trace("Adding CourseDto: {}", course);
        if (dao.existsByName(course.getName())) {
            logger.warn("Activity with name {} already exists.", course.getName());
            throw new EntityAlreadyExistsException("Course with name " + course.getName() + " already exists.");
        }
        dao.add(mapper.map(course, Course.class));
    }

    public void update(int id, CourseDto courseDto) {
        logger.debug("Updating CourseDto");
        logger.trace("Updating CourseDto: {} with provided id: {}", courseDto, id);
        if (!dao.existsById(id)) {
            logger.warn("Not found Course with id: {}", id);
            throw new EntityNotFoundException("Not found Course with id: " + id);
        }
        dao.update(id, mapper.map(courseDto, Course.class));
    }

    public void deleteById(int id) {
        logger.debug("Deleting Course by id");
        logger.trace("Deleting Course with id: {}", id);
        if (!dao.existsById(id)) {
            logger.warn("Not found Course with id: {}", id);
            throw new EntityNotFoundException("Not found Course with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByName(String name) {
        logger.debug("Deleting Course by name");
        logger.trace("Deleting Course with name: {}", name);
        if (!dao.existsByName(name)) {
            logger.warn("Not found Course with name: {}", name);
            throw new EntityNotFoundException("Not found Course with name: " + name);
        }
        dao.deleteByName(name);
    }

    public boolean existsById(int id) {
        logger.debug("Checking if Course exists by id");
        logger.trace("Checking if Course exists with id: {}", id);
        return dao.existsById(id);
    }

    public boolean existsByName(String name) {
        logger.debug("Checking if Course exists by name");
        logger.trace("Checking if Course exists with name: {}", name);
        return dao.existsByName(name);
    }

}
