package com.foxminded.services;

import com.foxminded.dao.CourseDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.entities.Course;
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
        mapper.addMappings(skipIdFieldMap);
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
        return mapper.map(dao.findById(id), CourseDto.class);
    }

    public CourseDto findByName(String name) {
        return mapper.map(dao.findByName(name), CourseDto.class);
    }

    public void add(CourseDto course) {
        dao.add(mapper.map(course, Course.class));
    }

    public void update(int id, CourseDto course) {
        dao.update(id, mapper.map(course, Course.class));
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public void deleteByName(String name) {
        dao.deleteByName(name);
    }

}
