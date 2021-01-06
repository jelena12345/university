package com.foxminded.services;

import com.foxminded.dao.ActivityDao;
import com.foxminded.dto.ActivityDto;
import com.foxminded.dto.CourseDto;
import com.foxminded.dto.ProfessorDto;
import com.foxminded.entities.Activity;
import com.foxminded.entities.Course;
import com.foxminded.entities.Professor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {

    private final ModelMapper mapper;
    private final ActivityDao dao;

    PropertyMap<ProfessorDto, Professor> skipProfessorIdFieldMap = new PropertyMap<ProfessorDto, Professor>() {
        protected void configure() {
            skip().setId(null);
        }
    };
    PropertyMap<CourseDto, Course> skipCourseIdFieldMap = new PropertyMap<CourseDto, Course>() {
        protected void configure() {
            skip().setId(null);
        }
    };

    @Autowired
    public ActivityService(ModelMapper mapper, ActivityDao dao) {
        this.mapper = mapper;
        this.mapper.addMappings(skipCourseIdFieldMap);
        this.mapper.addMappings(skipProfessorIdFieldMap);
        this.dao = dao;
    }

    public List<ActivityDto> findAll() {
        List<Activity> activities = dao.findAll();
        return activities.stream().map(item -> mapper.map(item, ActivityDto.class)).collect(Collectors.toList());
    }

    public ActivityDto findById(int id) {
        return mapper.map(dao.findById(id), ActivityDto.class);
    }

    public void add(ActivityDto activity) {
        dao.add(mapper.map(activity, Activity.class));
    }

    public void update(ActivityDto activity) {
        dao.update(mapper.map(activity, Activity.class));
    }

    public void deleteById(int id) {
        dao.deleteById(id);
    }

}
