package com.foxminded;

import com.foxminded.config.AppBeans;
import com.foxminded.dao.UserDao;
import com.foxminded.entities.User;
import com.foxminded.services.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting University app...");
        logger.info("Loading Application context");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppBeans.class);
        UserDao dao = new UserDao(context.getBean(NamedParameterJdbcTemplate.class));
        dao.deleteByPersonalId("1");
        User expected = new User("1", "role","name", "surname", "q");
        dao.add(expected);

        CourseService service = context.getBean(CourseService.class);
        service.findByName("name");
        logger.info("Exiting University app...");
    }
}
