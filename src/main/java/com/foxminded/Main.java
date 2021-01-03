package com.foxminded;

import com.foxminded.config.SpringConfig;
import com.foxminded.dao.ProfessorDao;
import com.foxminded.dto.Professor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ProfessorDao dao = new ProfessorDao(context.getBean(NamedParameterJdbcTemplate.class));
        Professor expected = new Professor("name", "surname", "q");
        dao.add(expected);
    }
}
