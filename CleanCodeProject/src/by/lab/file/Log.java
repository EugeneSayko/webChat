package by.lab.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Log {
    private static final String LOG_FILE_NAME = "src/by/lab/logfile.txt";

    public static void in(String text) {

        try(FileOutputStream fileOutputStream = new FileOutputStream(LOG_FILE_NAME, true);
            PrintStream printStream = new PrintStream(fileOutputStream)
                ) {

            printStream.println(text);

        }catch (IOException e) {
            Log.in("error in text to log!" + e.toString());
        }
    }

    public static void deleteLogFile() {

        File file = new File(LOG_FILE_NAME);

        if(!file.delete()){
            System.out.println("error delete file");
        }

    }
}