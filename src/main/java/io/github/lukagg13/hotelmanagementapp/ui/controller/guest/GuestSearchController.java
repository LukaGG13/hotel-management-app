package io.github.lukagg13.hotelmanagementapp.ui.controller.guest;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import io.github.lukagg13.hotelmanagementapp.entity.Guest;
import io.github.lukagg13.hotelmanagementapp.service.GuestService;
import io.github.lukagg13.hotelmanagementapp.ui.component.Modal;
import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used for searching and selecting {@link Guest}'s.
 */
public class GuestSearchController {
    @FXML
    private TextField searchField;
    @FXML
    private VBox guestsVBox;
    @FXML
    private Button selectButton;
    @FXML
    private Button createButton;

    private final ObservableList<GuestModel> masterGuests = FXCollections.observableArrayList();
    private FilteredList<GuestModel> filteredGuests;
    private final List<GuestModel> selectedGuestModels = new ArrayList<>();

    private final GuestService guestService;
    private boolean selectButtonClicked = false;

    /**
     * Returns a new instance of the {@link GuestSearchController}.
     * @param guestService The {@link GuestService} that will be used to access the {@link Guest}'s.
     */
    public GuestSearchController(GuestService guestService) {
        this.guestService = guestService;
    }

    /**
     * Returns a {@link List} of selected {@link GuestModel}'s.
     * @return The {@link List} of selected {@link GuestModel}'s.
     */
    public List<GuestModel> getSelectedGuestModels() {
        return selectButtonClicked ? this.selectedGuestModels : new ArrayList<>();
    }

    /**
     * Method used to initialize state for javaFX.
     */
    @FXML
    public void initialize() {
        guestService.getAll().forEach(g -> masterGuests.add(new GuestModel(g)));
        filteredGuests = new FilteredList<>(masterGuests, _ -> true);

        searchField.textProperty().addListener((_, _, query) ->
            filteredGuests.setPredicate(guestModel -> query == null || query.isBlank() ||
                    guestModel.getName().toLowerCase().contains(query.toLowerCase().trim()))
        );

        filteredGuests.addListener((InvalidationListener) _ -> refreshDisplay());
        refreshDisplay();

        createButton.setOnAction(_ -> {
            var optionalGuestModel = new Modal.ModalBuilder<GuestCreateController, GuestModel>(ViewManager.ViewPath.GUEST_CREATE)
                    .title("Create Guest")
                    .controller(new GuestCreateController(new GuestModel()))
                    .mapper(GuestCreateController::getGuestModel)
                    .build()
                    .showAndWait();

            var createdGuestModel = optionalGuestModel.orElseThrow();
            optionalGuestModel.ifPresent(guestModel -> {
                guestService.create(createdGuestModel.toGuest());
                selectedGuestModels.add(createdGuestModel);
                masterGuests.add(createdGuestModel);
            });
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
        guestsVBox.getChildren().clear();
        for (var guestModel : filteredGuests) {
            var guestComponentController = getGuestComponentController(guestModel);
            if (selectedGuestModels.contains(guestModel)) {
                applySelectedStyle(guestComponentController);
            }
            else {
                resetStyle(guestComponentController );
            }
            guestsVBox.getChildren().add(guestComponentController);
        }
    }

    /**
     * Sets the apply CSS for a {@link GuestComponentController}.
     * @param guestComponentController The {@link GuestComponentController} that the CSS will be applied to.
     */
    private void applySelectedStyle(GuestComponentController guestComponentController) {
        guestComponentController.setStyle("-fx-border-color: #0284c7; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #e0f2fe;");
        guestComponentController.applyCss();
        guestComponentController.layout();
    }

    /**
     * Resets the apply CSS for a {@link GuestComponentController}.
     * @param guestComponentController The {@link GuestComponentController} that the CSS will be reset to.
     */private void resetStyle(GuestComponentController guestComponentController) {
        guestComponentController.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: transparent;");
        guestComponentController.applyCss();
        guestComponentController.layout();
    }

    /**
     * Returns a {@link GuestComponentController} will the right style and mouse events.
     * @param guestModel The {@link GuestModel} from which a {@link GuestComponentController} will be created.
     * @return The new {@link GuestComponentController}.
     */
    private GuestComponentController getGuestComponentController(GuestModel guestModel) {
        var guestComponentController = new GuestComponentController(guestModel);
        guestComponentController.setOnMouseClicked(_ -> {
            if (selectedGuestModels.contains(guestModel)) {
                selectedGuestModels.remove(guestModel);
                resetStyle(guestComponentController);
            } else {
                selectedGuestModels.add(guestModel);
                applySelectedStyle(guestComponentController);
            }
        });
        return guestComponentController;
    }
}