package com.eugene.webchatapp.storage;

import com.eugene.webchatapp.DAO.MessageDAO;
import com.eugene.webchatapp.models.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private List<Message> messages;

    private InFileMessageStorage fileMessageStorage;

    private MessageDAO messageDAO;

    public InMemoryMessageStorage(){
        messages = new ArrayList<>();
        fileMessageStorage = new InFileMessageStorage();
        messageDAO = new MessageDAO();
        loadMessages();
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public boolean addMessage(Message message) {

        for(Message item : messages){
            if(item.getId().equals(message.getId())){
                System.err.println("error add message");
                return false;
            }
        }
        messages.add(message);
        //fileMessageStorage.addMessage(message);
        messageDAO.insertMessage(message);
        return true;
    }

    @Override
    public boolean updateMessage(Message message) throws FileNotFoundException {

        for(int i = 0; i < messages.size(); i++){
            if(messages.get(i).getId().equals(message.getId())){

                if(messageDAO.update(messages.get(i).getText(), message.getText())){
                    messages.get(i).setText(message.getText());
                    return true;
                }
                //fileMessageStorage.updateMessage(message);
            }
        }

        return false;
    }

    @Override
    public boolean removeMessage(String messageId) throws FileNotFoundException {

        Iterator<Message> iterator = messages.iterator();
        while(iterator.hasNext()){
            Message message = iterator.next();
            if(message.getId().equals(messageId)){

                if(messageDAO.delete(messageId)){
                    iterator.remove();
                    return true;
                }
                //fileMessageStorage.removeMessage(messageId);
            }
        }

        return false;
    }

    public void loadMessages(){
        /*try {
            messages.addAll(fileMessageStorage.getMessagesFromFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        messages.addAll(messageDAO.findAll());
    }

    @Override
    public int size() {
        return messages.size();
    }

    public List<Message> getMessages(){
        return messages;
    }
}
