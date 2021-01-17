package com.foxminded.services;

import com.foxminded.dao.GroupDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
import com.foxminded.entities.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GroupService {

    private final ModelMapper mapper;
    private final GroupDao dao;
    private final Logger logger;

    @Autowired
    GroupService(ModelMapper mapper, GroupDao dao, Logger logger) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
        this.logger = logger;
    }

    PropertyMap<StudentDto, Student> skipIdFieldMap = new PropertyMap<StudentDto, Student>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public GroupDto findGroupForCourse(CourseDto courseDto) {
        logger.log(Level.FINE, "Searching Group for CourseDto: {0}", courseDto);
        Group group =  dao.findGroupForCourse(mapper.map(courseDto, Course.class));
        return mapper.map(group, GroupDto.class);
    }

    public void add(StudentDto studentDto, CourseDto courseDto) {
        logger.log(Level.FINE, "Adding StudentDto: {0} for CourseDto: {1}", new Object[]{studentDto, courseDto});
        dao.add(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }

    public void delete(StudentDto studentDto, CourseDto courseDto) {
        logger.log(Level.FINE, "Deleting StudentDto: {0} for CourseDto: {1}", new Object[]{studentDto, courseDto});
        dao.delete(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }
}
