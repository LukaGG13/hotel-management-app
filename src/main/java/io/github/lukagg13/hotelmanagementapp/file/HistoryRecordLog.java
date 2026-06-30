package io.github.lukagg13.hotelmanagementapp.file;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A {@link Record} {@link Class} used to represent the log in {@link History}.
 * @param name The name of the person who caused the change being documented.
 * @param timeStamp The {@link LocalDateTime} of when the change happened.
 * @param oldObject The old state of the object.
 * @param newObject The new state of the object.
 */
public record HistoryRecordLog(String name, LocalDateTime timeStamp, Serializable oldObject, Serializable newObject) implements Serializable {}