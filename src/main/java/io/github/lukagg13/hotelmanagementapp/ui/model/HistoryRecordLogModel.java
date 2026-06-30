package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.file.HistoryRecordLog;
import javafx.beans.property.SimpleStringProperty;

public class HistoryRecordLogModel {
    private final SimpleStringProperty timeStamp = new SimpleStringProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty oldObject = new SimpleStringProperty();
    private final SimpleStringProperty newObject = new SimpleStringProperty();

    public HistoryRecordLogModel(HistoryRecordLog historyRecordLog) {
        timeStamp.setValue(historyRecordLog.timeStamp().toString());
        name.setValue(historyRecordLog.name());
        oldObject.setValue(historyRecordLog.oldObject() == null ? "" : historyRecordLog.oldObject().toString());
        newObject.setValue(historyRecordLog.newObject() == null ? "" : historyRecordLog.newObject().toString());
    }

    public String getTimeStamp() {
        return timeStamp.get();
    }

    public SimpleStringProperty timeStampProperty() {
        return timeStamp;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getOldObject() {
        return oldObject.get();
    }

    public SimpleStringProperty oldObjectProperty() {
        return oldObject;
    }

    public String getNewObject() {
        return newObject.get();
    }

    public SimpleStringProperty newObjectProperty() {
        return newObject;
    }
}
