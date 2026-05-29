package io.github.lukagg13.hotelmanagementapp.database;

import org.example.java.entity.admin.Admin;
import org.example.java.entity.booking.Booking;
import org.example.java.entity.guest.Guest;
import org.example.java.entity.review.Review;
import org.example.java.entity.room.Room;
import org.example.java.entity.user.User;
import org.example.java.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class DatabaseUtils {

    private DatabaseUtils(){}

    private static final String USER_ID = "ID";
    private static final String USER_NAME_COLUMN = "IME";
    private static final String USER_AGE_COLUMN = "AGE";
    private static final String GUEST_ID = "USER_ID";
    private static final String ADMIN_ID = "USER_ID";
    private static final String SELECT_USERS = "SELECT %s, %s, %s FROM users".formatted(USER_ID, USER_NAME_COLUMN, USER_AGE_COLUMN);
    private static final String SELECT_USER = SELECT_USERS + " WHERE %s = ?".formatted(USER_ID);
    private static final String SELECT_GUEST = SELECT_USER + "JOIN GUESTS ON %s = %s".formatted(USER_ID, GUEST_ID);
    private static final String SELECT_ADMIN = SELECT_USER + "JOIN ADMIN ON %s = %s".formatted(USER_ID, ADMIN_ID);
    private static final String CHECK_IF_USER_IS_GUEST = "SELECT %s FROM GUEST WHERE %s = ?".formatted(GUEST_ID, GUEST_ID);
    private static final String CHECK_IF_USER_IS_ADMIN = "SELECT %s FROM ADMIN WHERE %s = ?".formatted(ADMIN_ID, ADMIN_ID);
    private static final String INSERT_USER = "INSERT INTO users (%s, %s, %s) VALUES(?,?,?)".formatted(USER_ID, USER_NAME_COLUMN, USER_AGE_COLUMN);
    private static final String UPDATE_USER = "UPDATE users SET %s = ?, %s = ? WHERE %s = ?;".formatted(USER_NAME_COLUMN, USER_AGE_COLUMN, USER_ID);
    private static final String DELETE_USER = "DELETE FROM users WHERE %s = ?;".formatted(USER_ID);
    private static final String INSERT_ADMIN = "INSERT INTO admin (USER_ID) VALUES(?)";
    private static final String INSERT_GUEST = "INSERT INTO guest (USER_ID) VALUES(?)";

    private static final String ROOM_ID = "ID";
    private static final String ROOM_NUMBER_OF_BEDS = "num_of_beds";
    private static final String ROOM_SIZE_IN_SQUARE_METERS = "size_in_sqr_m";
    private static final String ROOM_PRICE_PER_NIGHT = "price_per_night";
    private static final String ROOM_DISTANCE_FROM_CITY_CENTER = "distance_from_city_center";
    private static final String ROOM_DISTANCE_FROM_BEACH = "distance_from_beach";
    private static final String ROOM_NUMBER = "room_number";
    private static final String ROOM_AMENITIES = "amenities";

    private static final String SELECT_ROOMS =
            "SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM ROOMS"
                    .formatted(
                            ROOM_ID,
                            ROOM_NUMBER_OF_BEDS,
                            ROOM_SIZE_IN_SQUARE_METERS,
                            ROOM_PRICE_PER_NIGHT,
                            ROOM_DISTANCE_FROM_CITY_CENTER,
                            ROOM_DISTANCE_FROM_BEACH,
                            ROOM_NUMBER,
                            ROOM_AMENITIES
                    );
    private static final String INSERT_ROOM =
            "INSERT INTO ROOMS (%s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                    .formatted(
                            ROOM_ID,
                            ROOM_NUMBER_OF_BEDS,
                            ROOM_SIZE_IN_SQUARE_METERS,
                            ROOM_PRICE_PER_NIGHT,
                            ROOM_DISTANCE_FROM_CITY_CENTER,
                            ROOM_DISTANCE_FROM_BEACH,
                            ROOM_NUMBER,
                            ROOM_AMENITIES
                    );

    private static final String REVIEW_ID = "ID";
    private static final String REVIEW_GUEST_ID = "GUEST_ID";
    private static final String REVIEW_TEXT = "REVIEW_TEXT";
    private static final String REVIEW_DATE = "DATE_OF_REVIEW";
    private static final String REVIEW_RATING = "RATING";
    private static final String REVIEW_CREATED_AT = "CREATED_AT";

    private static final String SELECT_REVIEWS = "SELECT %s, %s, %s, %s, %s, %s FROM reviews".formatted(REVIEW_ID, REVIEW_GUEST_ID, REVIEW_TEXT, REVIEW_DATE, REVIEW_RATING, REVIEW_CREATED_AT);
    private static final String INSERT_REVIEW = "INSERT INTO reviews (%s, %s, %s) VALUES(?,?,?)".formatted(REVIEW_ID, REVIEW_GUEST_ID, REVIEW_TEXT);

    // Booking constants
    private static final String BOOKING_ID = "ID";
    private static final String BOOKING_ROOM_ID = "ROOM_ID";
    private static final String BOOKING_USER_ID = "USER_ID";
    private static final String BOOKING_CHECK_IN = "CHECK_IN";
    private static final String BOOKING_CHECK_OUT = "CHECK_OUT";

    private static final String SELECT_BOOKINGS =
            "SELECT %s, %s, %s, %s, %s FROM bookings"
                    .formatted(BOOKING_ID, BOOKING_ROOM_ID, BOOKING_USER_ID, BOOKING_CHECK_IN, BOOKING_CHECK_OUT);
    private static final String INSERT_BOOKING =
            "INSERT INTO bookings (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)"
                    .formatted(BOOKING_ID, BOOKING_ROOM_ID, BOOKING_USER_ID, BOOKING_CHECK_IN, BOOKING_CHECK_OUT);
    private static final String UPDATE_BOOKING =
            "UPDATE bookings SET %s = ?, %s = ?, %s = ? WHERE %s = ? AND %s = ?"
                    .formatted(BOOKING_CHECK_IN, BOOKING_CHECK_OUT, BOOKING_USER_ID, BOOKING_ROOM_ID, BOOKING_USER_ID);
    private static final String DELETE_BOOKING =
            "DELETE FROM bookings WHERE %s = ? AND %s = ?"
                    .formatted(BOOKING_ROOM_ID, BOOKING_USER_ID);


    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final String DATABASE_FILE = "src/main/resources/database.properties";

    public static Connection createConnection() throws DatabaseException, IOException {
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

    public static void closeConnection(Connection conn) throws DatabaseException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private static User consturctUserFromResultSet(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString(USER_ID));
        Integer age = rs.getInt(USER_AGE_COLUMN);
        String name = rs.getString(USER_NAME_COLUMN);

        try(var conn = createConnection();
            var pstsm = conn.prepareStatement(CHECK_IF_USER_IS_ADMIN)
        ) {
            pstsm.setString(1, id.toString());
            var adminRs = pstsm.executeQuery();
            log.debug("creating admin with uuid {}", id );
            if (adminRs.next()) return new Admin(id, name, age);
        } catch(IOException _) {
            throw new DatabaseException("Can't connect to database");
        }
        log.debug("creating guest with uuid {}", id );
        return new Guest(id, name, age);
    }

    public static User getUserWithUUID(UUID uuid) throws DatabaseException, IOException {
        log.info("Fetching user from db with uuid:{}", uuid);

        try (   var conn = createConnection();
                var preparedStatement = conn.prepareStatement(SELECT_USER);
        ) {
            preparedStatement.setString(1, uuid.toString());
            var rs = preparedStatement.executeQuery();
            if(rs.next()) {
                return consturctUserFromResultSet(rs);
            }
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
        //TODO: what to do igs throw
        throw new IllegalArgumentException("User with this UUID doesn't exist");
    }

    public static List<User> getAllUsers() throws DatabaseException, IOException {
        log.info("Fetching users from db");
        List<User> users = new ArrayList<>();

        try (   var conn = createConnection();
                var preparedStatement = conn.prepareStatement(SELECT_USERS);
                var rs = preparedStatement.executeQuery()
        ) {
            while(rs.next()) {
                users.add(consturctUserFromResultSet(rs));
            }
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }

        return users;
    }

    private static void saveNewAdmin(Admin admin) throws DatabaseException, IOException{
        log.info("Adding admin into db");
        try ( var conn = createConnection();
              var pstmt = conn.prepareStatement(INSERT_ADMIN)
        )  {
            pstmt.setString(1, admin.getId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private static void saveNewGuest(Guest guest) throws DatabaseException, IOException{
        log.info("Adding guest into db");
        try ( var conn = createConnection();
              var pstmt = conn.prepareStatement(INSERT_GUEST)
        )  {
            pstmt.setString(1, guest.getId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void saveNewUser(User user) throws DatabaseException, IOException {
        log.info("Adding users into db");

        try (   var conn = createConnection();
                var pstmt = conn.prepareStatement(INSERT_USER)){
            pstmt.setString(1, user.getId().toString());
            pstmt.setString(2, user.getName());
            pstmt.setInt(3, user.getAge());
            pstmt.executeUpdate();

            if(user instanceof Admin admin) saveNewAdmin(admin);
            if(user instanceof Guest guest) saveNewGuest(guest);
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void updateUser(User user) throws DatabaseException, IOException {
        log.info("Updating user {} into db", user);

        try (   var conn = createConnection();
                var pstmt = conn.prepareStatement(UPDATE_USER)){
            pstmt.setString(1, user.getName());
            pstmt.setInt(2, user.getAge());
            pstmt.setString(3, user.getId().toString());
            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void deleteUser(User user) throws DatabaseException, IOException {
        log.info("Deleting user {} into db", user);

        try (   var conn = createConnection();
                var pstmt = conn.prepareStatement(DELETE_USER)){
            pstmt.setString(1, user.getId().toString());
            log.debug("delete to string" + pstmt.toString());
            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static List<Room> getAllRooms() throws DatabaseException, IOException {
        List<Room> rooms = new ArrayList<>();

           try (    var conn = createConnection();
                    var pstm = conn.prepareStatement(SELECT_ROOMS);
                    var rs = pstm.executeQuery()
           ){
               while (rs.next()) {

                   //TODO all fields and switch string with string constant variables defined above
                   var id = UUID.fromString(rs.getString(ROOM_ID));
                   var numberOfBeds = rs.getInt(ROOM_NUMBER_OF_BEDS);
                   var sizeInSqrM = rs.getInt(ROOM_SIZE_IN_SQUARE_METERS);
                   var pricePerNight = rs.getBigDecimal(ROOM_PRICE_PER_NIGHT);
                   var distanceFromCityCenter = rs.getBigDecimal(ROOM_DISTANCE_FROM_CITY_CENTER);
                   var distanceFromBeach = rs.getBigDecimal(ROOM_DISTANCE_FROM_BEACH);
                   var roomNumber = rs.getInt(ROOM_NUMBER);
                   //TODO test
                   var amenites = rs.getArray(ROOM_AMENITIES);

                   var roomBuilder = new Room.RoomBuilder(id, numberOfBeds, pricePerNight)
                           .sizeInSqrM(sizeInSqrM)
                           .distanceFromCityCenter(distanceFromCityCenter)
                           .roomNumber(roomNumber)
                           .distanceFromBeach(distanceFromBeach);
                   var amentiesResultSet = amenites.getResultSet();
                   while(amentiesResultSet.next()){
                       var amenity = Room.Amenity.valueOf(amentiesResultSet.getString(2));
                       roomBuilder.addAmenity(amenity);
                   }
                   rooms.add(roomBuilder.build());
               }
               return rooms;
           } catch (SQLException e) {
               throw new DatabaseException(e);
           }
    }

    public static User getLastCreateUser() throws DatabaseException, IOException {
        try (   var conn = createConnection();
                var pstm = conn.prepareStatement("select * from users order by created_at desc limit 1 ;");
                var rs = pstm.executeQuery();
        ) {

            if (rs.next()) {
                return consturctUserFromResultSet(rs);
            } else {
                throw new DatabaseException("No user");
            }
            /*
            for (int i = 0; i < 10; i++) {
                int taskNum = i;
                executor.submit(() -> {
                    System.out.println("⚡ Task " + taskNum +
                            " na " + Thread.currentThread());
                });
            }

             */
        } catch (SQLException e) {
           throw  new DatabaseException(e);
        }
    }

    public static void saveNewRoom(Room room) throws DatabaseException, IOException {
        log.info("Adding room into db");

        try (   var conn = createConnection();
                var pstmt = conn.prepareStatement(INSERT_ROOM)
        ){
            pstmt.setString(1, room.getId().toString());
            pstmt.setInt(2, room.getNumOfBeds());
            pstmt.setInt(3, room.getSizeInSqrM());
            pstmt.setBigDecimal(4, room.getPricePerNight());
            pstmt.setBigDecimal(5, room.getDistanceFromCityCenter());
            pstmt.setBigDecimal(6, room.getDistanceFromBeach());
            pstmt.setInt(7, room.getRoomNumber());
            //TODO: da manje izgleda ko chat kod
            Array sqlArray = conn.createArrayOf(
                    "VARCHAR", // H2 stores ENUM as VARCHAR internally
                    room.getAmenities()
                            .stream()
                            .map(Enum::name)
                            .toArray(String[]::new)
            );

            pstmt.setArray(8, sqlArray);


            pstmt.executeUpdate();
        } catch(SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static List<Review> getAllReviews() throws DatabaseException, IOException {
        log.info("Getting reviews from db");
        var reviews = new ArrayList<Review>();

        try (var conn = createConnection();
             var pstms = conn.prepareStatement(SELECT_REVIEWS);
             var rs = pstms.executeQuery()
        ) {
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString(REVIEW_ID));
                UUID guestId = UUID.fromString(rs.getString(REVIEW_GUEST_ID));
                Guest guest = (Guest) getUserWithUUID(guestId); //TODO: wtf fix this igs zasebno za admin i guest al puno koda
                String reviewText = rs.getString(REVIEW_TEXT);
                LocalDate date = rs.getDate(REVIEW_DATE).toLocalDate();
                Integer rating = rs.getInt(REVIEW_RATING);

               reviews.add(new Review(uuid, guest, reviewText, date, rating));
            }
        } catch (SQLException e){
            throw new DatabaseException(e);
        }
        return reviews;
    }

    public static List<Booking> getAllBookings() throws DatabaseException, IOException {
        log.info("Getting bookings from db");
        var bookings = new ArrayList<Booking>();

        try (var conn = createConnection();
             var pstms = conn.prepareStatement(SELECT_BOOKINGS);
             var rs = pstms.executeQuery()
        ) {
            while (rs.next()) {
                UUID roomId = UUID.fromString(rs.getString(BOOKING_ROOM_ID));
                UUID userId = UUID.fromString(rs.getString(BOOKING_USER_ID));
                LocalDateTime checkIn = rs.getTimestamp(BOOKING_CHECK_IN).toLocalDateTime();
                LocalDateTime checkOut = rs.getTimestamp(BOOKING_CHECK_OUT).toLocalDateTime();

                User user = getUserWithUUID(userId);
                List<Room> rooms = getAllRooms();
                Room room = rooms.stream()
                        .filter(r -> r.getId().equals(roomId))
                        .findFirst()
                        .orElseThrow(() -> new DatabaseException("Room not found for booking"));

                bookings.add(new Booking(room, user, checkIn, checkOut));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return bookings;
    }

    public static void saveNewBooking(Booking booking) throws DatabaseException, IOException {
        log.info("Adding booking into db");

        try (var conn = createConnection();
             var pstmt = conn.prepareStatement(INSERT_BOOKING)
        ) {
            UUID bookingId = UUID.randomUUID();
            pstmt.setString(1, bookingId.toString());
            pstmt.setString(2, booking.room().getId().toString());
            pstmt.setString(3, booking.user().getId().toString());
            pstmt.setTimestamp(4, Timestamp.valueOf(booking.checkIn()));
            pstmt.setTimestamp(5, Timestamp.valueOf(booking.checkOut()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void deleteBooking(Booking booking) throws DatabaseException, IOException {
        log.info("Deleting booking from db");

        try (var conn = createConnection();
             var pstmt = conn.prepareStatement(DELETE_BOOKING)
        ) {
            pstmt.setString(1, booking.room().getId().toString());
            pstmt.setString(2, booking.user().getId().toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public static void updateBooking(Booking booking) throws DatabaseException, IOException {
        log.info("Updating booking in db");

        try (var conn = createConnection();
             var pstmt = conn.prepareStatement(UPDATE_BOOKING)
        ) {
            pstmt.setTimestamp(1, Timestamp.valueOf(booking.checkIn()));
            pstmt.setTimestamp(2, Timestamp.valueOf(booking.checkOut()));
            pstmt.setString(3, booking.user().getId().toString());
            pstmt.setString(4, booking.room().getId().toString());
            pstmt.setString(5, booking.user().getId().toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}