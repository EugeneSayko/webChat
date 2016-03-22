package by.lab.message;

import by.lab.date.DateUtil;
import by.lab.file.Log;
import by.lab.file.FileUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageUtil {
    private final List<Message> historyMessage;
    private final FileUtil fileUtil;

    public MessageUtil(){
        historyMessage = new ArrayList<>();
        fileUtil = new FileUtil();
    }

    public void add(Message message) {
        for (Message item : historyMessage) {
            if (message.getId().equals(item.getId())){
                Log.in("the message is not added, is not a unique id");
                return;
            }
        }
        historyMessage.add(message);
    }

    public void delete(String id) {

        Iterator<Message> iterator = this.historyMessage.iterator();

        while (iterator.hasNext()) {

            Message message = iterator.next();
            if (message.getId().equals(id)){
                iterator.remove();
                Log.in("message deleted");
                Log.in(message.getAuthor() + " : " + message.getMessage());
                return;
            }
        }
        Log.in("message not found");
    }

    public List<Message> getAll() {
        return historyMessage;
    }

    public void addFile(String filename) throws FileNotFoundException {

        List<Message> list = fileUtil.read(filename);
        historyMessage.addAll(list);

        StringBuilder sb = new StringBuilder();
        sb.append("from the ");
        sb.append(filename);
        sb.append(" file uploaded ");
        sb.append(list.size());
        sb.append(" messages");

        Log.in(sb.toString());
    }

    public void saveFile(String filename) throws IOException {
        fileUtil.write(filename, historyMessage);
        Log.in("History of messages downloaded to " + filename);
    }

    public List<Message> searchAuthor(String author) {
        return historyMessage.stream().filter(item -> item.getAuthor().equalsIgnoreCase(author)).collect(Collectors.toList());
    }

    public List<Message> searchKeyword(String keyword) {
        return historyMessage.stream().filter(item -> item.getMessage().toLowerCase().contains(keyword.toLowerCase())).collect(Collectors.toList());
    }

    public List<Message> searchRegularExpression(String regular) {

        List<Message> list = new ArrayList<>();

        Pattern pattern = Pattern.compile(regular);

        for (Message item : historyMessage) {
            Matcher matcher = pattern.matcher(item.getMessage());
            if (matcher.find()){
                list.add(item);
            }
        }
        return list;
    }

    public List<Message> historyPeriod(LocalDate minDate, LocalDate maxDate) {

        List<Message> list = new ArrayList<>();

        for (Message item : historyMessage) {

            long time = item.getTimestamp();

            LocalDate date = LocalDate.parse(DateUtil.stringDate(time), DateUtil.getFormat());

            if (date.isAfter(minDate) && date.isBefore(maxDate)){
                list.add(item);
            }
        }
        return list;
    }
}