package by.lab.message;

import java.util.Comparator;

public class SortComparator implements Comparator<Message> {

    @Override
    public int compare(Message o1, Message o2) {
        return (int)o1.getTimestamp() - (int)o2.getTimestamp();
    }

}