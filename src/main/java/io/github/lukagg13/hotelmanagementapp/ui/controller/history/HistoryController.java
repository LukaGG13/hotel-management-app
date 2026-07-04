package io.github.lukagg13.hotelmanagementapp.ui.controller.history;

import io.github.lukagg13.hotelmanagementapp.file.History;
import io.github.lukagg13.hotelmanagementapp.ui.model.HistoryRecordLogModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Controller used to controlle the history view.
 */
public class HistoryController {

    @FXML
    private VBox historyVBox;

    private static final Logger log = LoggerFactory.getLogger(HistoryController.class);

    /**
     * Used to initialize javaFX.
     */
    @FXML
    private void initialize() {
        Thread.ofVirtual().start(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                History.readAllLogs().forEach(historyLog ->
                    Platform.runLater(() -> {
                        HistoryComponentController historyComponentController = new HistoryComponentController(new HistoryRecordLogModel(historyLog));
                        historyVBox.getChildren().add(historyComponentController);
                    })
                );

                try {
                    Thread.sleep(3000);
                    Platform.runLater(() ->
                        historyVBox.getChildren().clear()
                    );
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.warn("Interrupted {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
