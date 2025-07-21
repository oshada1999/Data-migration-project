package com.cba.datamigration.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    private DBConnection(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://148.251.225.125:3306/trackable?rewriteBatchedStatements=true", "trckuser", "Tr(k4U$#r");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to save the Delete");
        }

    }
    public static DBConnection getInstance() {
        return (null == dbConnection) ? dbConnection = new DBConnection() : dbConnection;
    }
    public Connection getConnection() {
        return connection;
    }
}