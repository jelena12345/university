package com.foxminded.services;

import com.foxminded.dao.StudentDao;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final ModelMapper mapper;
    private final StudentDao dao;

    @Autowired
    public StudentService(ModelMapper mapper, StudentDao dao) {
        this.mapper = mapper;
        mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<StudentDto, Student> skipIdFieldMap = new PropertyMap<StudentDto, Student>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<StudentDto> findAll() {
        List<Student> students = dao.findAll();
        return students.stream().map(item -> mapper.map(item, StudentDto.class)).collect(Collectors.toList());
    }

    public StudentDto findById(int id) {
        return mapper.map(dao.findById(id), StudentDto.class);
    }

    public void add(StudentDto student) {
        dao.add(mapper.map(student, Student.class));
    }

    public void update(int id, StudentDto student) {
        dao.update(id, mapper.map(student, Student.class));
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

}
