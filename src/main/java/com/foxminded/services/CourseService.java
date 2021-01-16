package com.foxminded.services;

import com.foxminded.dao.CourseDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.entities.Course;
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
public class CourseService {

    private final ModelMapper mapper;
    private final CourseDao dao;
    private final Logger logger;

    @Autowired
    public CourseService(ModelMapper mapper, CourseDao dao, Logger logger) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
        this.logger = logger;
    }

    PropertyMap<CourseDto, Course> skipIdFieldMap = new PropertyMap<CourseDto, Course>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<CourseDto> findAll() {
        logger.log(Level.FINE, "Searching for all CourseDto records");
        List<Course> courses = dao.findAll();
        return courses.stream().map(item -> mapper.map(item, CourseDto.class)).collect(Collectors.toList());
    }

    public CourseDto findById(int id) {
        logger.log(Level.FINE, "Searching for CourseDto with id: {0}", id);
        return mapper.map(dao.findById(id), CourseDto.class);
    }

    public CourseDto findByName(String name) {
        logger.log(Level.FINE, "Searching for CourseDto with name: {0}", name);
        return mapper.map(dao.findByName(name), CourseDto.class);
    }

    public void add(CourseDto course) {
        logger.log(Level.FINE, "Adding CourseDto: {0}", course);
        if (dao.findByName(course.getName()) != null) {
            throw new EntityAlreadyExistsException("Course with name " + course.getName() + " already exists.");
        }
        dao.add(mapper.map(course, Course.class));
    }

    public void update(int id, CourseDto courseDto) {
        logger.log(Level.FINE, "Updating CourseDto: {0} with provided id: {1}", new Object[]{courseDto, id});
        if (dao.findById(id) == null) {
            throw new EntityNotFoundException("Not found course with id: " + id);
        }
        dao.update(id, mapper.map(courseDto, Course.class));
    }

    public void deleteById(int id) {
        logger.log(Level.FINE, "Deleting Course with id: {0}", id);
        if (dao.findById(id) == null) {
            throw new EntityNotFoundException("Not found course with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByName(String name) {
        logger.log(Level.FINE, "Deleting Course with name: {0}", name);
        if (dao.findByName(name) == null) {
            throw new EntityNotFoundException("Not found course with name: " + name);
        }
        dao.deleteByName(name);
    }

}
