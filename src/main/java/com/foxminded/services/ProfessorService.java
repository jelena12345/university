package com.foxminded.services;

import com.foxminded.dao.ProfessorDao;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Professor;
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
public class ProfessorService {
    private final ModelMapper mapper;
    private final ProfessorDao dao;
    private static final Logger logger = LoggerFactory.getLogger(ProfessorService.class);

    @Autowired
    public ProfessorService(ModelMapper mapper, ProfessorDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<ProfessorDto, Professor> skipIdFieldMap = new PropertyMap<ProfessorDto, Professor>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<ProfessorDto> findAll() {
        logger.debug("Searching for all ProfessorDto records");
        List<Professor> professors = dao.findAll();
        return professors.stream().map(item -> mapper.map(item, ProfessorDto.class)).collect(Collectors.toList());
    }

    public ProfessorDto findById(int id) {
        logger.debug("Searching for ProfessorDto by id");
        logger.trace("Searching for ProfessorDto by id: {}", id);
        return mapper.map(dao.findById(id), ProfessorDto.class);
    }

    public ProfessorDto findByPersonalId(String personalId) {
        logger.debug("Searching for ProfessorDto by personalId");
        logger.trace("Searching for ProfessorDto by personalId: {}", personalId);
        return mapper.map(dao.findByPersonalId(personalId), ProfessorDto.class);
    }

    public void add(ProfessorDto professor) {
        logger.debug("Adding ProfessorDto");
        logger.trace("Adding ProfessorDto: {}", professor);
        if (dao.existsByPersonalId(professor.getPersonalId())) {
            logger.warn("Professor with personalId {} already exists.", professor.getPersonalId());
            throw new EntityAlreadyExistsException("Professor with personalId " + professor.getPersonalId() + " already exists.");
        }
        dao.add(mapper.map(professor, Professor.class));
    }

    public void update(ProfessorDto professor) {
        logger.debug("Updating ProfessorDto");
        logger.trace("Updating ProfessorDto: {} ", professor);
        if (!dao.existsByPersonalId(professor.getPersonalId())) {
            logger.warn("Not found Professor with personal id: {}", professor.getPersonalId());
            throw new EntityNotFoundException("Not found Professor with personal id: " + professor.getPersonalId());
        }
        dao.update(mapper.map(professor, Professor.class));
    }

    public void deleteById(int id) {
        logger.debug("Deleting Professor by id");
        logger.trace("Deleting Professor by id: {}", id);
        if (!dao.existsById(id)) {
            logger.warn("Not found Professor with id: {}", id);
            throw new EntityNotFoundException("Not found Professor with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByPersonalId(String personalId) {
        logger.debug("Deleting Professor by personalId");
        logger.trace("Deleting Professor by personalId: {}", personalId);
        if (!dao.existsByPersonalId(personalId)) {
            logger.warn("Not found Professor with personalId: {}", personalId);
            throw new EntityNotFoundException("Not found Professor with name: " + personalId);
        }
        dao.deleteByPersonalId(personalId);
    }

    public boolean existsById(int id) {
        logger.debug("Checking if Professor exists by id");
        logger.trace("Checking if Professor exists by id: {}", id);
        return dao.existsById(id);
    }

    public boolean existsByName(String personalId) {
        logger.debug("Checking if Professor exists by personalId");
        logger.trace("Checking if Professor exists by personalId: {}", personalId);
        return dao.existsByPersonalId(personalId);
    }
}
