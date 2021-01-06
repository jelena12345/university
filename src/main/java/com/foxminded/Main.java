package com.foxminded;

import com.foxminded.config.DaoConfiguration;
import com.foxminded.dao.ProfessorDao;
import com.foxminded.entities.Professor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoConfiguration.class);
        ProfessorDao dao = new ProfessorDao(context.getBean(NamedParameterJdbcTemplate.class));
        dao.deleteByPersonalId("1");
        Professor expected = new Professor("1", "name", "surname", "q");
        dao.add(expected);
    }
}
