package ru.javawebinar.topjava.util;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {
    private static Properties properties = new Properties();

    static {
        InputStream propertyResourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(propertyResourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
