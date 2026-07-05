package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

/**
 * Class used to display {@link Guest}'s in the app.
 */
public class GuestModel {
    private static final Logger log = LoggerFactory.getLogger(GuestModel.class);

    private final SimpleStringProperty name;
    private final ObjectProperty<Integer> age;
    private UUID uuid;

    /**
     * Creates a new instance of {@link GuestModel} from an existing {@link Guest}.
     * @param guest the {@link Guest} to copy values from.
     */
    public GuestModel(Guest guest) {
        log.debug("Creating guest model for guest with UUID:{}, name:{}, age:{}", guest.uuid(), guest.name(), guest.age());

        uuid = guest.uuid();
        name = new SimpleStringProperty(guest.name());
        age = new SimpleObjectProperty<>(guest.age());
    }

    /**
     * Creates a new instance of {@link GuestModel} with default values.
     */
    public GuestModel() {
        name = new SimpleStringProperty("");
        age = new SimpleObjectProperty<>(18);
    }

    /**
     * Returns the {@link Guest}'s name.
     * @return the name.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Returns the name property.
     * @return the name property.
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Returns the {@link Guest}'s age.
     * @return the age.
     */
    public int getAge() {
        return age.get();
    }

    /**
     * Returns the age property.
     * @return the age property.
     */
    public ObjectProperty<Integer> ageProperty() {
        return age;
    }

    /**
     * Returns a {@link Guest} from the {@link GuestModel}.
     * @return a new {@link Guest}.
     */
    public Guest toGuest() {
        if (uuid == null) uuid = UUID.randomUUID();
        return new Guest(uuid, getName(), getAge());
    }

    /**
     * Checks if equal.
     * @param o the object to be checked with.
     * @return true if equal else false.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GuestModel that)) return false;
        return Objects.equals(name, that.name)
                && Objects.equals(age, that.age)
                && Objects.equals(uuid, that.uuid);
    }

    /**
     * Returns the hash code.
     * @return hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, age, uuid);
    }

    /**
     * Returns a string representation of this model.
     * @return string representation.
     */
    @Override
    public String toString() {
        return "GuestModel{" +
                "name=" + name +
                ", age=" + age +
                '}';
    }
}