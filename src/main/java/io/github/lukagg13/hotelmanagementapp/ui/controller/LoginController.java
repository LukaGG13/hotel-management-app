package io.github.lukagg13.hotelmanagementapp.ui.controller;

import io.github.lukagg13.hotelmanagementapp.exception.IncorrectPasswordException;
import io.github.lukagg13.hotelmanagementapp.exception.UserNotFoundException;
import io.github.lukagg13.hotelmanagementapp.service.LoginService;
import io.github.lukagg13.hotelmanagementapp.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

        @FXML
        private TextField userNameTextField;
        @FXML
        private PasswordField passwordTextField;
        @FXML
        private Button loginButton;

        private void loginButtonClicked() {
            var name = userNameTextField.getText();
            var password = passwordTextField.getText();
            if (name.isBlank() || password.isBlank()) throw new IllegalStateException("Text fields empty");

            try {
                LoginService.login(name, password);
                userNameTextField.setText("");
                passwordTextField.setText("");

                (new Alert(Alert.AlertType.INFORMATION, "Successful login")).showAndWait();
                ViewManager.switchView(ViewManager.ViewPath.GUEST);
            } catch (UserNotFoundException | IncorrectPasswordException e) {
                if(e instanceof UserNotFoundException) log.error("Trying to log in as wrong user name");
                if(e instanceof IncorrectPasswordException) log.error("Trying to log in with wrong password");

                (new Alert(Alert.AlertType.ERROR, "User name or password is wrong please try again." )).showAndWait();
            }
        }

        @FXML
        private void initialize() {
            loginButton.setOnAction(_ -> loginButtonClicked());
        }
}
