package io.github.lukagg13.hotelmanagementapp.ui.controller.booking;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.controller.guest.GuestSearchController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.room.RoomSearchController;
import io.github.lukagg13.hotelmanagementapp.ui.model.BookingModel;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class BookingCreateController {

    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private Button selectRoomButton;
    @FXML private Label selectedRoomLabel;
    @FXML private Button selectGuestsButton;
    @FXML private Label selectedGuestsLabel;
    @FXML private Button cancelButton;
    @FXML private Button createBookingButton;

    private static final Logger log = LoggerFactory.getLogger(BookingCreateController.class);

    private final RoomService roomService;
    private final GuestService guestService;

    // The model injected into the controller
    private final BookingModel bookingModel;
    private boolean isConfirmed = false;

    // Pass the model directly into the constructor
    public BookingCreateController(BookingModel bookingModel, RoomService roomService, GuestService guestService) {
        this.bookingModel = bookingModel;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    @FXML
    public void initialize() {
        // 1. Bidirectional binding for standard properties (Dates)
        // Assuming your BookingModel has checkInProperty() and checkOutProperty()
        checkInDatePicker.valueProperty().bindBidirectional(bookingModel.checkInDateProperty());
        checkOutDatePicker.valueProperty().bindBidirectional(bookingModel.checkOutDateProperty());

        // 2. Unidirectional string mappings for the complex model wrappers
        selectedRoomLabel.textProperty().bind(bookingModel.roomProperty().map(rm ->
                rm == null ? "No room selected" : "Selected: Room " + rm.getRoomNumber() + " ($" + rm.getPricePerNight() + "/night)"
        ));

        // Dynamically style the room label text based on whether a selection is present
        bookingModel.roomProperty().addListener((_, _, newRoom) -> {
            selectedRoomLabel.setStyle(newRoom == null ? "-fx-text-fill: #64748b; -fx-font-style: italic;" : "-fx-text-fill: #0284c7; -fx-font-style: normal;");
        });

        // 3. Unidirectional list summary string mapping for guests
        selectedGuestsLabel.textProperty().bind(bookingModel.guestListProperty().map(guestsList ->
                guestsList == null || guestsList.isEmpty() ? "No guests selected" : guestsList.size() + " guest(s) selected."
        ));

        bookingModel.guestListProperty().addListener((_, _, newGuests) -> {
            selectedGuestsLabel.setStyle(newGuests == null || newGuests.isEmpty() ? "-fx-text-fill: #64748b; -fx-font-style: italic;" : "-fx-text-fill: #0284c7; -fx-font-style: normal;");
        });

        // Set up action handlers
        selectRoomButton.setOnAction(_ -> handleSelectRoom());
        selectGuestsButton.setOnAction(_ -> handleSelectGuests());

        createBookingButton.setOnAction(_ -> {
            if (validateBookingInputs()) {
                this.isConfirmed = true;
                closeWindow();
            }
        });

        cancelButton.setOnAction(_ -> closeWindow());
    }

    private void handleSelectRoom() {
        var modalResult = new Modal.ModalBuilder<RoomSearchController, RoomModel>(ViewManager.ViewPath.ROOM_SEARCH)
                .title("Select a Room")
                .controller(new RoomSearchController(roomService))
                .mapper(RoomSearchController::getSelectedRoomModel)
                .build()
                .showAndWait();

        // Simply update the model's property wrapper; the UI text bindings react automatically
        modalResult.ifPresent(roomModel -> {
            bookingModel.setRoom(roomModel);
            log.debug("Room property updated on core model: {}", roomModel);
        });
    }

    private void handleSelectGuests() {
        var modalResult = new Modal.ModalBuilder<GuestSearchController, List<GuestModel>>(ViewManager.ViewPath.GUEST_SEARCH)
                .title("Select Guests")
                .controller(new GuestSearchController(guestService))
                .mapper(GuestSearchController::getSelectedGuestModels)
                .build()
                .showAndWait();

        // Set the collection directly to the model's list property wrapper
        modalResult.ifPresent(guestList -> {
            if (!guestList.isEmpty()) {
                bookingModel.guestListProperty().setAll(guestList);
                log.debug("Guests collection updated on core model: {} items", guestList.size());
            }
        });
    }

    private boolean validateBookingInputs() {
        if (bookingModel.getCheckInDate() == null || bookingModel.getCheckOutDate() == null) {
            showValidationError("Please select valid check-in and check-out dates.");
            return false;
        }
        if (!bookingModel.getCheckOutDate().isAfter(bookingModel.getCheckInDate())) {
            showValidationError("Check-out date must be strictly after the check-in date.");
            return false;
        }
        if (bookingModel.getRoom() == null) {
            showValidationError("Please assign a room to this booking before proceeding.");
            return false;
        }
        if (bookingModel.guestListProperty().isEmpty()) {
            showValidationError("Please assign at least one guest to this booking.");
            return false;
        }
        return true;
    }

    private void showValidationError(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Returns the populated model back to the dashboard if they confirmed creation
    public BookingModel getBookingModel() {
        return isConfirmed ? this.bookingModel : null;
    }
}