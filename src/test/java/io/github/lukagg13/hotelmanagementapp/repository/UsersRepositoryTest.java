package io.github.lukagg13.hotelmanagementapp.repository;

import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.entity.Admin;
import io.github.lukagg13.hotelmanagementapp.entity.HotelStaffAccount;
import io.github.lukagg13.hotelmanagementapp.entity.User;
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

class UsersRepositoryTest {

    private Connection connection;
    private UsersRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        try {
            connection = DatabaseUtils.createConnection("src/test/java/resources/database.properties");
            try (var reader = new FileReader("src/test/java/resources/hotel_management_app_test.sql")) {
                RunScript.execute(connection, reader);
            }
            repository = new UsersRepository(connection);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Setup failed due to exception: " + e.getMessage());
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
        UUID newUuid = UUID.randomUUID();
        User newUser = new HotelStaffAccount(newUuid, "john_staff", 1);

        boolean isCreated = repository.create(newUser);
        assertTrue(isCreated, "User should be successfully created.");

        Optional<User> fetchedUser = repository.getWithUUID(newUuid);
        assertTrue(fetchedUser.isPresent());
        assertEquals("john_staff", fetchedUser.get().getUserName());
        assertEquals(1, fetchedUser.get().getRole());
        assertInstanceOf(HotelStaffAccount.class, fetchedUser.get());
    }

    @Test
    void testGetAll() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        repository.create(new Admin(uuid1, "admin1", 0));
        repository.create(new HotelStaffAccount(uuid2, "staff1", 1));

        List<User> users = repository.getAll();

        assertTrue(users.size() >= 2, "Should return at least the 2 users created in this test.");

        boolean foundAdmin = users.stream().anyMatch(u -> u.getUuid().equals(uuid1) && u instanceof Admin);
        boolean foundStaff = users.stream().anyMatch(u -> u.getUuid().equals(uuid2) && u instanceof HotelStaffAccount);

        assertTrue(foundAdmin, "Admin user should be in the returned list.");
        assertTrue(foundStaff, "Staff user should be in the returned list.");
    }

    @Test
    void testGetWithUUID() {
        UUID uuid = UUID.randomUUID();
        User admin = new Admin(uuid, "boss_admin", 0);
        repository.create(admin);

        Optional<User> result = repository.getWithUUID(uuid);

        assertTrue(result.isPresent());
        assertEquals("boss_admin", result.get().getUserName());
        assertInstanceOf(Admin.class, result.get());

        Optional<User> emptyResult = repository.getWithUUID(UUID.randomUUID());
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testUpdate() {
        UUID uuid = UUID.randomUUID();
        User staff = new HotelStaffAccount(uuid, "old_name", 1);
        repository.create(staff);

        User updatedStaff = new Admin(uuid, "new_name", 0);
        boolean isUpdated = repository.update(updatedStaff);

        assertTrue(isUpdated, "Update should return true on successful execution.");

        Optional<User> fetched = repository.getWithUUID(uuid);
        assertTrue(fetched.isPresent());
        assertEquals("new_name", fetched.get().getUserName());
        assertEquals(0, fetched.get().getRole());
        assertInstanceOf(Admin.class, fetched.get(), "Role mutation should change the instantiated class type.");
    }

    @Test
    void testDeleteWithUUID() {
        UUID uuid = UUID.randomUUID();
        User staff = new HotelStaffAccount(uuid, "temporary_user", 1);
        repository.create(staff);

        boolean isDeleted = repository.deleteWithUUID(uuid);
        assertTrue(isDeleted, "Delete operation should confirm row removal.");

        Optional<User> fetched = repository.getWithUUID(uuid);
        assertTrue(fetched.isEmpty(), "Deleted user should no longer exist in the database.");

        boolean deleteNonExistent = repository.deleteWithUUID(UUID.randomUUID());
        assertFalse(deleteNonExistent);
    }
}