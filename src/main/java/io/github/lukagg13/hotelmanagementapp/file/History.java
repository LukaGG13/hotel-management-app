package io.github.lukagg13.hotelmanagementapp.file;


import java.io.Serializable;
import java.nio.file.Path;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class History {

    private static final Path DATA_FILE_PATH = Paths.get("history", "history_data.dat");

    static {
        try {
            Files.createDirectories(DATA_FILE_PATH.getParent());
        } catch (IOException e) {
            System.err.println("Failed to initialize history directory: " + e.getMessage());
        }
    }

    private History() {}

    /**
     * Appends a record entry into the single binary tracking file.
     */
    public static boolean appendLog(String user, Serializable oldState, Serializable newState) throws IOException {
        return Thread.ofVirtual().start(() -> {
            List<HistoryRecordLog> currentLogs;
            try {
                currentLogs = readAllLogs();
                currentLogs.add(new HistoryRecordLog(user, java.time.LocalDateTime.now(), oldState, newState));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(DATA_FILE_PATH))) {
                oos.writeObject(currentLogs);
                //return true;
            } catch (IOException e) {
                System.err.println("Failed to write binary history records: " + e.getMessage());
                //return false;
            }
        });
    }

    /**
     * Reads the entire sequence of historical records from the single data file.
     */
    public static List<HistoryRecordLog> readAllLogs() throws IOException {
        if (!Files.exists(DATA_FILE_PATH) || Files.size(DATA_FILE_PATH) == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(DATA_FILE_PATH))) {
            return (List<HistoryRecordLog>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reconstructing binary records file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}