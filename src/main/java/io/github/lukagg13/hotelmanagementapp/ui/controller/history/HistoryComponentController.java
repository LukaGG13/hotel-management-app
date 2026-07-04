package io.github.lukagg13.hotelmanagementapp.ui.controller.history;

import io.github.lukagg13.hotelmanagementapp.ui.model.HistoryRecordLogModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Controller used for displaying a custom History component.
 */
public class HistoryComponentController extends VBox {

    static final Logger log = LoggerFactory.getLogger(HistoryComponentController.class);

    @FXML
    private Label nameLabel;
    @FXML
    private Label timeStampLabel;
    @FXML
    private Label oldObjectLabel;
    @FXML
    private Label newObjectLabel;

    /**
     * Makes a new {@link HistoryComponentController} from a {@link HistoryRecordLogModel}.
     * @param historyRecordLogModel the {@link HistoryRecordLogModel} that will be bound to the component.
     */
    public HistoryComponentController(HistoryRecordLogModel historyRecordLogModel) {
        var fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/lukagg13/hotelmanagementapp/history-component.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            nameLabel.textProperty().bind(historyRecordLogModel.nameProperty());
            timeStampLabel.textProperty().bind(historyRecordLogModel.timeStampProperty());
            oldObjectLabel.textProperty().bind(historyRecordLogModel.oldObjectProperty());
            newObjectLabel.textProperty().bind(historyRecordLogModel.newObjectProperty());
        } catch (IOException e) {
           log.error("Error loading history component {}", e.getMessage());
        }
    }
}