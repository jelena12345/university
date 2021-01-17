package com.foxminded.services;

import com.foxminded.dao.ProfessorDao;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Professor;
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
public class ProfessorService {
    private final ModelMapper mapper;
    private final ProfessorDao dao;
    private final Logger logger;

    @Autowired
    public ProfessorService(ModelMapper mapper, ProfessorDao dao, Logger logger) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
        this.logger = logger;
    }

    PropertyMap<ProfessorDto, Professor> skipIdFieldMap = new PropertyMap<ProfessorDto, Professor>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<ProfessorDto> findAll() {
        logger.log(Level.FINE, "Searching for all ProfessorDto records");
        List<Professor> professors = dao.findAll();
        return professors.stream().map(item -> mapper.map(item, ProfessorDto.class)).collect(Collectors.toList());
    }

    public ProfessorDto findById(int id) {
        logger.log(Level.FINE, "Searching for ProfessorDto with id: {0}", id);
        return mapper.map(dao.findById(id), ProfessorDto.class);
    }

    public ProfessorDto findByPersonalId(String personalId) {
        logger.log(Level.FINE, "Searching for ProfessorDto with personalId: {0}", personalId);
        return mapper.map(dao.findByPersonalId(personalId), ProfessorDto.class);
    }

    public void add(ProfessorDto professor) {
        logger.log(Level.FINE, "Adding ProfessorDto: {0}", professor);
        if (dao.existsByPersonalId(professor.getName())) {
            throw new EntityAlreadyExistsException("Professor with name " + professor.getName() + " already exists.");
        }
        dao.add(mapper.map(professor, Professor.class));
    }

    public void update(int id, ProfessorDto professor) {
        logger.log(Level.FINE, "Updating ProfessorDto: {0} with provided id: {1}", new Object[]{professor, id});
        if (!dao.existsById(id)) {
            throw new EntityNotFoundException("Not found Professor with id: " + id);
        }
        dao.update(id, mapper.map(professor, Professor.class));
    }

    public void deleteById(int id) {
        logger.log(Level.FINE, "Deleting Professor with id: {0}", id);
        if (!dao.existsById(id)) {
            throw new EntityNotFoundException("Not found Professor with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByPersonalId(String personalId) {
        logger.log(Level.FINE, "Deleting Professor with personalId: {0}", personalId);
        if (!dao.existsByPersonalId(personalId)) {
            throw new EntityNotFoundException("Not found Professor with name: " + personalId);
        }
        dao.deleteByPersonalId(personalId);
    }

    public boolean existsById(int id) {
        logger.log(Level.FINE, "Checking if Professor exists with id: {0}", id);
        return dao.existsById(id);
    }

    public boolean existsByName(String personalId) {
        logger.log(Level.FINE, "Checking if Professor exists with personalId: {0}", personalId);
        return dao.existsByPersonalId(personalId);
    }
}
