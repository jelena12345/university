package com.foxminded.dao;

import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class FileReader {
    public Properties readConfig() {
        Properties properties = new Properties();
        String propFileName = "config";
        try (InputStream stream = Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(propFileName))) {
            properties.load(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
