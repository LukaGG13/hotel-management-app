module io.github.lukagg13.hotelmanagementapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens io.github.lukagg13.hotelmanagementapp to javafx.fxml;
    exports io.github.lukagg13.hotelmanagementapp;
}