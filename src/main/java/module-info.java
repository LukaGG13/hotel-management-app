module io.github.lukagg13.hotelmanagementapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires jbcrypt;
    requires java.desktop;
    requires io.github.lukagg13.hotelmanagementapp;


    opens io.github.lukagg13.hotelmanagementapp to javafx.fxml;
    opens io.github.lukagg13.hotelmanagementapp.file to jakarta.json.bind;
    opens io.github.lukagg13.hotelmanagementapp.ui.controller to javafx.fxml;
    opens io.github.lukagg13.hotelmanagementapp.ui.component to javafx.fxml;
    opens io.github.lukagg13.hotelmanagementapp.ui.controller.guest to javafx.fxml;
    opens io.github.lukagg13.hotelmanagementapp.ui.controller.room to javafx.fxml;
    opens io.github.lukagg13.hotelmanagementapp.ui.controller.booking to javafx.fxml;
    exports io.github.lukagg13.hotelmanagementapp;
    opens io.github.lukagg13.hotelmanagementapp.ui.controller.history to javafx.fxml;
    exports io.github.lukagg13.hotelmanagementapp.ui;
    opens io.github.lukagg13.hotelmanagementapp.ui to javafx.fxml;
}