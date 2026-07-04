package io.github.lukagg13.hotelmanagementapp.ui.controller.guest;

import io.github.lukagg13.hotelmanagementapp.ui.model.GuestModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A controller used for displaying a custom guest component.
 */
public class GuestComponentController extends VBox {

    private static final Logger log = LoggerFactory.getLogger(GuestComponentController.class);

    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;


    /**
     * Makes a new {@link GuestComponentController} from a {@link GuestModel}.
     * @param guestModel the {@link GuestModel} that will be bound to the component.
     */
    public GuestComponentController(GuestModel guestModel) {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/guest-component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            nameLabel.textProperty().bind(guestModel.nameProperty());
            ageLabel.textProperty().bind(guestModel.ageProperty().asString());
        } catch (IOException e) {
            log.error("Error loading Guest component {}", e.getMessage());
        }
    }
}
