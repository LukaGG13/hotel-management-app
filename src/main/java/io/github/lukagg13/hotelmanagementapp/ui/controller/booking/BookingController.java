package io.github.lukagg13.hotelmanagementapp.ui.controller.booking;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.repository.GuestRepository;
import io.github.lukagg13.hotelmanagementapp.repository.RoomRepository;
import io.github.lukagg13.hotelmanagementapp.service.BookingService;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.model.BookingModel;
import io.github.lukagg13.hotelmanagementapp.database.DatabaseUtils;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingController {

    @FXML private TableView<BookingModel> bookingTable;
    @FXML private TableColumn<BookingModel, UUID> id;
    @FXML private TableColumn<BookingModel, String> roomNumber; // Switched to String/Integer to show the nested data
    @FXML private TableColumn<BookingModel, LocalDate> checkIn;
    @FXML private TableColumn<BookingModel, LocalDate> checkOut;
    @FXML private TableColumn<BookingModel, Integer> guestCount;
    @FXML private TableColumn<BookingModel, String> guestNames;
    @FXML private TableColumn<BookingModel, Void> actions;
    @FXML private Button addBookingButton;

    private final ObservableList<BookingModel> masterData = FXCollections.observableArrayList();
    private final BookingService bookingService;
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @FXML
    public void initialize() {
        // 1. Link the table to our master list
        bookingTable.setItems(masterData);

        // 2. Setup columns for direct properties
        //id.setCellValueFactory(new PropertyValueFactory<>("bookingUUID"));
        id.setCellValueFactory(cellData ->
            Bindings.createObjectBinding(() -> cellData.getValue().getBookingUUID())
        );

        checkIn.setCellValueFactory(cellData ->
                Bindings.createObjectBinding(() -> cellData.getValue().getCheckInDate())
        );

        checkOut.setCellValueFactory(cellData ->
                Bindings.createObjectBinding(() -> cellData.getValue().getCheckOutDate())
        );
        //checkIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        //checkOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        // 3. Setup columns for NESTED properties (RoomModel)
        roomNumber.setCellValueFactory(cellData -> {
            RoomModel room = cellData.getValue().getRoom();
            // Assuming RoomModel has a getRoomNumber() or roomNumberProperty()
            return room != null ? room.roomNumberProperty() : new SimpleStringProperty("N/A");
        });

        // 4. Setup column to count the collection size (Guest List)
        guestCount.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getGuestList().size()).asObject()
        );

        // 5. Setup column to map/join multiple strings into one display cell
        guestNames.setCellValueFactory(cellData -> {
            String names = cellData.getValue().getGuestList().stream()
                    .map(GuestModel::getName) // Or getFullName() if you have it
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(names.isEmpty() ? "No guests" : names);
        });

        // 6. Setup the Custom Action Column (e.g., a Delete button per row)
        setupActionsColumn();


        // Optional: Fake data initialization to verify everything works
        addBookingButton.setOnAction(_ -> {
            try {
                var roomService = new RoomService(new RoomRepository(DatabaseUtils.createConnection()));
                var guestService = new GuestService(new GuestRepository(DatabaseUtils.createConnection()));

                var modalResult = new Modal.ModalBuilder<BookingCreateController, BookingModel>(ViewManager.ViewPath.BOOKING_CREATE)
                        .title("New Booking Reservation")
                        // Passes the model alongside your functional backend service dependencies
                        .controller(new BookingCreateController(new BookingModel(), roomService, guestService))
                        .mapper(BookingCreateController::getBookingModel)
                        .build()
                        .showAndWait();

                modalResult.ifPresent(confirmedBookingModel -> {
                    log.info("Successfully received booking details for room: {}", confirmedBookingModel.getRoom().getId());

                    // Save to database
                    try {
                        bookingService.create(confirmedBookingModel.toBooking());
                    } catch (Exception e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to save booking to database: " + e.getMessage()).showAndWait();
                    }
                    // Instantly push to the UI array list table view items collection
                    masterData.add(confirmedBookingModel);
                });

            } catch (Exception e) {
                throw new RuntimeException("Error processing booking modal session wrapper context.", e);
            }
        });

        loadSampleData();
    }

    private void setupActionsColumn() {
        actions.setCellFactory(param -> new TableCell<>() {
            // 1. Instantiate both buttons
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            // 2. Put them side-by-side inside an HBox with 8px spacing
            private final HBox container = new HBox(8, updateButton, deleteButton);

            {
                // 3. Set up the Delete button click handler
                deleteButton.setOnAction(_ -> {
                    BookingModel clickedBooking = getTableRow().getItem(); // Safer way to grab the row item
                    if (clickedBooking != null) {
                        handleDeleteBooking(clickedBooking);
                    }
                });

                // 4. Set up the Update button click handler
                updateButton.setOnAction(_ -> {
                    BookingModel clickedBooking = getTableRow().getItem();
                    if (clickedBooking != null) {
                        //openBookingModal(clickedBooking); // Directs to your Edit logic
                        try {
                            var result = new Modal.ModalBuilder<BookingCreateController, BookingModel>(ViewManager.ViewPath.BOOKING_CREATE)
                                    .title("Update Booking Reservation")
                                    .controller(new BookingCreateController(clickedBooking, new RoomService(new RoomRepository(DatabaseUtils.createConnection())), new GuestService(new GuestRepository(DatabaseUtils.createConnection()))))
                                    .mapper(BookingCreateController::getBookingModel)
                                    .build()
                                    .showAndWait();
                            if(result.isPresent()) {
                                bookingService.update(result.get().toBooking());
                                masterData.setAll(bookingService.getAll().stream().map(BookingModel::new).toList());
                            }
                        } catch (IOException e) {
                            new Alert(Alert.AlertType.ERROR, "Failed to open booking update modal: " + e.getMessage()).showAndWait();
                        }
                    }
                });
            }

            // 5. The mandatory override that controls rendering logic
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    // If row is empty, clear out any visual graphics
                    setGraphic(null);
                } else {
                    // If row has data, show our HBox containing both buttons
                    setGraphic(container);
                }
            }
        });
    }

    private void handleDeleteBooking(BookingModel booking) {
        masterData.remove(booking);
        System.out.println("Deleted booking: " + booking.getBookingUUID());
        bookingService.deleteWithUUID(booking.getBookingUUID());
        // Here you would also call your backend service: bookingService.delete(booking.toBooking());
    }

    private void loadSampleData() {
        // You would normally fetch this from a database/service wrapper
         masterData.addAll(bookingService.getAll().stream().map(BookingModel::new).toList());
    }
}
/*
public class BookingController {
    @FXML private TableView<BookingModel> bookingTable;
    @FXML private TableColumn<BookingModel, UUID> id;
    @FXML private TableColumn<BookingModel, UUID> roomNumber;
    @FXML private TableColumn<BookingModel, LocalDate> checkIn;
    @FXML private TableColumn<BookingModel, LocalDate> checkOut;
    @FXML private TableColumn<BookingModel, Integer> guestCount;
    @FXML private TableColumn<BookingModel, String> guestNames;
    @FXML private Button addBookingButton;

    private final ObservableList<BookingModel> bookingList = FXCollections.observableArrayList();
    private final BookingService bookingService;
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
        bookingList.addAll(bookingService.getAll().stream()
                .map(b -> new BookingModel(b.bookingUUID(), FXCollections.observableSet(b.guests()), b.roomUUID(), b.checkInDate(), b.checkOutDate()))
                .toList());
    }

    @FXML
    public void initialize() {
        id.setCellValueFactory(cellData -> cellData.getValue().bookingUUIDProperty());
        roomNumber.setCellValueFactory(cellData -> cellData.getValue().roomUUIDProperty());
        checkIn.setCellValueFactory(cellData -> cellData.getValue().checkInProperty());
        checkOut.setCellValueFactory(cellData -> cellData.getValue().checkOutProperty());

        /*
        guestCount.setCellValueFactory(cellData ->
                Bindings.createObjectBinding(() -> cellData.getValue().getGuests().size(), cellData.getValue().guestsProperty())
        );


        guestNames.setCellValueFactory(cellData ->
                Bindings.createStringBinding(() -> {
                    String names = cellData.getValue().getGuests().stream()
                            .map(guest -> guest.uuid().toString())
                            .collect(Collectors.joining(", "));
                    return names.isEmpty() ? "No occupants" : names;
                }, cellData.getValue().guestsProperty())
        );

        bookingTable.setItems(bookingList);

        addBookingButton.setOnAction(_ -> {
            try {
                var roomService = new RoomService(new RoomRepository(DatabaseUtils.createConnection()));
                var guestService = new GuestService(new GuestRepository(DatabaseUtils.createConnection()));

                var modalResult = new Modal.ModalBuilder<BookingCreateController, BookingModel>(ViewManager.ViewPath.BOOKING_CREATE)
                        .title("New Booking Reservation")
                        // Passes the model alongside your functional backend service dependencies
                        .controller(new BookingCreateController(new BookingModel(), roomService, guestService))
                        .mapper(BookingCreateController::getBookingModel)
                        .build()
                        .showAndWait();

                modalResult.ifPresent(confirmedBookingModel -> {
                    log.info("Successfully received booking details for room: {}", confirmedBookingModel.getRoomUUID());

                    // Save to database
                    try {
                        bookingService.create(confirmedBookingModel.toBooking());
                    } catch (Exception e) {
                        new Alert(Alert.AlertType.ERROR, "Failed to save booking to database: " + e.getMessage()).showAndWait();
                    }
                    // Instantly push to the UI array list table view items collection
                    bookingList.add(confirmedBookingModel);
                });

            } catch (Exception e) {
                throw new RuntimeException("Error processing booking modal session wrapper context.", e);
            }
        });
    }
}

 */
