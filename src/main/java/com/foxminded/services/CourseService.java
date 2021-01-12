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
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final ModelMapper mapper;
    private final CourseDao dao;

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
        List<Course> courses = dao.findAll();
        return courses.stream().map(item -> mapper.map(item, CourseDto.class)).collect(Collectors.toList());
    }

    public CourseDto findById(int id) {
        Course course = dao.findById(id);
        if (course == null) {
            throw new EntityNotFoundException("Not found course with id: " + id);
        }
        return mapper.map(course, CourseDto.class);
    }

    public CourseDto findByName(String name) {
        Course course = dao.findByName(name);
        if (course == null) {
            throw new EntityNotFoundException("Not found course with name: " + name);
        }
        return mapper.map(course, CourseDto.class);
    }

    public void add(CourseDto course) {
        if (dao.findByName(course.getName()) != null) {
            throw new EntityAlreadyExistsException("Course with name " + course.getName() + " already exists.");
        }
        dao.add(mapper.map(course, Course.class));
    }

    public void update(int id, CourseDto courseDto) {
        if (dao.findById(id) == null) {
            throw new EntityNotFoundException("Not found course with id: " + id);
        }
        dao.update(id, mapper.map(courseDto, Course.class));
    }

    public void deleteById(int id) {
        if (dao.findById(id) == null) {
            throw new EntityNotFoundException("Not found course with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByName(String name) {
        if (dao.findByName(name) == null) {
            throw new EntityNotFoundException("Not found course with name: " + name);
        }
        dao.deleteByName(name);
    }

}
