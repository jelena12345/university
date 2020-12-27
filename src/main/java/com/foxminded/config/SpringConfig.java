package com.foxminded.config;

import com.foxminded.dao.FileReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.foxminded")
public class SpringConfig {

    @Bean
    public DataSource dataSource() {
        FileReader reader = new FileReader();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(reader.readConfig().getProperty("jdbc.url"));
        dataSource.setUsername(reader.readConfig().getProperty("jdbc.user"));
        dataSource.setPassword(reader.readConfig().getProperty("jdbc.pass"));

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
