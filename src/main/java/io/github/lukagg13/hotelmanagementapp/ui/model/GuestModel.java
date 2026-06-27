package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import javafx.beans.property.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

public final class GuestModel {
    private static final Logger log = LoggerFactory.getLogger(GuestModel.class);

    private final SimpleStringProperty name;
    private final ObjectProperty<Integer> age;
    private UUID uuid;

    public GuestModel(Guest guest) {
        log.debug("Creating guest model for guest with UUID:{}, name:{}, age:{}",guest.uuid(), guest.name(), guest.age());

        uuid = guest.uuid();
        name = new SimpleStringProperty(guest.name());
        age = new SimpleObjectProperty<>(guest.age());
    }

    public GuestModel() {
        name = new SimpleStringProperty("");
        age = new SimpleObjectProperty<>(18);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getAge() {
        return age.get();
    }

    public ObjectProperty<Integer> ageProperty() {
        return age;
    }

    public Guest toGuest() {
        if (uuid == null) uuid = UUID.randomUUID();
        return new Guest(uuid, getName(), getAge());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GuestModel that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(age, that.age) && Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, uuid);
    }

    @Override
    public String toString() {
        return "GuestModel{" +
                "name=" + name +
                ", age=" + age +
                '}';
    }
}