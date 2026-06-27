package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.controller.room.RoomComponentController;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
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

public class RoomSearchController {
    @FXML private TextField searchField;
    @FXML private VBox roomsVBox;
    @FXML private Button selectButton;
    @FXML private Button createButton;

    private final ObservableList<RoomModel> masterRooms = FXCollections.observableArrayList();
    private FilteredList<RoomModel> filteredRooms;

    // Track exactly one selected room instead of a list
    private RoomModel selectedRoomModel = null;

    private static final Logger log = LoggerFactory.getLogger(RoomSearchController.class);
    private final RoomService roomService;
    private boolean selectButtonClicked = false;

    public RoomSearchController(RoomService roomService) {
        this.roomService = roomService;
    }

    @FXML
    public void initialize() {
        roomService.getAll().forEach(r -> masterRooms.add(new RoomModel(r)));
        filteredRooms = new FilteredList<>(masterRooms, _ -> true);

        searchField.textProperty().addListener((_, _, query) -> {
            filteredRooms.setPredicate(rm -> query == null || query.isBlank() ||
                    rm.getRoomNumber().contains(query.trim()));
        });

        filteredRooms.addListener((javafx.collections.ListChangeListener.Change<? extends RoomModel> _) -> refreshDisplay());
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

            // Auto-select the newly created room (clearing any old selection)
            selectedRoomModel = createdRoomModel;
            masterRooms.add(createdRoomModel);
        });

        selectButton.setOnAction(_ -> {
            selectButtonClicked = true;
            Stage stage = (Stage) selectButton.getScene().getWindow();
            stage.close();
        });
    }

    private void refreshDisplay() {
        roomsVBox.getChildren().clear();
        for (var rm : filteredRooms) {
            var comp = getRoomComponentController(rm);
            // Highlights the item if it matches our single selected room
            if (selectedRoomModel != null && selectedRoomModel.equals(rm)) {
                applySelectedStyle(comp);
            } else {
                resetStyle(comp);
            }
            roomsVBox.getChildren().add(comp);
        }
    }

    private void applySelectedStyle(RoomComponentController roomComponentController) {
        roomComponentController.setStyle("-fx-border-color: #0284c7; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #e0f2fe;");
        roomComponentController.applyCss();
        roomComponentController.layout();
    }

    private void resetStyle(RoomComponentController roomComponentController) {
        roomComponentController.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 6; -fx-background-radius: 6; -fx-background-color: #ffffff;");
        roomComponentController.applyCss();
        roomComponentController.layout();
    }

    private RoomComponentController getRoomComponentController(RoomModel roomModel) {
        var roomComponentController = new RoomComponentController(roomModel);
        roomComponentController.setOnMouseClicked(_ -> {
            if (roomModel.equals(selectedRoomModel)) {
                // Clicking the already selected room deselects it
                selectedRoomModel = null;
                log.debug("Deselected room: {}", roomModel);
            } else {
                // Selects the new room, clearing out the old one
                selectedRoomModel = roomModel;
                log.debug("Selected room: {}", roomModel);
            }
            // Trigger a full visual re-render to enforce the single-selection style swap
            refreshDisplay();
        });
        return roomComponentController;
    }

    // Returns the single selected room model (or null if they canceled or didn't select one)
    public RoomModel getSelectedRoomModel() {
        return selectButtonClicked ? this.selectedRoomModel : null;
    }
}