package io.github.lukagg13.hotelmanagementapp.ui.model;

import io.github.lukagg13.hotelmanagementapp.file.HistoryRecordLog;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class used to display {@link HistoryRecordLog} entries in the app.
 */
public class HistoryRecordLogModel {
    private final SimpleStringProperty timeStamp = new SimpleStringProperty();
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty oldObject = new SimpleStringProperty();
    private final SimpleStringProperty newObject = new SimpleStringProperty();

    /**
     * Creates a new instance of {@link HistoryRecordLogModel} from an existing {@link HistoryRecordLog}.
     * @param historyRecordLog the log entry to copy values from.
     */
    public HistoryRecordLogModel(HistoryRecordLog historyRecordLog) {
        timeStamp.setValue(historyRecordLog.timeStamp().toString());
        name.setValue(historyRecordLog.name());
        oldObject.setValue(historyRecordLog.oldObject() == null ? "" : historyRecordLog.oldObject().toString());
        newObject.setValue(historyRecordLog.newObject() == null ? "" : historyRecordLog.newObject().toString());
    }

    /**
     * Returns the timestamp of the log entry.
     * @return the timestamp.
     */
    public String getTimeStamp() {
        return timeStamp.get();
    }

    /**
     * Returns the timestamp property.
     * @return the timestamp property.
     */
    public SimpleStringProperty timeStampProperty() {
        return timeStamp;
    }

    /**
     * Returns the name of the log entry.
     * @return the name.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Returns the name property.
     * @return the name property.
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Returns the old object as a string.
     * @return the old object.
     */
    public String getOldObject() {
        return oldObject.get();
    }

    /**
     * Returns the old object property.
     * @return the old object property.
     */
    public SimpleStringProperty oldObjectProperty() {
        return oldObject;
    }

    /**
     * Returns the new object as a string.
     * @return the new object.
     */
    public String getNewObject() {
        return newObject.get();
    }

    /**
     * Returns the new object property.
     * @return the new object property.
     */
    public SimpleStringProperty newObjectProperty() {
        return newObject;
    }
}