package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.utils.MessageHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Sayko on 29.03.2016.
 */
public class InFileMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private static final String FILE_NAME_SAVE_MESSAGES = "src/files/log.txt";

    @Override
    public List<Message> getPortion(Portion portion) {
        return null;
    }

    @Override
    public void addMessage(Message message) {
        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME_SAVE_MESSAGES, true);
                PrintStream printStream = new PrintStream(fos);
                ) {
            printStream.println(MessageHelper.messageToJSONObject(message));
        }catch (IOException e){
            logger.info("error save to file");
        }
    }

    @Override
    public boolean updateMessage(Message message) throws FileNotFoundException {

        List<Message> list = getMessagesFromFile();

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getId().equals(message.getId())){
                list.get(i).setText(message.getText());

                rewrite(list);

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean removeMessage(String messageId) throws FileNotFoundException {
        List<Message> list = getMessagesFromFile();

        Iterator<Message> iterator = list.iterator();

        while(iterator.hasNext()){
            Message message = iterator.next();

            if(message.getId().equals(messageId)){
                iterator.remove();
                rewrite(list);
                return true;
            }

        }

        return false;
    }

    public List getMessagesFromFile() throws FileNotFoundException {

        Scanner scanner = new Scanner(new FileReader(FILE_NAME_SAVE_MESSAGES));

        List<Message> list = new ArrayList<>();

        while(scanner.hasNextLine()){

            try {
                JSONObject obj = MessageHelper.stringToJsonObject(scanner.nextLine());

                Message mess = MessageHelper.jsonObjectToMessage(obj);

                list.add(mess);

            } catch (ParseException e) {}
        }

        return list;

    }

    private void rewrite(List<Message> list){
        try(
                FileOutputStream fos = new FileOutputStream(FILE_NAME_SAVE_MESSAGES);
                PrintStream printStream = new PrintStream(fos);
        ) {

            for(Message item : list){
                printStream.println(MessageHelper.messageToJSONObject(item));
            }

        }catch (IOException e){
            logger.info("error save to file");
        }
    }

    @Override
    public int size() {
        return 0;
    }
}
