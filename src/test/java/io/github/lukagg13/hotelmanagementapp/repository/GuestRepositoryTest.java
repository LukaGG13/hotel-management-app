package io.github.lukagg13.hotelmanagementapp.repository;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GuestRepositoryTest {

    private Connection connection;
    private GuestRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        try {
            connection = DatabaseUtils.createConnection("src/test/java/resources/database.properties");
            try (var reader = new FileReader("src/test/java/resources/hotel_management_app_test.sql")) {
                RunScript.execute(connection, reader);
            }
            repository = new GuestRepository(connection);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) {
                try (var stmt = connection.createStatement()) {
                    stmt.execute("DROP ALL OBJECTS");
                }
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreate() {
        var newUuid = UUID.randomUUID();
        var name = "Chud Maximus";
        var age = 20;

        var newGuest = new Guest(newUuid, name, age);

        boolean isCreated = repository.create(newGuest);
        assertTrue(isCreated, "User should be successfully created");

        Optional<Guest> fetchedUser = repository.getWithUUID(newUuid);
        assertTrue(fetchedUser.isPresent());
        assertEquals(name, fetchedUser.get().name());
        assertEquals(age, fetchedUser.get().age());
    }

    @Test
    void testGetAll() {
        var uuid1 = UUID.randomUUID();
        var uuid2 = UUID.randomUUID();

        var name1 = "Larping Larry";
        var name2 = "Lol Cow";

        var age1 = 67;
        var age2 = 23;

        repository.create(new Guest(uuid1, name1 , age1));
        repository.create(new Guest(uuid2, name2 , age2));

        List<Guest> guests = repository.getAll();

        assertTrue(guests.size() >= 2, "Should return at least the 2 guests created in this test");

        boolean foundGuest1 = guests.stream().anyMatch(u -> u.uuid().equals(uuid1));
        boolean foundGuest2 = guests.stream().anyMatch(u -> u.uuid().equals(uuid2));

        assertTrue(foundGuest1, "Guest1 should be in the returned list");
        assertTrue(foundGuest2, "Guest2 should should be in the returned list");
    }

    @Test
    void testGetWithUUID() {
        UUID uuid = UUID.randomUUID();
        Guest admin = new Guest(uuid, "Tun tun sahur", 20);
        repository.create(admin);

        Optional<Guest> result = repository.getWithUUID(uuid);

        assertTrue(result.isPresent());
        assertEquals("Tun tun sahur", result.get().name());

        Optional<Guest> emptyResult = repository.getWithUUID(UUID.randomUUID());
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testUpdate() {
        var uuid = UUID.randomUUID();
        var guest = new Guest(uuid, "Tun Tun sahur", 17);
        repository.create(guest);

        Guest updatedGuest = new Guest(uuid, "Triple T", 21);
        boolean isUpdated = repository.update(updatedGuest);

        assertTrue(isUpdated, "Update should return true on successful execution");

        Optional<Guest> fetched = repository.getWithUUID(uuid);
        assertTrue(fetched.isPresent());
        assertEquals("Triple T", fetched.get().name());
        assertEquals(21, fetched.get().age());
    }

    @Test
    void testDeleteWithUUID() {
        var uuid = UUID.randomUUID();
        var temp = new Guest(uuid, "temp", 1);
        repository.create(temp);

        boolean isDeleted = repository.deleteWithUUID(uuid);
        assertTrue(isDeleted, "Delete operation should confirm row removal");

        Optional<Guest> fetched = repository.getWithUUID(uuid);
        assertTrue(fetched.isEmpty(), "Deleted user should no longer exist in the database");

        boolean deleteNonExistent = repository.deleteWithUUID(UUID.randomUUID());
        assertFalse(deleteNonExistent);
    }
}