package io.github.lukagg13.hotelmanagementapp.ui.controller;

import io.github.lukagg13.hotelmanagementapp.file.History;
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
                try {
                    History.readAllLogs().forEach(log -> {
                        //HistoryRecordController recordController = new HistoryRecordController(log);
                        //historyVBox.getChildren().add(recordController.getRoot());
                        Platform.runLater(() -> {
                            historyVBox.getChildren().clear();
                            historyVBox.getChildren().add(new Label(log.toString()));
                        });
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    historyVBox.getChildren().add(new Label("Error reading history: " + e.getMessage()));
                }
            }
        });
    }
        /*
        try {
            Thread.ofVirtual().start(() -> {
                while (!Thread.currentThread().isInterrupted()) {

                }
                try {
                    History.readAllLogs().forEach(log -> {
                HistoryRecordController recordController = new HistoryRecordController(log);
                historyVBox.getChildren().add(recordController.getRoot());
                        historyVBox.getChildren().add(new Label(log.toString()));

                    }
                }
                }
                } catch (IOException e) {
            historyVBox.getChildren().add(new Label("Error reading history: " + e.getMessage()));
        })});

                //loadHistory();


    }
                */
}
