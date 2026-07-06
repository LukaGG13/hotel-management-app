package io.github.lukagg13.hotelmanagementapp.file;

import io.github.lukagg13.hotelmanagementapp.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Utility class used to manage passwords for the app.
 */
public class PasswordUtils {

    public static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

    /**
     * Private constructor to hide the implicit one.
     */
    private PasswordUtils() {}

    /**
     * Checks if a password matches a corresponding {@link UUID}.
     * @param userId The {@link UUID} of the {@link User}
     * @param password The password the {@link User} is trying to log in with
     * @return True if successful else false
     */
    public static boolean check(UUID userId, String password) {

        log.debug("Checking password for UUID:{}", userId);
        var filePath = Path.of(userId.toString());
        if (!Files.exists(filePath)) return false;
        try(var fileReader = new FileReader(filePath.toFile())) {
            var hashed = fileReader.readAllAsString();
            log.debug("Returning {}", BCrypt.checkpw(password, hashed));
            return BCrypt.checkpw(password, hashed);
        } catch(IOException _) {
            log.error("Can't read file {}", filePath);
            return false;
        }
    }

    /**
     * Saves a password for the corresponding {@link UUID}.
     * @param userId The {@link UUID} of the user password that will be saved.
     * @param password The password that will be hashed and then saved.
     */
    public static void savePassword(UUID userId, String password) {
        log.debug("Saving password for UUID:{}", userId);
        var filePath = Path.of(userId.toString());
        try(var fileWriter = new FileWriter(filePath.toFile())) {
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            fileWriter.write(hashed);
        } catch (IOException _) {
            log.error("Can't read file {}", filePath);
        }
    }
}
