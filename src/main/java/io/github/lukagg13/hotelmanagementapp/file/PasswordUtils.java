package io.github.lukagg13.hotelmanagementapp.file;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class PasswordUtils {

    public static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

    private PasswordUtils() {}

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
