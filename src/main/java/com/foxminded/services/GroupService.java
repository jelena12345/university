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

@Service
public class GroupService {

    private final ModelMapper mapper;
    private final GroupDao dao;

    @Autowired
    GroupService(ModelMapper mapper, GroupDao dao) {
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
        Group group =  dao.findGroupForCourse(mapper.map(courseDto, Course.class));
        return mapper.map(group, GroupDto.class);
    }

    public void add(StudentDto studentDto, CourseDto courseDto) {
        dao.add(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }

    public void delete(StudentDto studentDto, CourseDto courseDto) {
        dao.delete(mapper.map(studentDto, Student.class), mapper.map(courseDto, Course.class));
    }
}
