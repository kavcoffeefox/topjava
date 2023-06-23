package ru.javawebinar.topjava.util;

import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class ConnectionManager {
    public static final String URL = "db.url";
    public static final String USERNAME = "db.username";
    public static final String PASSWORD = "db.password";
    public static final String DRIVER = "db.driver";

    private static PropertiesUtil propertiesUtil;

    static {
        try {
            Class.forName(PropertiesUtil.get(DRIVER));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Connection get() {
        try {
            return DriverManager.getConnection(
              PropertiesUtil.get(URL),
              PropertiesUtil.get(USERNAME),
              PropertiesUtil.get(PASSWORD)
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
