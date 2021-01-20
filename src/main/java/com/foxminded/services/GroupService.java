package com.foxminded.services;

import com.foxminded.dao.GroupDao;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.GroupDto;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Course;
import com.foxminded.entities.Group;
import com.foxminded.entities.Student;
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

    PropertyMap<StudentDto, Student> skipIdFieldMap = new PropertyMap<StudentDto, Student>() {
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

    public void add(StudentDto studentDto, CourseDto courseDto) {
        logger.debug("Adding StudentDto for CourseDto");
        logger.trace("Adding StudentDto: {} for CourseDto: {}", studentDto, courseDto);
        if (dao.existsCourseForStudent(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class))) {
            logger.warn("Student {} for Course {} already exists.", studentDto, courseDto);
            throw new EntityAlreadyExistsException("Student " + studentDto + " for Course " + courseDto + " already exists.");
        }
        dao.add(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }

    public void delete(StudentDto studentDto, CourseDto courseDto) {
        logger.debug("Deleting StudentDto for CourseDto");
        logger.trace("Deleting StudentDto: {} for CourseDto: {}", studentDto, courseDto);
        if (!dao.existsCourseForStudent(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class))) {
            logger.warn("Not found Student : {} for Course {}", studentDto, courseDto);
            throw new EntityNotFoundException("Not found Student : " + studentDto + " with Course: " + courseDto);
        }
        dao.delete(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }

    public boolean existsCourseForStudent(StudentDto studentDto, CourseDto courseDto) {
        logger.debug("Checking if StudentDto added to CourseDto");
        logger.trace("Checking if StudentDto: {} added to CourseDto: {}", studentDto, courseDto);
        return dao.existsCourseForStudent(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }
}
