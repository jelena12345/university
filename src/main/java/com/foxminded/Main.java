package com.foxminded;

import com.foxminded.config.DaoConfiguration;
import com.foxminded.dao.CourseDao;
import com.foxminded.dao.ProfessorDao;
import com.foxminded.entities.Professor;
import com.foxminded.services.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoConfiguration.class);
        ProfessorDao dao = new ProfessorDao(context.getBean(NamedParameterJdbcTemplate.class));
        dao.deleteByPersonalId("1");
        Professor expected = new Professor("1", "name", "surname", "q");
        dao.add(expected);

        CourseService service = new CourseService(context.getBean(ModelMapper.class), context.getBean(CourseDao.class),
                context.getBean(Logger.class));
        service.findByName("name");
    }
}
