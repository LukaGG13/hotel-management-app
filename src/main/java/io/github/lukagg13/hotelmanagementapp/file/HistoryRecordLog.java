package io.github.lukagg13.hotelmanagementapp.file;

import java.io.Serializable;
import java.time.LocalDateTime;

public record HistoryRecordLog(String name, LocalDateTime timeStamp, Serializable oldObject, Serializable newObject) implements Serializable {}

