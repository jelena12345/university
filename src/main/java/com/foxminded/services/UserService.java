package com.foxminded.services;

import com.foxminded.dao.UserDao;
import com.foxminded.dto.UserDto;
import com.foxminded.entities.User;
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
public class UserService {
    private final ModelMapper mapper;
    private final UserDao dao;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(ModelMapper mapper, UserDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipIdFieldMap);
        this.dao = dao;
    }

    PropertyMap<UserDto, User> skipIdFieldMap = new PropertyMap<UserDto, User>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    public List<UserDto> findAll() {
        logger.debug("Searching for all UserDto records");
        List<User> users = dao.findAll();
        return users.stream().map(item -> mapper.map(item, UserDto.class)).collect(Collectors.toList());
    }

    public UserDto findById(int id) {
        logger.debug("Searching for UserDto by id");
        logger.trace("Searching for UserDto by id: {}", id);
        return mapper.map(dao.findById(id), UserDto.class);
    }

    public UserDto findByPersonalId(String personalId) {
        logger.debug("Searching for UserDto by personalId");
        logger.trace("Searching for UserDto by personalId: {}", personalId);
        return mapper.map(dao.findByPersonalId(personalId), UserDto.class);
    }

    public void add(UserDto user) {
        logger.debug("Adding UserDto");
        logger.trace("Adding UserDto: {}", user);
        if (dao.existsByPersonalId(user.getPersonalId())) {
            logger.warn("User with personalId {} already exists.", user.getPersonalId());
            throw new EntityAlreadyExistsException("User with personalId " + user.getPersonalId() + " already exists.");
        }
        dao.add(mapper.map(user, User.class));
    }

    public void update(UserDto user) {
        logger.debug("Updating UserDto");
        logger.trace("Updating UserDto: {} ", user);
        if (!dao.existsByPersonalId(user.getPersonalId())) {
            logger.warn("Not found User with personal id: {}", user.getPersonalId());
            throw new EntityNotFoundException("Not found User with personal id: " + user.getPersonalId());
        }
        dao.update(mapper.map(user, User.class));
    }

    public void deleteById(int id) {
        logger.debug("Deleting User by id");
        logger.trace("Deleting User by id: {}", id);
        if (!dao.existsById(id)) {
            logger.warn("Not found User with id: {}", id);
            throw new EntityNotFoundException("Not found User with id: " + id);
        }
        dao.deleteById(id);
    }

    public void deleteByPersonalId(String personalId) {
        logger.debug("Deleting User by personalId");
        logger.trace("Deleting User by personalId: {}", personalId);
        if (!dao.existsByPersonalId(personalId)) {
            logger.warn("Not found User with personalId: {}", personalId);
            throw new EntityNotFoundException("Not found User with name: " + personalId);
        }
        dao.deleteByPersonalId(personalId);
    }

    public boolean existsById(int id) {
        logger.debug("Checking if User exists by id");
        logger.trace("Checking if User exists by id: {}", id);
        return dao.existsById(id);
    }

    public boolean existsByName(String personalId) {
        logger.debug("Checking if User exists by personalId");
        logger.trace("Checking if User exists by personalId: {}", personalId);
        return dao.existsByPersonalId(personalId);
    }
}
