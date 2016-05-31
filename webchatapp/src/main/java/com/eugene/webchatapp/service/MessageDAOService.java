package com.eugene.webchatapp.service;

import com.eugene.webchatapp.DAO.MessageDAO;
import com.eugene.webchatapp.models.Message;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eugene on 31.05.16.
 */
public class MessageDAOService {

    private MessageDAO messageDAO;
    public MessageDAOService(){
        messageDAO = new MessageDAO();
    }

    public boolean addMessage(Message message) {
        messageDAO.insertMessage(message);
        return true;
    }


    public boolean updateMessage(Message message) throws FileNotFoundException {

        return messageDAO.update(message);

    }

    public boolean removeMessage(String messageId) throws FileNotFoundException {

        return messageDAO.delete(messageId);
    }



    public List<Message> search(String text){
        return null;

    }

    public List<Message> getMessages(){
        return messageDAO.findAll();
    }

}
