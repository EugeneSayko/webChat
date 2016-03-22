package by.lab.file;

import by.lab.message.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public List<Message> read(String filename) throws FileNotFoundException {
        ArrayList<Message> list;

        BufferedReader reader = new BufferedReader(new FileReader(filename));

        Type collectionType = new TypeToken<List<Message>>() {}.getType();

        Gson gson = new Gson();
        list = gson.fromJson(reader, collectionType);

        return list;
    }

    public void write(String filename, List<Message> listMessage) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(filename);
             PrintStream ps = new PrintStream(fos)
        ) {


            if (listMessage.size() != 0) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ps.println(gson.toJson(listMessage));

            } else {
                ps.println("not found messages");
            }

        }

    }
}