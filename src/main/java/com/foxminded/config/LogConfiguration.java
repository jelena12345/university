package com.foxminded.config;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.*;

@Configuration
public class LogConfiguration {

    @Bean
    public Logger logger(InjectionPoint injectionPoint) {
        Logger logger = Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
        try (InputStream is = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("logging.properties"))) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.setLevel(Level.FINEST);
        return logger;
    }
}
