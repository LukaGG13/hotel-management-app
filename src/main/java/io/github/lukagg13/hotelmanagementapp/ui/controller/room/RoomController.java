package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.ui.ViewManager;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used for the room view.
 */
public class RoomController {

    @FXML
    private TableView<RoomModel> table;
    @FXML
    private TableColumn<RoomModel, Integer> numOfBeds;
    @FXML
    private TableColumn<RoomModel, String> sizeInSqrM;
    @FXML
    private TableColumn<RoomModel, String> pricePerNight;
    @FXML
    private TableColumn<RoomModel, String> distanceFromCityCenter;
    @FXML
    private TableColumn<RoomModel, String> distanceFromBeach;
    @FXML
    private TableColumn<RoomModel, String> roomNumber;
    @FXML
    private TableColumn<RoomModel, String> amenities;
    @FXML
    private Button addRoomButton;
    @FXML
    private Button editRoomButton;
    @FXML
    private Button deleteRoomButton;
    @FXML
    private TextField searchTextField;

    private final ObservableList<RoomModel> data = FXCollections.observableArrayList();
    private final RoomService roomService;

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    /**
     * Returns a new instance of the {@link RoomController}. *
     * @param roomService The {@link RoomService} used for accessing rooms.
     */
    public RoomController(RoomService roomService) {
        log.debug("Creating room controller");
        this.roomService = roomService;
    }


    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    public void initialize() {
        numOfBeds.setCellValueFactory(f -> f.getValue().numberOfBedsProperty());
        sizeInSqrM.setCellValueFactory(f -> f.getValue().sizeInSqrMProperty());
        pricePerNight.setCellValueFactory(f -> f.getValue().pricePerNightProperty().map(s -> s + "€"));
        distanceFromCityCenter.setCellValueFactory(f -> f.getValue().distanceFromCityCenterProperty());
        distanceFromBeach.setCellValueFactory(f -> f.getValue().distanceFromBeachProperty());
        roomNumber.setCellValueFactory(f -> f.getValue().roomNumberProperty());
        amenities.setCellValueFactory(f -> f.getValue().amenitiesProperty());


        data.setAll(getListOfRoomModels());

        final var filteredList = new FilteredList<>(data, _ -> true);
        table.setItems(filteredList);

        searchTextField.textProperty().addListener((_, _, query) ->
                filteredList.setPredicate(roomModel -> query.isBlank() || roomModel.toRoom().toString().toLowerCase().contains(query.toLowerCase()))
        );

        addRoomButton.setOnAction(_ -> {
            openRoomModal(null);
            data.setAll(getListOfRoomModels());
        });

        editRoomButton.setOnAction(_ -> {
            var model = table.getSelectionModel().getSelectedItem();
            if(model == null) return;

            var buttonTypeOptional = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to edit the room?").showAndWait();
            if(!buttonTypeOptional.orElse(ButtonType.NO).equals(ButtonType.OK)) return;

            openRoomModal(model);
            data.setAll(getListOfRoomModels());
        });

        deleteRoomButton.setOnAction(_ -> {
            var model = table.getSelectionModel().getSelectedItem();
            if(model == null) return;

            var buttonTypeOptional = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the room?").showAndWait();

            if(!buttonTypeOptional.orElse(ButtonType.NO).equals(ButtonType.OK)) return;
            roomService.deleteWithUUID(model.toRoom().getId());
            data.remove(model);
        });
    }

    private List<RoomModel> getListOfRoomModels() {
        List<RoomModel> modelList = new ArrayList<>();
        for (var room : roomService.getAll()) {
            modelList.add(new RoomModel(room));
        }
        return modelList;
    }

    private void openRoomModal(RoomModel room) {
        String title = (room == null) ? "Add New Room" : "Edit Room";
        var controller = (room == null)
                ? new RoomCreateController(new RoomModel(), roomService::create, title)
                : new RoomCreateController(room, roomService::update, title);

        new Modal.ModalBuilder<RoomCreateController, RoomModel>(ViewManager.ViewPath.ROOM_CREATE)
                .title(title)
                .controller(controller)
                .build()
                .showAndWait();
    }
}