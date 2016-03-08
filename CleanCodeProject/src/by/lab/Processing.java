package by.lab;

import by.lab.date.DateUtil;
import by.lab.file.Log;
import by.lab.message.Message;
import by.lab.message.MessageUtil;
import by.lab.message.SortComparator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Processing {

    private final MessageUtil messageUtil;
    private final BufferedReader reader;

    public Processing(){

        messageUtil = new MessageUtil();
        reader = new BufferedReader(new InputStreamReader(System.in));

    }

    public void start() throws IOException {

        Log.in("program launch");

        int number = 1;

        while (number != 0) {
            menu();
            try {
                number = Integer.parseInt(reader.readLine());
                switch (number) {

                    case 0:
                        break;

                    case 1:
                        printAllMessage();
                        break;

                    case 2:
                        readFile();
                        break;

                    case 3:
                        saveFile();
                        break;

                    case 4:
                        addMessage();
                        break;

                    case 5:
                        chronologicalOrder();
                        break;

                    case 6:
                        deleteMessage();
                        break;

                    case 7:
                        searchAuthor();
                        break;

                    case 8:
                        searchKeyword();
                        break;

                    case 9:
                        searchRegularExpression();
                        break;

                    case 10:
                        historyPeriod();
                        break;

                    default:
                        System.out.println("no menu item!");
                        break;
                }

            }
            catch (NumberFormatException e) {
                Log.in(e.toString());
                System.out.println("Enter only numerals!");
            }
        }
    }

    private void menu() {
        System.out.println("1. view all messages");
        System.out.println("2. download messages from a file");
        System.out.println("3. save the messages to a file");
        System.out.println("4. add a message");
        System.out.println("5. view messages in chronological order");
        System.out.println("6. delete the message by ID");
        System.out.println("7. Search messages by author");
        System.out.println("8. Search in the history of messages by key word (token entirely)");
        System.out.println("9. Search in the history of messages by a regular expression");
        System.out.println("10. view message history for a certain period");
        System.out.println("Enter 0 to exit");
    }

    private void addMessage() throws IOException {

        System.out.println("Enter id: ");
        String id = reader.readLine();

        System.out.println("Enter message: ");
        String message = reader.readLine();

        System.out.println("Enter author: ");
        String author = reader.readLine();

        long timestamp = DateUtil.getPresentTime();

        Log.in("add message");

        this.messageUtil.add(new Message(id, message, author, timestamp));

        Log.in(author + ": " + message);
    }

    private void saveFile() throws IOException {

        System.out.println("Enter url to file");
        String url = reader.readLine();

        try {
            messageUtil.saveFile(url);
        }
        catch (FileNotFoundException e) {
            Log.in("error not found file");
            System.out.println("not found file");
        }
    }

    private void readFile() throws IOException {

        System.out.println("Enter url to file");
        String url = reader.readLine();

        try {
            messageUtil.addFile(url);
        }
        catch (FileNotFoundException e) {

            Log.in("error not found file");
            System.out.println("not found file");

        }
    }

    private void printAllMessage() {

        Log.in("request: view all messages");

        Log.in("total number of messages - " + messageUtil.getAll().size());

        print(messageUtil.getAll());
    }

    private void deleteMessage() throws IOException {

        Log.in("request: delete the message by ID");

        System.out.println("enter id: ");
        String id = reader.readLine();

        messageUtil.delete(id);
    }

    private void chronologicalOrder() {

        Log.in("request: view messages in chronological order");

        List<Message> list = new ArrayList<>(messageUtil.getAll());

        Collections.sort(list, new SortComparator());

        print(list);
    }

    private void print(List<Message> list) {

        if (list != null && list.size() != 0) {

            for (Message item : list) {

                System.out.println("id - " + item.getId());
                System.out.println("message - " + item.getMessage());
                System.out.println("author - " + item.getAuthor());
                System.out.println("timestamp - " + DateUtil.stringDate(item.getTimestamp()));
                System.out.println("------------------------------");

            }

        } else {
            System.out.println("not found messages");
        }
    }

    private void searchAuthor() throws IOException {

        Log.in("request: search messages by author");

        System.out.println("Enter author: ");
        String author = reader.readLine();

        Log.in("author: " + author);

        List<Message> list = new ArrayList<>();
        list.addAll(messageUtil.searchAuthor(author));

        Log.in("messages found - " + list.size());

        print(list);
    }

    private void searchKeyword() throws IOException {

        Log.in("request: Search in the history of messages by key word (token entirely)");

        System.out.println("Enter keyword(lexeme): ");
        String keyword = reader.readLine();

        Log.in("key word: " + keyword);

        List<Message> list = new ArrayList<>();
        list.addAll(messageUtil.searchKeyword(keyword));

        Log.in("messages found - " + list.size());

        print(list);
    }

    private void searchRegularExpression() throws IOException {

        Log.in("request: Search in the history of messages by a regular expression");

        System.out.println("Enter regular expression: ");
        String regular = reader.readLine();

        Log.in("regular expression: " + regular);

        List<Message> list = new ArrayList<>();
        list.addAll(messageUtil.searchRegularExpression(regular));

        Log.in("messages found - " + list.size());

        print(list);
    }

    private void historyPeriod() throws IOException {

        Log.in("request: view message history for a certain period");

        try {

            System.out.println("Enter min date(for example - 01 01 1970): ");
            LocalDate minDate = LocalDate.parse(reader.readLine(), DateUtil.getFormat());

            System.out.println("Enter max date(format - 01 01 1970): ");
            LocalDate maxDate = LocalDate.parse(reader.readLine(), DateUtil.getFormat());

            Log.in("period: " + minDate + " and " + maxDate);

            List<Message> list = new ArrayList<>();
            list.addAll(messageUtil.historyPeriod(minDate, maxDate));

            print(list);

        }
        catch (DateTimeParseException e) {
            Log.in(e.toString());
            System.out.println("wrong format!");
        }
    }
}