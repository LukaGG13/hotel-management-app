package io.github.lukagg13.hotelmanagementapp.ui.controller.guest;

import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestController {

    @FXML
    private TableView<GuestModel> table;

    @FXML
    private TableColumn<GuestModel, String> name;

    @FXML
    private TableColumn<GuestModel, Integer> age;

    @FXML
    private TableColumn<GuestModel, Object> editAndDelete;

    @FXML Button addGuestButton;

    private final ObservableList<GuestModel> data = FXCollections.observableArrayList();
    private final GuestService guestService;

    private static final Logger log = LoggerFactory.getLogger(GuestController.class);

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @FXML
    public void initialize() {
        name.setCellValueFactory(f -> f.getValue().nameProperty());
        age.setCellValueFactory(f -> f.getValue().ageProperty());

        editAndDelete.setCellFactory(getEditAndDeleteColumn().getCellFactory());

        data.setAll(getListOfGuestModels());
        table.setItems(data);

        addGuestButton.setOnAction(_ -> {
            openGuestModal(null);
            data.setAll(getListOfGuestModels());

        });
    }

    private List<GuestModel> getListOfGuestModels() {
        List<GuestModel> modelList = new ArrayList<>();
        for (var guest : guestService.getAll()) {
            modelList.add(new GuestModel(guest));
        }
        return modelList;
    }

    private TableColumn<GuestModel, Object> getEditAndDeleteColumn() {
        TableColumn<GuestModel, Object> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(param -> new TableCell<GuestModel, Object>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setOnAction(e -> openGuestModal(getTableRow().getItem()));
                deleteBtn.setOnAction(e -> {
                    GuestModel model = getTableRow().getItem();
                    if (model != null) {
                        guestService.deleteWithUUID(model.toGuest().uuid());
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

    private void openGuestModal(GuestModel guest) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/guest-create.fxml"));

            GuestCreateController guestCreateController;
            var title = "";
            if (guest == null) {
                title = "Add New Guest";
                guestCreateController = new GuestCreateController(new GuestModel(), guestService::create, title);
            } else {
                title = "Edit Guest";
                guestCreateController = new GuestCreateController(guest, guestService::update, title);
            }
            loader.setController(guestCreateController);

            Parent root = loader.load();
            Stage modalStage = new Stage();

            modalStage.setTitle(title);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));

            modalStage.showAndWait();
        } catch (IOException e) {
            log.error("Error loading view {}", e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load.");
            alert.showAndWait();
        }
    }
}