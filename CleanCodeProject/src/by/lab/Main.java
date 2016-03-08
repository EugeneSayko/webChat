package by.lab;

import by.lab.file.Log;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Log.deleteLogFile();
        Processing processing = new Processing();
        try {
            processing.start();
            Log.in("program completion");
        }
        catch (IOException e) {
            Log.in(e.toString());
            e.printStackTrace();
        }
    }
}