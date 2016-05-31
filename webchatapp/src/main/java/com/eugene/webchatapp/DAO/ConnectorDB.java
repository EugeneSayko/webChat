package com.eugene.webchatapp.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by eugene on 26.05.16.
 */
public class ConnectorDB {

    private static final String URL = "jdbc:mysql://localhost:3306/chat";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    public static Connection getConnection() throws SQLException{

        try {
            Class.forName(DRIVER_NAME).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

    public static void closeConnection() throws SQLException{
        connection.close();
    }

}
