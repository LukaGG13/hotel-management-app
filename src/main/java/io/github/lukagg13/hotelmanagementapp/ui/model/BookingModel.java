package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.entity.Booking;
import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingModel {

    private final ObjectProperty<UUID> bookingUUID = new SimpleObjectProperty<>(this, "bookingUUID");
    private final ObjectProperty<LocalDate> checkInDate = new SimpleObjectProperty<>(this, "checkInDate");
    private final ObjectProperty<LocalDate> checkOutDate = new SimpleObjectProperty<>(this, "checkOutDate");

    private final ObjectProperty<RoomModel> room = new SimpleObjectProperty<>(this, "room");
    private final ListProperty<GuestModel> guestList = new SimpleListProperty<>(this, "guestList", FXCollections.observableArrayList());

    public BookingModel(Booking booking) {
       bookingUUID.setValue(booking.uuid());
       checkInDate.setValue(booking.checkInDate());
       checkOutDate.setValue(booking.checkOutDate());
       room.setValue(new RoomModel(booking.room()));
       guestList.setAll(booking.guests().stream().map(GuestModel::new).toList());

    }

    public BookingModel() {
        bookingUUID.setValue(UUID.randomUUID());
        checkInDate.setValue(LocalDate.now());
        checkOutDate.setValue(LocalDate.now().plusDays(1));
    }

    public void setRoom(RoomModel roomModel) {
        room.setValue(roomModel);
    }

    public UUID getBookingUUID() {
        return bookingUUID.get();
    }

    public ObjectProperty<UUID> bookingUUIDProperty() {
        return bookingUUID;
    }

    public LocalDate getCheckInDate() {
        return checkInDate.get();
    }

    public ObjectProperty<LocalDate> checkInDateProperty() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate.get();
    }

    public ObjectProperty<LocalDate> checkOutDateProperty() {
        return checkOutDate;
    }

    public RoomModel getRoom() {
        return room.get();
    }

    public ObjectProperty<RoomModel> roomProperty() {
        return room;
    }

    public ObservableList<GuestModel> getGuestList() {
        return guestList.get();
    }

    public ListProperty<GuestModel> guestListProperty() {
        return guestList;
    }

    public Booking toBooking() {
        return new Booking(
                getBookingUUID(),
                getGuestList().stream().map(GuestModel::toGuest).collect(Collectors.toSet()),
                getRoom().toRoom(), //TODO: null pointer
                getCheckInDate(),
                getCheckOutDate()
        );
    }

    /*
    public BookingModel() {
        // Listen to UI room changes to keep the raw roomUUID synchronized
        this.room.addListener((_, _, newRoomModel) -> {
            setRoomUUID(newRoomModel == null ? null : newRoomModel.getUuid());
        });
    }

    public BookingModel(UUID bookingUUID, Set<Guest> guests, UUID roomUUID, LocalDate checkInDate, LocalDate checkOutDate) {
        this();
        setBookingUUID(bookingUUID);
        if (guests != null) {
            //setGuests(FXCollections.observableSet(guests));
            // Initialize UI wrappers from base entity data if needed
            this.guestList.setAll(guests.stream().map(GuestModel::new).toList());
        }
        setRoomUUID(roomUUID);
        setCheckInDate(checkInDate);
        setCheckOutDate(checkOutDate);
    }

    // --- bookingUUID Property ---
    public UUID getBookingUUID() { return bookingUUID.get(); }
    public void setBookingUUID(UUID value) { bookingUUID.set(value); }
    public ObjectProperty<UUID> bookingUUIDProperty() { return bookingUUID; }

    // --- guests Property (Raw Entity Set) ---
    public ObservableSet<Guest> getGuests() { return guests.get(); }
    public void setGuests(ObservableSet<Guest> value) { guests.set(value); }
    public SetProperty<Guest> guestsProperty() { return guests; }


    // --- roomUUID Property ---
    public UUID getRoomUUID() { return roomUUID.get(); }
    public void setRoomUUID(UUID value) { roomUUID.set(value); }
    public ObjectProperty<UUID> roomUUIDProperty() { return roomUUID; }

    // --- checkInDate Property ---
    public LocalDate getCheckIn() { return checkInDate.get(); }
    public void setCheckInDate(LocalDate value) { checkInDate.set(value); }
    public ObjectProperty<LocalDate> checkInProperty() { return checkInDate; }

    // --- checkOutDate Property ---
    public LocalDate getCheckOut() { return checkOutDate.get(); }
    public void setCheckOutDate(LocalDate value) { checkOutDate.set(value); }
    public ObjectProperty<LocalDate> checkOutProperty() { return checkOutDate; }

    // --- room Property (UI Wrapper used by Controller) ---
    public RoomModel getRoom() { return room.get(); }
    public void setRoom(RoomModel value) { room.set(value); }
    public ObjectProperty<RoomModel> roomProperty() { return room; }

    // --- guestList Property (UI Wrapper used by Controller) ---
    public ObservableList<GuestModel> getGuestList() { return guestList.get(); }
    public void setGuestList(ObservableList<GuestModel> value) { guestList.set(value); }
    public ListProperty<GuestModel> guestsProperty() { return guestList; }

    // --- Conversion to domain Entity for database serialization ---
    public Booking toBooking() {
        UUID finalId = getBookingUUID() == null ? UUID.randomUUID() : getBookingUUID();

        // Transform UI GuestModels back into a raw entity Set for the database
        Set<Guest> rawGuests = getGuestList().stream()
                .map(GuestModel::toGuest)
                .collect(Collectors.toSet());

        return new Booking(
                finalId,
                rawGuests,
                getRoomUUID(),
                getCheckIn(),
                getCheckOut()
        );
    }
    */
}