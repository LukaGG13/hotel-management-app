package io.github.lukagg13.hotelmanagementapp.database;

import org.example.java.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

import static org.example.java.database.DatabaseUtils.createConnection;

public class DatabaseHelper {
    private DatabaseHelper() {}
    private static final Logger log = LoggerFactory.getLogger(DatabaseHelper.class);

    public static void createTables() throws DatabaseException, IOException, SQLException {
        try (var connection = createConnection()) {
            log.debug("Creating table users");
            var preparedStatement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS users (
                                      id UUID,
                                      ime VARCHAR(50) NOT NULL,
                                      age INT NOT NULL,
                                      CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      PRIMARY KEY (id)
                                  );              
                    """);
            preparedStatement.executeUpdate();


           connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS admin (
                               user_id UUID PRIMARY KEY,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                           );
                    """).executeUpdate();

            connection.prepareStatement("""
                           CREATE TABLE IF NOT EXISTS guest (
                               user_id UUID PRIMARY KEY,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                           );
                    """).executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS reviews (
                                     ID UUID NOT NULL PRIMARY KEY,
                                     GUEST_ID UUID NOT NULL,
                                     REVIEW_TEXT VARCHAR(255) NOT NULL,
                                     DATE_OF_REVIEW DATE NOT NULL,
                                     RATING TINYINT NOT NULL,
                                     CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\s
                                     FOREIGN KEY (GUEST_ID) REFERENCES guest(USER_ID) ON DELETE CASCADE,
                                     CHECK (1 <= RATING AND RATING <= 10)
                    );
                """).executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS rooms (
                          id UUID NOT NULL PRIMARY KEY,
                          num_of_beds INT NOT NULL,
                          size_in_sqr_m INT NOT NULL,
                          price_per_night DECIMAL(9, 2) NOT NULL,
                          distance_from_city_center DECIMAL(9, 2) NOT NULL,
                          distance_from_beach DECIMAL(9, 2) NOT NULL,
                          room_number INT NOT NULL,
                          amenities ENUM('GYM', 'WIFI', 'POOL', 'PARKING', 'SPA', 'BREAKFAST') ARRAY
                                                                        );
                        """).executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS bookings (
                    id UUID NOT NULL PRIMARY KEY,
                    room_id UUID NOT NULL,
                    user_id UUID NOT NULL,
                    check_in DATE NOT NULL,
                    check_out DATE NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
                        );
                    """).executeUpdate();
            /*
            connection.prepareStatement("""
                        CREATE TABLE IF NOT EXISTS amenities (
                            id INT AUTO_INCREMENT PRIMARY KEY,          
                            name VARCHAR(50) NOT NULL UNIQUE 
                        );
                    """).executeUpdate();
             */

            /*
            connection.prepareStatement("""
                    INSERT IGNORE INTO amenities (name)
                    VALUES
                        ('GYM'),
                        ('WIFI'),
                        ('POOL'),
                        ('PARKING'),
                        ('SPA'),
                        ('BREAKFAST');
                    """).executeUpdate();

             */
            /*
            connection.prepareStatement("""
                        CREATE TABLE IF NOT EXISTS room_amenities (
                            room_id INT NOT NULL,
                            amenity_id INT NOT NULL,
                            PRIMARY KEY (room_id, amenity_id),
                            FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
                            FOREIGN KEY (amenity_id) REFERENCES amenities(id) ON DELETE CASCADE
                        );
                    """).executeUpdate();
             */
        }
    }
}
