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
    public ProfessorDao professorDao() {
        return new ProfessorDao(jdbcTemplate());
    }

    @Bean
    @Scope("prototype")
    public StudentDao studentDao() {
        return new StudentDao(jdbcTemplate());
    }

    @Bean
    @Scope("prototype")
    public GroupDao groupDao() {
        return new GroupDao(jdbcTemplate());
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
    public GroupService groupService() {
        return new GroupService(modelMapper(), groupDao());
    }

    @Bean
    @Scope("prototype")
    public ProfessorService professorService() {
        return new ProfessorService(modelMapper(), professorDao());
    }

    @Bean
    @Scope("prototype")
    public StudentService studentService() {
        return new StudentService(modelMapper(), studentDao());
    }

}
