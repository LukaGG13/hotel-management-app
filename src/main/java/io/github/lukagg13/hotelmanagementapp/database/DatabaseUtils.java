package io.github.lukagg13.hotelmanagementapp.database;

import io.github.lukagg13.hotelmanagementapp.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A utility class for interacting with the database.
 */
public class DatabaseUtils {
    private DatabaseUtils() {}

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final String DATABASE_FILE = "src/main/resources/database.properties";

    /**
     * Creates and returns a new Database connection.
     * @return a {@link Connection} object.
     * @throws DatabaseException if there is an error establishing the connection.
     * @throws IOException if the database.properties doesn't exist.
     */
    public static Connection createConnection() throws DatabaseException, IOException {
        log.info("Creating connection to database.");
        try (var reader = new FileReader(DATABASE_FILE)) {

            var properties = new Properties();
            properties.load(reader);

            var url  = properties.getProperty("url");
            var user = properties.getProperty("username");
            var pass = properties.getProperty("password");

            return DriverManager.getConnection(url, user, pass);
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Creates and returns a new Database connection from the specified database.properties file.
     * @param filePath a {@link String} path to the database.properties file we want to use to connect to the database.
     * @return a {@link Connection} object.
     * @throws DatabaseException if a SQL error happens.
     * @throws IOException if the {@code filePath} isn't valid.
     */
    public static Connection createConnection(String filePath) throws DatabaseException, IOException {
        log.info("Creating connection to database with filePath: {}", filePath);
        try (var reader = new FileReader(filePath)) {

            var properties = new Properties();
            properties.load(reader);

            var url  = properties.getProperty("url");
            var user = properties.getProperty("username");
            var pass = properties.getProperty("password");

            return DriverManager.getConnection(url, user, pass);
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }
}