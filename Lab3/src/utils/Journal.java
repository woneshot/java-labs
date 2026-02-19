package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Journal {
    private static final String LOG_FILE = "journal.log";

    public static void log(String action) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String time = LocalDateTime.now().toString();
            writer.write("[" + time + "] " + action + "\n");
        } catch (IOException e) {
            System.err.println("Не удалось записать в журнал: " + e.getMessage());
        }
    }
}