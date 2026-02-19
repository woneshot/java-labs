package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class Journal {
    private static final String LOG_FILE = "journal.log";

    //теперь храним в связном списке
    private static final LinkedList<String> logHistory = new LinkedList<>();

    public static void log(String action) {
        String time = LocalDateTime.now().toString();
        String entry = "[" + time + "] " + action;

        logHistory.add(entry); //добавляем в список

        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(entry + "\n");
        } catch (IOException e) {
            System.err.println("Не удалось записать в журнал: " + e.getMessage());
        }
    }
}