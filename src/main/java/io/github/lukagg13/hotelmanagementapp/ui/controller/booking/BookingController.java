package io.github.lukagg13.hotelmanagementapp.ui.controller.booking;

import io.github.lukagg13.hotelmanagementapp.ui.ViewManager;
import io.github.lukagg13.hotelmanagementapp.entity.Booking;
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
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class used for the booking screen.
 */
public class BookingController {
    @FXML
    private TableView<BookingModel> bookingTable;
    @FXML
    private TableColumn<BookingModel, String> roomNumber;
    @FXML
    private TableColumn<BookingModel, LocalDate> checkIn;
    @FXML
    private TableColumn<BookingModel, LocalDate> checkOut;
    @FXML
    private TableColumn<BookingModel, Integer> guestCount;
    @FXML
    private TableColumn<BookingModel, String> guestNames;
    @FXML
    private TableColumn<BookingModel, Void> actions;
    @FXML
    private Button createBookingButton;
    @FXML
    private Button editBookingButton;
    @FXML
    private Button deleteBookingButton;
    @FXML
    private TextField searchTextField;

    private final ObservableList<BookingModel> data = FXCollections.observableArrayList();
    private final BookingService bookingService;
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    /**
     * Creates a new instance of the {@link BookingController}.
     * @param bookingService Services used to add, update and delete {@link Booking}'s.
     */
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    public void initialize() {
        final var filteredList = new FilteredList<>(data, _ -> true);
        bookingTable.setItems(filteredList);

        searchTextField.textProperty().addListener((_, _, query) ->
                filteredList.setPredicate(bookingModel -> query.isBlank() || bookingModel.toBooking().toString().contains(query))
        );

        checkIn.setCellValueFactory(cellData ->
                Bindings.createObjectBinding(() -> cellData.getValue().getBookingCheckInDate())
        );
        checkOut.setCellValueFactory(cellData ->
                Bindings.createObjectBinding(() -> cellData.getValue().getBookingCheckOutDate())
        );
        roomNumber.setCellValueFactory(cellData -> {
            RoomModel room = cellData.getValue().getRoom();
            return room.roomNumberProperty();
        });
        guestCount.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getGuestList().size()).asObject()
        );
        guestNames.setCellValueFactory(cellData -> {
            String names = cellData.getValue().getGuestList().stream()
                    .map(GuestModel::getName)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(names.isEmpty() ? "No guests" : names);
        });

        editBookingButton.setOnAction(_ -> {
            log.info("Edit button clicked");
            var bookingModel = bookingTable.getSelectionModel().getSelectedItem();
            if(bookingModel == null) return;
            handleEditBooking(bookingModel);
        });

        deleteBookingButton.setOnAction(_ -> {
            log.info("Delete button clicked");
            var bookingModel = bookingTable.getSelectionModel().getSelectedItem();
            if(bookingModel == null) return;
            handleDeleteBooking(bookingModel);
        });

        createBookingButton.setOnAction(_ -> {
            log.info("Create button clicked");
            handleCrateBooking();
        });

        data.addAll(getListOfBookingModels());
    }

    /**
     * Method used to handle {@link Booking} deletion.
     * @param bookingModel {@link BookingModel} to be deleted.
     */
    private void handleDeleteBooking(BookingModel bookingModel) {
        data.remove(bookingModel);
        bookingService.deleteWithUUID(bookingModel.getBookingUUID());
    }

    /**
     * Method used to handle {@link Booking} editing.
     * @param bookingModel {@link BookingModel} to be edited.
     */
    private void handleEditBooking(BookingModel bookingModel) {
        var result = new Modal.ModalBuilder<BookingCreateController, BookingModel>(ViewManager.ViewPath.BOOKING_CREATE)
                .title("Edit Booking Reservation")
                .controller(new BookingCreateController(bookingModel, new RoomService(new RoomRepository(DatabaseUtils.createConnection())), new GuestService(new GuestRepository(DatabaseUtils.createConnection()))))
                .mapper(BookingCreateController::getBookingModel)
                .build()
                .showAndWait();

        if(result.isPresent()) {
            bookingService.update(result.get().toBooking());
            data.setAll(getListOfBookingModels());
        }
    }

    /**
     * Method used to handle {@link Booking} creation.
     */
    private void handleCrateBooking() {
        var roomService = new RoomService(new RoomRepository(DatabaseUtils.createConnection()));
        var guestService = new GuestService(new GuestRepository(DatabaseUtils.createConnection()));

        var modalResult = new Modal.ModalBuilder<BookingCreateController, BookingModel>(ViewManager.ViewPath.BOOKING_CREATE)
                .title("New Booking Reservation")
                .controller(new BookingCreateController(new BookingModel(), roomService, guestService))
                .mapper(BookingCreateController::getBookingModel)
                .build()
                .showAndWait();

        modalResult.ifPresent(bookingModel -> {
            bookingService.create(bookingModel.toBooking());
            data.add(bookingModel);
        });
    }

    /**
     * Returns a {@link List} off all the {@link BookingModel}'s.
     * @return The {@link List} off all the {@link BookingModel}'s.
     */
    private List<BookingModel> getListOfBookingModels() {
        var bookingModelList = new ArrayList<BookingModel>();
        for(var booking : bookingService.getAll()) {
            bookingModelList.add(new BookingModel(booking));
        }
        return bookingModelList;
    }
}