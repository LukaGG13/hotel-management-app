package io.github.lukagg13.hotelmanagementapp.ui.controller.history;

import io.github.lukagg13.hotelmanagementapp.file.History;
import io.github.lukagg13.hotelmanagementapp.ui.model.HistoryRecordLogModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HistoryController {

    @FXML
    private VBox historyVBox;
    @FXML
    private void initialize() {
        Thread.ofVirtual().start(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                History.readAllLogs().forEach(log -> {
                    Platform.runLater(() -> {
                        HistoryComponentController historyComponentController = new HistoryComponentController(new HistoryRecordLogModel(log));
                        historyVBox.getChildren().add(historyComponentController);
                    });
                });

                try {
                    Thread.sleep(3000);
                    Platform.runLater(() -> {
                        historyVBox.getChildren().clear();
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
