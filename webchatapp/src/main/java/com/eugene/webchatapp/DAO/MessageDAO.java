package com.eugene.webchatapp.DAO;

import com.eugene.webchatapp.models.Message;
import com.eugene.webchatapp.storage.StaticKeyStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugene on 26.05.16.
 */
public class MessageDAO {

    private static final String SQL_INSERT = "INSERT INTO messages(id, id_user, text, date) VALUES(?, ?, ?, ?)";
    private static final String SQL_ALL_SELECT = "SELECT * FROM messages";
    private static final String SQL_DELETE_ID = "DELETE FROM messages where id = ?";
    private static final String SQL_UPDATE_TEXT = "UPDATE messages set text = ? where text = ?";


    public boolean insertMessage(Message message){

        boolean flag = false;

        try(
                PreparedStatement ps = ConnectorDB.getConnection().prepareStatement(SQL_INSERT);
                ) {


            ps.setString(1, message.getId());
            ps.setString(2, StaticKeyStorage.getByUsername(message.getAuthor()));
            ps.setString(3, message.getText());
            ps.setDate(4, new Date(message.getTimestamp()));

            ps.executeUpdate();

            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public List<Message> findAll(){
        List<Message> messages = new ArrayList<>();

        try(
                Statement statement = ConnectorDB.getConnection().createStatement();
                ) {

            ResultSet resultSet = statement.executeQuery(SQL_ALL_SELECT);

            while(resultSet.next()){

                Message message = new Message();

                message.setId(resultSet.getString("id"));
                message.setAuthor(StaticKeyStorage.getUserByUid(resultSet.getString("id_user")));
                message.setText(resultSet.getString("text"));
                message.setTimestamp(resultSet.getDate("date").getTime());

                messages.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public boolean delete(String id){

        boolean flag = false;

        try(
                PreparedStatement preparedStatement = ConnectorDB.getConnection().prepareStatement(SQL_DELETE_ID);
                ) {

            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean update(String oldText, String newtext){

        boolean flag = true;

        try(
                PreparedStatement preparedStatement = ConnectorDB.getConnection().prepareStatement(SQL_UPDATE_TEXT);
                ) {

            preparedStatement.setString(1, newtext);
            preparedStatement.setString(2, oldText);

            preparedStatement.executeUpdate();
            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

}
