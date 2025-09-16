package com.cba.datamigration.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find database.properties");
            }
            props.load(input);

            Class.forName(props.getProperty("db.driver"));
            connection = DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
            );
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException("Failed to establish database connection: " + e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        return (null == dbConnection) ? dbConnection = new DBConnection() : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}