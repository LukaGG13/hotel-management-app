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

    //TODO: finish
    public static Boolean check(UUID userId, String password) {

        log.debug("Checking password for UUID:{}", userId);
        var filePath = Path.of(userId.toString());
        if (!Files.exists(filePath)) return false;
        try(var fileReader = new FileReader(filePath.toFile())) {
            // TODO move to create account screen String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            var hashed = fileReader.readAllAsString();
            log.debug("Returning {}", BCrypt.checkpw(password, hashed));
            return BCrypt.checkpw(password, hashed);
        } catch(IOException _) {
            throw new RuntimeException("Something when wrong");
        }
    }

    public static void savePassword(UUID userId, String password) {
        log.debug("Saving password for UUID:{}", userId);
        var filePath = Path.of(userId.toString());
        try(var fileWriter = new FileWriter(filePath.toFile())) {
            // TODO move to create account screen String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            fileWriter.write(hashed);
        } catch (IOException e) {
            throw new RuntimeException("Cant write to file");
        }
    }
}
