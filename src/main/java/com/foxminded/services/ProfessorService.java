package com.foxminded.services;

import com.foxminded.dao.ProfessorDao;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Professor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {
    private final ModelMapper mapper;
    private final ProfessorDao dao;

    @Autowired
    public ProfessorService(ModelMapper mapper, ProfessorDao dao) {
        this.mapper = mapper;
        mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<ProfessorDto, Professor> skipIdFieldMap = new PropertyMap<ProfessorDto, Professor>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<ProfessorDto> findAll() {
        List<Professor> professors = dao.findAll();
        return professors.stream().map(item -> mapper.map(item, ProfessorDto.class)).collect(Collectors.toList());
    }

    public ProfessorDto findById(int id) {
        return mapper.map(dao.findById(id), ProfessorDto.class);
    }

    public ProfessorDto findByPersonalId(String personalId) {
        return mapper.map(dao.findByPersonalId(personalId), ProfessorDto.class);
    }

    public void add(ProfessorDto professor) {
        dao.add(mapper.map(professor, Professor.class));
    }

    public void update(int id, ProfessorDto professor) {
        dao.update(id, mapper.map(professor, Professor.class));
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

    public void deleteByPersonalId(String personalId) {
        dao.deleteByPersonalId(personalId);
    }
}
