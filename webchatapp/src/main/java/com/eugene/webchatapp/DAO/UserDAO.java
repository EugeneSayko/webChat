package com.eugene.webchatapp.DAO;

import com.eugene.webchatapp.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 26.05.16.
 */
public class UserDAO {

    private static final String SQL_INSERT = "INSERT INTO users(id, name, password) VALUES(?, ?, ?)";
    private static final String SQL_ALL_SELECT = "SELECT * FROM users";
    private static final String SQL_NAME_SELECT = "SELECT name FROM users";
    private static final String SQL_UPDATE_NAME = "UPDATE users set name=? WHERE name = ?";
    private static final String SQL_NAME_SELECT_BY_ID = "SELECT name FROM users WHERE id = ?";
    private static final String SQL_ID_SELECT_BY_NAME = "SELECT id FROM users WHERE name = ?";
    private static final String SQL_USER_SELECT_BY_NAME = "SELECT * FROM users WHERE name=?";

    public boolean insertUser(User user){

        boolean flag = false;

        try(
                PreparedStatement ps = ConnectorDB.getConnection().prepareStatement(SQL_INSERT);
                ) {


            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();

            flag = true;

            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public List<User> findAll(){
        List<User> users = new ArrayList<>();

        try(
                Statement statement = ConnectorDB.getConnection().createStatement();
                ) {

            ResultSet resultSet = statement.executeQuery(SQL_ALL_SELECT);

            while(resultSet.next()){
                User user = new User();

                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));

                users.add(user);
            }
            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return users;
    }

    public User findUser(String username){
        User user = null;

        try(
                PreparedStatement preparedStatement = ConnectorDB.getConnection().prepareStatement(SQL_USER_SELECT_BY_NAME);

        ) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
            }

            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<String> findName(){
        List<String> names = new ArrayList<>();

        try(
                Statement statement = ConnectorDB.getConnection().createStatement();
        ) {

            ResultSet resultSet = statement.executeQuery(SQL_NAME_SELECT);

            while(resultSet.next()){
                String name = resultSet.getString("name");

                names.add(name);
            }

            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }

    public boolean updateName(String oldName, String newName){
        boolean flag = false;

        try(
                PreparedStatement ps = ConnectorDB.getConnection().prepareStatement(SQL_UPDATE_NAME);
                ) {


            ps.setString(1, newName);
            ps.setString(2, oldName);

            ps.executeUpdate();

            flag = true;
            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public String findById(String id){

        String name = null;

        try(
                PreparedStatement preparedStatement = ConnectorDB.getConnection().prepareStatement(SQL_NAME_SELECT_BY_ID);

                ) {

            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                name = resultSet.getString("name");
            }
            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

    public String findByName(String name){

        String id = null;

        try(
                PreparedStatement preparedStatement = ConnectorDB.getConnection().prepareStatement(SQL_ID_SELECT_BY_NAME);

        ) {

            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                id = resultSet.getString("id");
            }
            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;

    }
}
