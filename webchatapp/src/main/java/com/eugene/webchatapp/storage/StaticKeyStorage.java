package com.eugene.webchatapp.storage;

import com.eugene.webchatapp.DAO.UserDAO;
import com.eugene.webchatapp.models.User;
import com.eugene.webchatapp.utils.MessageHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

/**
 * Created by eugene on 12.05.16.
 */
public class StaticKeyStorage {

    private static final Map<String, String > userIdMap = new HashMap<>();

    private static final String USERS_NAME_FILE = System.getenv("HOME") + "/users.txt";

    private static List<User> users = new ArrayList<>();

    private static UserDAO userDAO;

    static {
        userDAO = new UserDAO();
        //loadUsers();
        findAllUsers();
    }

    public static String getByUsername(String username){
        /*for(User user : users){
            if(user.getName().equals(username)){
                return user.getId();
            }
        }

        return null;*/
        return userDAO.findByName(username);
    }

    public static String getUserByUid(String uid){

        /*for(User user : users){
            if(user.getId().equals(uid)){
                return user.getName();
            }
        }
        return null;*/
        return userDAO.findById(uid);
    }

    public static boolean isPassword(String username, String password){
        for(User user : users){
            if(user.getName().equals(username)){
                if(user.getPassword().equals(password)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isUser(String username){

        for(User user : users){
            if(user.getName().equals(username)){
                return true;
            }
        }

        return false;
    }

    public static List<String> getUsersName(){
        /*List<String> names = new ArrayList<>();

        for(User user : users){
            names.add(user.getName());
        }

        return names;*/

        return userDAO.findName();
    }

    public static boolean addUser(String username, String password){
        String token = "token-u$#" + System.nanoTime();

        User user = new User(token, username, password);

        users.add(user);
        //saveUser(user);

        return userDAO.insertUser(user);
    }

    private static void loadUsers(){

        try(
                Scanner scanner = new Scanner(new FileReader(USERS_NAME_FILE));
                ) {


            while(scanner.hasNextLine()){
                try {

                    JSONObject jsonObject = MessageHelper.stringToJsonObject(scanner.nextLine());
                    User user = MessageHelper.jsonObjectToUser(jsonObject);
                    users.add(user);

                } catch (ParseException e) {}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveUser(User user){


        try(
                FileOutputStream fos = new FileOutputStream(USERS_NAME_FILE, true);
                PrintStream printStream = new PrintStream(fos);
                ) {

            printStream.println(MessageHelper.userToJSONObject(user));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void saveAllUsers(){
        try(
                FileOutputStream fos = new FileOutputStream(USERS_NAME_FILE);
                PrintStream printStream = new PrintStream(fos);
        ) {

            for(User user : users){
                printStream.println(MessageHelper.userToJSONObject(user));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void editName(String newName, String oldName){

        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(oldName)){

                if(userDAO.updateName(oldName, newName)){
                    users.get(i).setName(newName);
                    //saveAllUsers();
                    return;
                }

            }
        }


    }

    private static void findAllUsers(){
        users.addAll(userDAO.findAll());
    }
}
