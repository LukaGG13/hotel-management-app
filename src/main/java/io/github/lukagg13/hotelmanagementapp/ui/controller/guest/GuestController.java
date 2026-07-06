package io.github.lukagg13.hotelmanagementapp.ui.controller.guest;

import io.github.lukagg13.hotelmanagementapp.ui.ViewManager;
import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class used for the Guest view.
 */
public class GuestController {
    @FXML
    private TableView<GuestModel> table;
    @FXML
    private TableColumn<GuestModel, String> name;
    @FXML
    private TableColumn<GuestModel, Integer> age;
    @FXML
    private Button editGuestButton;
    @FXML
    private Button deleteGuestButton;
    @FXML
    private Button addGuestButton;
    @FXML
    private TextField searchTextField;

    private final ObservableList<GuestModel> data = FXCollections.observableArrayList();
    private final GuestService guestService;

    /**
     * Return an instance of the {@link GuestController}.
     * @param guestService The {@link GuestService} that will be used to add, update and delete the {@link Guest}'s.
     */
    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    public void initialize() {
        data.setAll(getListOfGuestModels());

        final var filteredList = new FilteredList<>(data, _ -> true);
        table.setItems(filteredList);

        searchTextField.textProperty().addListener((_, _, query) ->
                filteredList.setPredicate(guestModel -> query.isBlank() || guestModel.toGuest().toString().contains(query))
        );

        name.setCellValueFactory(param -> param.getValue().nameProperty());
        age.setCellValueFactory(param -> param.getValue().ageProperty());

        addGuestButton.setOnAction(_ -> {
            var createdGuestModel = new Modal.ModalBuilder<GuestCreateController, GuestModel>(ViewManager.ViewPath.GUEST_CREATE)
                    .title("Edit Guest")
                    .controller(new GuestCreateController(new GuestModel()))
                    .mapper(GuestCreateController::getGuestModel)
                    .build()
                    .showAndWait();

            createdGuestModel.ifPresent(guestModel -> {
                guestService.create(guestModel.toGuest());
                data.add(guestModel);
            });
        });

        editGuestButton.setOnAction(_ -> {
            var model = table.getSelectionModel().getSelectedItem();
            if(model == null) return;

            var buttonTypeOptional = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to edit the user?").showAndWait();
            if(!buttonTypeOptional.orElse(ButtonType.NO).equals(ButtonType.OK)) return;

            var updatedGuestModel = new Modal.ModalBuilder<GuestCreateController, GuestModel>(ViewManager.ViewPath.GUEST_CREATE)
                    .title("Edit Guest")
                    .controller(new GuestCreateController(model))
                    .mapper(GuestCreateController::getGuestModel)
                    .build()
                    .showAndWait();

            updatedGuestModel.ifPresent(guestModel ->
               guestService.update(guestModel.toGuest())
            );
            data.setAll(getListOfGuestModels());
        });

        deleteGuestButton.setOnAction(_ -> {
            var model = table.getSelectionModel().getSelectedItem();
            if (model == null) return;
            var result = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the user?").showAndWait();

            result.ifPresent(buttonType -> {
                if(buttonType.equals(ButtonType.OK)) {
                    guestService.deleteWithUUID(model.toGuest().uuid());
                    data.remove(model);
                }
            });
        });
    }

    /**
     * Turns all the {@link Guest}'s in to a {@link List} of {@link GuestModel}'s.
     * @return The {@link List} of {@link GuestModel}'s.
     */
    private List<GuestModel> getListOfGuestModels() {
        List<GuestModel> modelList = new ArrayList<>();
        for (var guest : guestService.getAll()) {
            modelList.add(new GuestModel(guest));
        }
        return modelList;
    }
}