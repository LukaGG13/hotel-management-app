package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.entity.Booking;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Class used to display {@link Booking}'s in the app.
 */
public class BookingModel {

    //TODO: promjeni.
    private final ObjectProperty<UUID> bookingUUID = new SimpleObjectProperty<>(this, "bookingUUID");
    private final ObjectProperty<LocalDate> checkInDate = new SimpleObjectProperty<>(this, "checkInDate");
    private final ObjectProperty<LocalDate> checkOutDate = new SimpleObjectProperty<>(this, "checkOutDate");

    private final ObjectProperty<RoomModel> room = new SimpleObjectProperty<>(this, "room");
    private final ListProperty<GuestModel> guestList = new SimpleListProperty<>(this, "guestList", FXCollections.observableArrayList());

    /**
     * Creates a new instance of {@link BookingModel} from an existing {@link Booking}.
     * @param booking the {@link Booking} to copy values from.
     */
    public BookingModel(Booking booking) {
        bookingUUID.setValue(booking.uuid());
        checkInDate.setValue(booking.checkInDate());
        checkOutDate.setValue(booking.checkOutDate());
        room.setValue(new RoomModel(booking.room()));
        guestList.setAll(booking.guests().stream().map(GuestModel::new).toList());
    }

    /**
     * Creates a new instance of {@link BookingModel} with default values.
     */
    public BookingModel() {
        bookingUUID.setValue(UUID.randomUUID());
        checkInDate.setValue(LocalDate.now());
        checkOutDate.setValue(LocalDate.now().plusDays(1));
    }

    /**
     * Sets the room for this booking.
     * @param roomModel the room to set.
     */
    public void setRoom(RoomModel roomModel) {
        room.setValue(roomModel);
    }

    /**
     * Returns the booking UUID.
     * @return the booking UUID.
     */
    public UUID getBookingUUID() {
        return bookingUUID.get();
    }

    /**
     * Returns the booking UUID property.
     * @return the booking UUID property,
     */
    public ObjectProperty<UUID> bookingUUIDProperty() {
        return bookingUUID;
    }

    /**
     * Returns the check-in date.
     * @return the check-in date.
     */
    public LocalDate getCheckInDate() {
        return checkInDate.get();
    }

    /**
     * Returns the check-in date property.
     * @return the check-in date property.
     */
    public ObjectProperty<LocalDate> checkInDateProperty() {
        return checkInDate;
    }

    /**
     * Returns the check-out date.
     * @return the check-out date.
     */
    public LocalDate getCheckOutDate() {
        return checkOutDate.get();
    }

    /**
     * Returns the check-out date property.
     * @return the check-out date property.
     */
    public ObjectProperty<LocalDate> checkOutDateProperty() {
        return checkOutDate;
    }

    /**
     * Returns the room of this booking.
     * @return the room.
     */
    public RoomModel getRoom() {
        return room.get();
    }

    /**
     * Returns the room property.
     * @return the room property.
     */
    public ObjectProperty<RoomModel> roomProperty() {
        return room;
    }

    /**
     * Returns the list of guests in this booking.
     * @return the guest list.
     */
    public ObservableList<GuestModel> getGuestList() {
        return guestList.get();
    }

    /**
     * Returns the guest list property.
     * @return the guest list property.
     */
    public ListProperty<GuestModel> guestListProperty() {
        return guestList;
    }

    /**
     * Return a {@link Booking} from the {@link BookingModel}'s values.
     * @return a new {@link Booking} with the {@link BookingModel}'s values.
     */
    public Booking toBooking() {
        return new Booking(
                getBookingUUID(),
                getGuestList().stream().map(GuestModel::toGuest).collect(Collectors.toSet()),
                getRoom().toRoom(),
                getCheckInDate(),
                getCheckOutDate()
        );
    }
}