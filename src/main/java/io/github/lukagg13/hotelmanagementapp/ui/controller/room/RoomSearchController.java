package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.entity.Room;
import io.github.lukagg13.hotelmanagementapp.ui.ViewManager;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller used for searching and selecting {@link Room}'s.
 */
public class RoomSearchController {
    @FXML
    private TextField searchField;
    @FXML
    private VBox roomsVBox;
    @FXML
    private Button selectButton;
    @FXML
    private Button createButton;

    private final ObservableList<RoomModel> roomModels = FXCollections.observableArrayList();
    private FilteredList<RoomModel> filteredRooms;

    private RoomModel selectedRoomModel = null;

    private static final Logger log = LoggerFactory.getLogger(RoomSearchController.class);
    private final RoomService roomService;
    private boolean selectButtonClicked = false;

    /**
     * Returns a new instance of the {@link RoomSearchController}.
     * @param roomService The {@link RoomService} that will be used to access the {@link Room}'s.
     */
    public RoomSearchController(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    public void initialize() {
        roomService.getAll().forEach(room -> roomModels.add(new RoomModel(room)));
        filteredRooms = new FilteredList<>(roomModels, _ -> true);

        searchField.textProperty().addListener((_, _, query) ->
            filteredRooms.setPredicate(roomModel -> query == null || query.isBlank() ||
                    roomModel.getRoomNumber().contains(query.trim())
            )
        );

        filteredRooms.addListener((InvalidationListener) _ -> refreshDisplay());
        refreshDisplay();

        createButton.setOnAction(_ -> {
            final var buttonClicked = new AtomicBoolean(false);
            var optionalRoomModel = new Modal.ModalBuilder<RoomCreateController, RoomModel>(ViewManager.ViewPath.ROOM_CREATE)
                    .title("Create Room")
                    .controller(new RoomCreateController(new RoomModel(), _ -> buttonClicked.set(true) , "Create"))
                    .mapper(RoomCreateController::getRoomModel)
                    .build()
                    .showAndWait();

            if(!buttonClicked.get() || optionalRoomModel.isEmpty()) return;

            var createdRoomModel = optionalRoomModel.orElseThrow();
            roomService.create(createdRoomModel.toRoom());

            selectedRoomModel = createdRoomModel;
            roomModels.add(createdRoomModel);
        });

        selectButton.setOnAction(_ -> {
            selectButtonClicked = true;
            Stage stage = (Stage) selectButton.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Method to refresh the guestVBox.
     */
    private void refreshDisplay() {
        roomsVBox.getChildren().clear();
        for (var roomModel : filteredRooms) {
            var roomComponentController = getRoomComponentController(roomModel);
            if (selectedRoomModel != null && selectedRoomModel.equals(roomModel)) {
                applySelectedStyle(roomComponentController);
            } else {
                resetStyle(roomComponentController);
            }
            roomsVBox.getChildren().add(roomComponentController);
        }
    }

    /**
     * Sets the apply CSS for a {@link RoomComponentController}.
     * @param roomComponentController The {@link RoomComponentController} that the CSS will be applied to.
     */
    private void applySelectedStyle(RoomComponentController roomComponentController) {
        roomComponentController.setStyle("-fx-border-color: #0284c7; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #e0f2fe;");
        roomComponentController.applyCss();
        roomComponentController.layout();
    }

    /**
     * Resets the apply CSS for a {@link RoomComponentController}.
     * @param roomComponentController The {@link RoomComponentController} that the CSS will be reset to.
     */
    private void resetStyle(RoomComponentController roomComponentController) {
        roomComponentController.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #ffffff;");
        roomComponentController.applyCss();
        roomComponentController.layout();
    }

    /**
     * Returns a {@link RoomComponentController} will the right style and mouse events.
     * @param roomModel The {@link RoomModel} from which a {@link RoomComponentController} will be created.
     * @return The new {@link RoomComponentController}.
     */
    private RoomComponentController getRoomComponentController(RoomModel roomModel) {
        var roomComponentController = new RoomComponentController(roomModel);
        roomComponentController.setOnMouseClicked(_ -> {
            if (roomModel.equals(selectedRoomModel)) {
                selectedRoomModel = null;
                log.debug("Deselected room: {}", roomModel);
            } else {
                selectedRoomModel = roomModel;
                log.debug("Selected room: {}", roomModel);
            }
            refreshDisplay();
        });
        return roomComponentController;
    }

    /**
     * Returns the selected {@link RoomModel}.
     * @return the selected {@link RoomModel}.
     */
    public RoomModel getSelectedRoomModel() {
        return selectButtonClicked ? this.selectedRoomModel : null;
    }
}