package by.lab.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static String stringDate(long time){

        Date date = new Date();
        date.setTime(time);
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");

        return dateFormat.format(date);

    }

    public static long getPresentTime() {

        Date date = new Date();
        return date.getTime();

    }

    public static DateTimeFormatter getFormat(){

        return DateTimeFormatter.ofPattern("dd MM yyyy");

    }
}