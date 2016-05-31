package com.eugene.webchatapp.service;

import com.eugene.webchatapp.DAO.UserDAO;
import com.eugene.webchatapp.models.User;

import java.util.List;

/**
 * Created by eugene on 31.05.16.
 */
public class UserDAOService {

    private UserDAO userDAO;

    public UserDAOService(){
        userDAO = new UserDAO();
    }

    public String getByUsername(String username){

        return userDAO.findByName(username);
    }

    public String getUserByUid(String uid){

        return userDAO.findById(uid);
    }

    public boolean isPassword(String username, String password) {
        User user = userDAO.findUser(username);
        if(user == null){
            return false;
        }

        if(user.getPassword().equals(password)){
            return true;
        }

        return false;
    }
    public boolean isUser(String username){

        if(userDAO.findUser(username) != null){
            return true;
        }

        return false;
    }

    public List<String> getUsersName(){

        return userDAO.findName();
    }

    public boolean addUser(String username, String password){
        String token = "token-u$#" + System.nanoTime();

        User user = new User(token, username, password);

        return userDAO.insertUser(user);
    }


    public void editName(String newName, String oldName){
        userDAO.updateName(oldName, newName);

    }

    public List<User> findAllUsers(){
        return userDAO.findAll();
    }

}
