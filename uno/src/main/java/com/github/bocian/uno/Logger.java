package com.github.bocian.uno;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public abstract class Logger {
    private static final String LOG_FILE = "log.txt";
    private static BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, false));
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    private static void log(String message) {
        try {
            if (writer != null) {
                writer.write(message);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }

    public static void logEvent(String event) {
        String timestamp = java.time.LocalDateTime.now().toString();
        log("[" + timestamp + "] " + event);
    }
}
