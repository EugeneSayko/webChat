package by.lab.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateWork {
    public static String dateToString(String when) {
        Date date = new Date();
        date.setTime(Long.parseLong(when));
        return date.toString();
    }

    public static String stringDate(long time){

        Date date = new Date();
        date.setTime(time);
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
        return dateFormat.format(date);

    }

    public static String getPresentTime() {
        Date date = new Date();
        return String.valueOf(date.getTime());
    }
}