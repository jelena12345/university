package com.foxminded.config;

import com.foxminded.dao.*;
import com.foxminded.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.foxminded.dao", "com.foxminded.services", "com.foxminded.config"})
@PropertySource("classpath:application.properties")
public class AppBeans {

    @Bean
    public DataSource dataSource() {
        try {
            return (DataSource) new JndiTemplate().lookup("java:comp/env/jdbc/University");
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.foxminded.entities");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    @Scope("prototype")
    public CourseDao courseDao() {
        return new CourseDao();
    }

    @Bean
    @Scope("prototype")
    public UserDao userDao() {
        return new UserDao();
    }

    @Bean
    @Scope("prototype")
    public UserCourseDao userCourseDao() {
        return new UserCourseDao();
    }

    @Bean
    @Scope("prototype")
    public ActivityDao activityDao() {
        return new ActivityDao();
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
        return new ActivityService(modelMapper(), activityDao(), userDao(), courseDao());
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
