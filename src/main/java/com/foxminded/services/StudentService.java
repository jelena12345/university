package com.foxminded.services;

import com.foxminded.dao.StudentDao;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Student;
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
public class StudentService {
    private final ModelMapper mapper;
    private final StudentDao dao;
    private final Logger logger;

    @Autowired
    public StudentService(ModelMapper mapper, StudentDao dao, Logger logger) {
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

    public List<StudentDto> findAll() {
        logger.log(Level.FINE, "Searching for all StudentDto records");
        List<Student> students = dao.findAll();
        return students.stream().map(item -> mapper.map(item, StudentDto.class)).collect(Collectors.toList());
    }

    public StudentDto findById(int id) {
        logger.log(Level.FINE, "Searching for StudentDto with id: {0}", id);
        return mapper.map(dao.findById(id), StudentDto.class);
    }

    public StudentDto findByPersonalId(String personalId) {
        logger.log(Level.FINE, "Searching for StudentDto with personalId: {0}", personalId);
        return mapper.map(dao.findByPersonalId(personalId), StudentDto.class);
    }

    public void add(StudentDto student) {
        logger.log(Level.FINE, "Adding StudentDto: {0}", student);
        if (dao.existsByPersonalId(student.getName())) {
            throw new EntityAlreadyExistsException("Student with name " + student.getName() + " already exists.");
        }
        dao.add(mapper.map(student, Student.class));
    }

    public void update(int id, StudentDto student) {
        logger.log(Level.FINE, "Updating StudentDto: {0} with provided id: {1}", new Object[]{student, id});
        if (!dao.existsById(id)) {
            throw new EntityNotFoundException("Not found Student with id: " + id);
        }
        dao.update(id, mapper.map(student, Student.class));
    }

    public void deleteById(int id) {
        logger.log(Level.FINE, "Deleting Student with id: {0}", id);
        if (!dao.existsById(id)) {
            throw new EntityNotFoundException("Not found Student with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByPersonalId(String personalId) {
        logger.log(Level.FINE, "Deleting Student with personalId: {0}", personalId);
        if (!dao.existsByPersonalId(personalId)) {
            throw new EntityNotFoundException("Not found Student with name: " + personalId);
        }
        dao.deleteByPersonalId(personalId);
    }

    public boolean existsById(int id) {
        logger.log(Level.FINE, "Checking if Student exists with id: {0}", id);
        return dao.existsById(id);
    }

    public boolean existsByName(String personalId) {
        logger.log(Level.FINE, "Checking if Student exists with personalId: {0}", personalId);
        return dao.existsByPersonalId(personalId);
    }

}
