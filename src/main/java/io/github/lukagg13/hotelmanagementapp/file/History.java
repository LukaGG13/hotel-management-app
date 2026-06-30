package io.github.lukagg13.hotelmanagementapp.file;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A class used for writhing the history of the changes in the app.
 */
public final class History {

    private static final Path DATA_FILE_PATH = Paths.get("history", "history_data.dat");
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Logger log = LoggerFactory.getLogger(History.class);


    /**
     * A private constructor to hide the implicit one.
     */
    private History() {}

    /**
     * A function used to add a {@link HistoryRecordLog} to the history file.
     * @param historyRecordLog The {@link HistoryRecordLog} to be added.
     */
    public static void addLog(HistoryRecordLog historyRecordLog) {
        Thread.ofVirtual().start(() -> {
            lock.writeLock().lock();
            try {
                var currentLogs = readAllLogs();
                currentLogs.add(historyRecordLog);

                try (var objectOutputStream = new ObjectOutputStream(Files.newOutputStream(DATA_FILE_PATH))) {
                    objectOutputStream.writeObject(currentLogs);
                }
            } catch (IOException _) {
                log.error("Failed to save log {} to {}", historyRecordLog, DATA_FILE_PATH);
            } finally {
                lock.writeLock().unlock();
            }
        });
    }

    /**
     * Reads all the history logs and returns them as a {@link List}.
     * @return A {@link List} of {@link HistoryRecordLog}.
     */
    public static List<HistoryRecordLog> readAllLogs() {
        lock.readLock().lock();
        try (var objectInputStream = new ObjectInputStream(Files.newInputStream(DATA_FILE_PATH))) {
            return (List<HistoryRecordLog>) objectInputStream.readObject();
        } catch (IOException _) {
            log.error("Error reading logs from {}", DATA_FILE_PATH);
        } catch (ClassNotFoundException e) {
            log.error("Error reading logs {}", e.getMessage());
        }
         finally {
            lock.readLock().unlock();
        }
        return new ArrayList<>();
    }
}