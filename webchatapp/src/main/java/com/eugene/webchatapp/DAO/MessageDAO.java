package com.eugene.webchatapp.DAO;

import com.eugene.webchatapp.models.Message;
import com.eugene.webchatapp.service.UserDAOService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by eugene on 26.05.16.
 */
public class MessageDAO {

    private static final String SQL_INSERT = "INSERT INTO messages(id, id_user, text, date_time) VALUES(?, ?, ?, ?)";
    private static final String SQL_ALL_SELECT = "SELECT * FROM messages ORDER BY date_time";
    private static final String SQL_DELETE_ID = "DELETE FROM messages where id = ?";
    private static final String SQL_UPDATE_TEXT = "UPDATE messages set text = ? where id = ?";
    private static final UserDAOService userDAOService = new UserDAOService();


    public boolean insertMessage(Message message){

        boolean flag = false;

        try(
                PreparedStatement ps = ConnectorDB.getConnection().prepareStatement(SQL_INSERT);

                ) {


            ps.setString(1, message.getId());
            ps.setString(2, userDAOService.getByUsername(message.getAuthor()));
            ps.setString(3, message.getText());
            ps.setTimestamp(4, new java.sql.Timestamp(message.getTimestamp()));

            ps.executeUpdate();

            flag = true;
            ConnectorDB.closeConnection();
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
                message.setAuthor(userDAOService.getUserByUid(resultSet.getString("id_user")));
                message.setText(resultSet.getString("text"));
                message.setTimestamp(resultSet.getDate("date_time").getTime());

                messages.add(message);
            }
            ConnectorDB.closeConnection();
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
            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    public boolean update(Message message){

        boolean flag = true;

        try(
                PreparedStatement preparedStatement = ConnectorDB.getConnection().prepareStatement(SQL_UPDATE_TEXT);
                ) {

            preparedStatement.setString(1, message.getText());
            preparedStatement.setString(2, message.getId());

            preparedStatement.executeUpdate();
            flag = true;
            ConnectorDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

}
