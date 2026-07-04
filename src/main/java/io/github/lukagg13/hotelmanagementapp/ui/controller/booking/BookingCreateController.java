package io.github.lukagg13.hotelmanagementapp.ui.controller.booking;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.entity.Booking;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.controller.guest.GuestComponentController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.guest.GuestSearchController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.room.RoomComponentController;
import io.github.lukagg13.hotelmanagementapp.ui.controller.room.RoomSearchController;
import io.github.lukagg13.hotelmanagementapp.ui.model.BookingModel;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller class used for creating or editing an existing {@link Booking}.
 */
public class BookingCreateController {
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;
    @FXML
    private Button selectRoomButton;
    @FXML
    private Label selectedRoomLabel;
    @FXML
    private Button selectGuestsButton;
    @FXML
    private Label selectedGuestsLabel;
    @FXML
    private Button createBookingButton;
    @FXML
    private VBox roomVBox;
    @FXML
    private VBox guestsVBox;

    private static final Logger log = LoggerFactory.getLogger(BookingCreateController.class);

    private final RoomService roomService;
    private final GuestService guestService;

    private final BookingModel bookingModel;
    private boolean isConfirmed = false;

    /**
     * Returns a new {@link BookingCreateController}.
     * @param bookingModel the {@link BookingModel} that is bound to the UI.
     * @param roomService the {@link RoomService} that is used to access {@link io.github.lukagg13.hotelmanagementapp.entity.Room}'s.
     * @param guestService the {@link GuestService} that is used to access {@link io.github.lukagg13.hotelmanagementapp.entity.Guest}'s.
     */
    public BookingCreateController(BookingModel bookingModel, RoomService roomService, GuestService guestService) {
        this.bookingModel = bookingModel;
        this.roomService = roomService;
        this.guestService = guestService;
    }

    /**
     * Method used to update the guest VBox.
     * @param guestModels A {@link List} of the {@link GuestModel}'s that will be added to the VBox.
     */
    private void updateGuestsVBox(List<GuestModel> guestModels) {
        guestsVBox.getChildren().clear();
        if(guestModels.isEmpty()) guestsVBox.getChildren().add(new Label("No guest selected"));
        guestModels.forEach(guestModel ->
                guestsVBox.getChildren().add(guestModel  != null ? new GuestComponentController(guestModel) : new Label("Invalid guest"))
        );
    }

    /**
     * Method used to update the room VBox.
     * @param roomModel {@link RoomModel}'s that will be added to the VBox.
     */
    private void updateRoomVBox(RoomModel roomModel) {
        roomVBox.getChildren().clear();
        roomVBox.getChildren().add(roomModel != null ? new RoomComponentController(roomModel) : new Label("Invalid room"));
    }

    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    public void initialize() {
        checkInDatePicker.valueProperty().bindBidirectional(bookingModel.checkInDateProperty());
        checkOutDatePicker.valueProperty().bindBidirectional(bookingModel.checkOutDateProperty());

        updateRoomVBox(bookingModel.getRoom());
        bookingModel.roomProperty().addListener((_, _, newValue) ->
                updateRoomVBox(newValue)
        );

        updateGuestsVBox(bookingModel.getGuestList());
        bookingModel.guestListProperty().addListener((_, _, newValue) ->
            updateGuestsVBox(newValue)
       );

        selectRoomButton.setOnAction(_ -> handleSelectRoom());
        selectGuestsButton.setOnAction(_ -> handleSelectGuests());

        createBookingButton.setOnAction(_ -> {
            if (validateBookingInputs()) {
                this.isConfirmed = true;
                Stage stage = (Stage) createBookingButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    /**
     * Opens a {@link Modal} for selecting a {@link io.github.lukagg13.hotelmanagementapp.entity.Room} that will be
     * used in the {@link Booking}.
     */
    //TODO: lose jel modifija state trebalo bi returnat.
    private void handleSelectRoom() {
        var modalResult = new Modal.ModalBuilder<RoomSearchController, RoomModel>(ViewManager.ViewPath.ROOM_SEARCH)
                .title("Select a Room")
                .controller(new RoomSearchController(roomService))
                .mapper(RoomSearchController::getSelectedRoomModel)
                .build()
                .showAndWait();

        modalResult.ifPresent(roomModel -> {
            bookingModel.setRoom(roomModel);
            log.debug("Room: {}", roomModel);
        });
    }

    /**
     * Opens a {@link Modal} for selecting {@link io.github.lukagg13.hotelmanagementapp.entity.Guest}'s that will be
     * used in the {@link Booking}.
     */
    //TODO: lose jel modifija state trebalo bi returnat.
    private void handleSelectGuests() {
        var modalResult = new Modal.ModalBuilder<GuestSearchController, List<GuestModel>>(ViewManager.ViewPath.GUEST_SEARCH)
                .title("Select Guests")
                .controller(new GuestSearchController(guestService))
                .mapper(GuestSearchController::getSelectedGuestModels)
                .build()
                .showAndWait();

        modalResult.ifPresent(guestList -> {
            if (!guestList.isEmpty()) {
                bookingModel.guestListProperty().setAll(guestList);
                log.debug("Guests: {} items", guestList.size());
            }
        });
    }

    /**
     * Check if the inputs are correct.
     * @return True if inputs are correct false if not.
     */
    private boolean validateBookingInputs() {
        if (bookingModel.getCheckInDate() == null || bookingModel.getCheckOutDate() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select check-in and check-out.").showAndWait();
            return false;
        }
        if (!bookingModel.getCheckOutDate().isAfter(bookingModel.getCheckInDate())) {
            new Alert(Alert.AlertType.WARNING, "Check-out must be after the check-in.").showAndWait();
            return false;
        }
        if (bookingModel.getRoom() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a room.").showAndWait();
            return false;
        }
        if (bookingModel.guestListProperty().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please assign the guests.").showAndWait();
            return false;
        }
        return true;
    }


    /**
     * Gets the {@link BookingModel}. If the confirm button has been pressed.
     * This is done to prevent changing data without the users intent.
     * @return The {@link BookingModel} that was pass to the constructor with the updated values.
     */
    public BookingModel getBookingModel() {
        return isConfirmed ? this.bookingModel : null;
    }
}