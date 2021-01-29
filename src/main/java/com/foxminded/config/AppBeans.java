package com.foxminded.config;

import com.foxminded.dao.*;
import com.foxminded.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.foxminded.dao", "com.foxminded.services", "com.foxminded.config"})
@PropertySource("classpath:application.properties")
public class AppBeans {

    @Value("${jdbc.driver}")
    private String driverName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.pass}")
    private String pass;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(pass);

        Resource initSchema = new ClassPathResource("schema.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);

        return dataSource;
    }

    @Bean
    @Scope("prototype")
    public NamedParameterJdbcTemplate jdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    @Scope("prototype")
    public CourseDao courseDao() {
        return new CourseDao(jdbcTemplate());
    }

    @Bean
    @Scope("prototype")
    public UserDao userDao() {
        return new UserDao(jdbcTemplate());
    }

    @Bean
    @Scope("prototype")
    public UserCourseDao userCourseDao() {
        return new UserCourseDao(jdbcTemplate());
    }

    @Bean
    @Scope("prototype")
    public ActivityDao activityDao() {
        return new ActivityDao(jdbcTemplate());
    }

    @Bean
    @Scope("prototype")
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

    @Bean
    @Scope("prototype")
    public ActivityService activityService() {
        return new ActivityService(modelMapper(), activityDao());
    }

    @Bean
    @Scope("prototype")
    public CourseService courseService() {
        return new CourseService(modelMapper(), courseDao());
    }

    @Bean
    @Scope("prototype")
    public UserCourseService userCourseService() {
        return new UserCourseService(modelMapper(), userCourseDao(), userDao(), courseDao());
    }

    @Bean
    @Scope("prototype")
    public UserService userService() {
        return new UserService(modelMapper(), userDao());
    }

}
