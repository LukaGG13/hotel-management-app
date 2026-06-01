package io.github.lukagg13.hotelmanagementapp.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.java.services.LoginService;
import org.example.java.ui.RepositoryUiAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

        @FXML
        private TextField userNameTextField;

        @FXML
        private PasswordField passwordTextField;

        @FXML
        private Button loginButton; //TODO: ovo sam radio prije bumbija

        private final RepositoryUiAdapter repository;
        private final LoginService loginService;

        public LoginController(LoginService loginService, RepositoryUiAdapter repository) {
            this.loginService = loginService;
            this.repository = repository;
        }

        private void loginButtonClicked() {
            var name = userNameTextField.getText();
            var password = passwordTextField.getText();
            if (name.isBlank() || password.isBlank()) throw new IllegalStateException("Text fields empty");

            try {
                loginService.login(name, password);
                userNameTextField.setText("");
                passwordTextField.setText("");
                //TODO: maknuo sam privremeno jel mi se neda svaki put
                //(new Alert(Alert.AlertType.INFORMATION, "Logged in as " + repository.getActiveUser())).showAndWait();
            } catch (RuntimeException e) {
                (new Alert(Alert.AlertType.ERROR, e.getMessage())).showAndWait();
            }
        }

        @FXML
        private void initialize() {

            //loginButton.setOnAction(_ -> (new Alert(Alert.AlertType.INFORMATION, "hello")).showAndWait());
            loginButton.setOnAction(_ -> loginButtonClicked());

            //TODO passwordTextField = validating text field
            //TODD link na create account isto dodat stvar za password
            //repository.login();
        }
}
