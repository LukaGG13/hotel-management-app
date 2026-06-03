module io.github.lukagg13.hotelmanagementapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;


    opens io.github.lukagg13.hotelmanagementapp to javafx.fxml;
    opens io.github.lukagg13.hotelmanagementapp.ui.controller to javafx.fxml;
    exports io.github.lukagg13.hotelmanagementapp;
}