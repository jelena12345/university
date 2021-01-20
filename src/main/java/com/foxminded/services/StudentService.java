package com.foxminded.services;

import com.foxminded.dao.StudentDao;
import com.foxminded.dto.StudentDto;
import com.foxminded.entities.Student;
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
public class StudentService {
    private final ModelMapper mapper;
    private final StudentDao dao;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);


    @Autowired
    public StudentService(ModelMapper mapper, StudentDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<StudentDto, Student> skipIdFieldMap = new PropertyMap<StudentDto, Student>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<StudentDto> findAll() {
        logger.debug("Searching for all StudentDto records");
        List<Student> students = dao.findAll();
        return students.stream().map(item -> mapper.map(item, StudentDto.class)).collect(Collectors.toList());
    }

    public StudentDto findById(int id) {
        logger.debug("Searching for StudentDto by id");
        logger.trace("Searching for StudentDto by id: {}", id);
        return mapper.map(dao.findById(id), StudentDto.class);
    }

    public StudentDto findByPersonalId(String personalId) {
        logger.debug("Searching for StudentDto by personalId");
        logger.trace("Searching for StudentDto by personalId: {}", personalId);
        return mapper.map(dao.findByPersonalId(personalId), StudentDto.class);
    }

    public void add(StudentDto student) {
        logger.debug("Adding StudentDto");
        logger.trace("Adding StudentDto: {}", student);
        if (dao.existsByPersonalId(student.getName())) {
            logger.warn("Student by name {} already exists.", student.getName());
            throw new EntityAlreadyExistsException("Student by name " + student.getName() + " already exists.");
        }
        dao.add(mapper.map(student, Student.class));
    }

    public void update(int id, StudentDto student) {
        logger.debug("Updating StudentDto");
        logger.trace("Updating StudentDto: {} by provided id: {}", student, id);
        if (!dao.existsById(id)) {
            logger.warn("Not found Student by id: {}", id);
            throw new EntityNotFoundException("Not found Student by id: " + id);
        }
        dao.update(id, mapper.map(student, Student.class));
    }

    public void deleteById(int id) {
        logger.debug("Deleting Student by id");
        logger.trace("Deleting Student by id: {}", id);
        if (!dao.existsById(id)) {
            logger.warn("Not found Student by id: {}", id);
            throw new EntityNotFoundException("Not found Student by id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByPersonalId(String personalId) {
        logger.debug("Deleting Student by personalId");
        logger.trace("Deleting Student by personalId: {}", personalId);
        if (!dao.existsByPersonalId(personalId)) {
            logger.warn("Not found Student by personalId: {}", personalId);
            throw new EntityNotFoundException("Not found Student by personalId: " + personalId);
        }
        dao.deleteByPersonalId(personalId);
    }

    public boolean existsById(int id) {
        logger.debug("Checking if Student exists by id");
        logger.trace("Checking if Student exists by id: {}", id);
        return dao.existsById(id);
    }

    public boolean existsByPersonalId(String personalId) {
        logger.debug("Checking if Student exists by personalId");
        logger.trace("Checking if Student exists by personalId: {}", personalId);
        return dao.existsByPersonalId(personalId);
    }

}
