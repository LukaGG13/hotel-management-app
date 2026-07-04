package io.github.lukagg13.hotelmanagementapp.ui.controller.room;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.service.RoomService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.model.RoomModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RoomController {

    @FXML
    private TableView<RoomModel> table;
    @FXML
    private TableColumn<RoomModel, String> id;
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
    private TableColumn<RoomModel, Object> editAndDelete;
    @FXML Button addRoomButton;

    private final ObservableList<RoomModel> data = FXCollections.observableArrayList();
    private final RoomService roomService;

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    public RoomController(RoomService roomService) {
        log.debug("Creating room controller");
        this.roomService = roomService;
    }

    @FXML
    public void initialize() {
        id.setCellValueFactory(f -> f.getValue().idProperty());
        numOfBeds.setCellValueFactory(f -> f.getValue().numberOfBedsProperty());
        sizeInSqrM.setCellValueFactory(f -> f.getValue().sizeInSqrMProperty());
        pricePerNight.setCellValueFactory(f -> f.getValue().pricePerNightProperty());
        distanceFromCityCenter.setCellValueFactory(f -> f.getValue().distanceFromCityCenterProperty());
        distanceFromBeach.setCellValueFactory(f -> f.getValue().distanceFromBeachProperty());
        roomNumber.setCellValueFactory(f -> f.getValue().roomNumberProperty());
        amenities.setCellValueFactory(f -> f.getValue().amenitiesProperty());

        editAndDelete.setCellFactory(getEditAndDeleteColumn().getCellFactory());

        data.setAll(getListOfRoomModels());
        table.setItems(data);

        addRoomButton.setOnAction(_ -> {
            openRoomModal(null);
            data.setAll(getListOfRoomModels());
        });
    }

    private List<RoomModel> getListOfRoomModels() {
        List<RoomModel> modelList = new ArrayList<>();
        for (var room : roomService.getAll()) {
            modelList.add(new RoomModel(room));
        }
        return modelList;
    }

    private TableColumn<RoomModel, Object> getEditAndDeleteColumn() {
        TableColumn<RoomModel, Object> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<RoomModel, Object>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            //TODO: znat objasnit ovo ?
            {
                editBtn.setOnAction(e -> openRoomModal(getTableRow().getItem()));
                deleteBtn.setOnAction(e -> {
                    RoomModel model = getTableRow().getItem();
                    if (model != null) {
                        roomService.deleteWithUUID(model.toRoom().getId());
                        data.remove(model);
                    }
                });
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableRow() == null || getTableRow().getItem() == null ? null : pane);
            }
        });
        return actionCol;
    }

    private void openRoomModal(RoomModel room) {
        String title = (room == null) ? "Add New Room" : "Edit Room";
        RoomCreateController controller = (room == null)
                ? new RoomCreateController(new RoomModel(), roomService::create, title)
                : new RoomCreateController(room, roomService::update, title);

        new Modal.ModalBuilder<RoomCreateController, RoomModel>(ViewManager.ViewPath.ROOM_CREATE)
                .title(title)
                .controller(controller)
                .build()
                .showAndWait();
    }
}

