package by.lab.message;

import by.lab.message.Message;

import java.util.Comparator;

public class SortComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        return (int)Long.parseLong(o1.getTimestamp()) - (int)Long.parseLong(o2.getTimestamp());
    }
}