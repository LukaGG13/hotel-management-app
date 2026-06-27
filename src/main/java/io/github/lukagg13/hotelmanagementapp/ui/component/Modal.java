package io.github.lukagg13.hotelmanagementapp.ui.component;

import io.github.lukagg13.hotelmanagementapp.ViewManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class Modal<C, R> {

    private final ViewManager.ViewPath viewPath;
    private final String title;
    private final Modality modality;
    private final C controller;
    private final Function<C, R> mapper;

    private Modal(ModalBuilder<C, R> builder) {
        this.viewPath = builder.viewPath;
        this.title = builder.title;
        this.modality = builder.modality;
        this.controller = builder.controller;
        this.mapper = builder.mapper;
    }

    public Optional<R> showAndWait() {
        try {
            FXMLLoader loader = new FXMLLoader(viewPath.path);
            //FXMLLoader loader = new FXMLLoader(Modal.class());

            if (controller != null) {
                loader.setController(controller);
            }

            Parent root = loader.load();
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.initModality(modality);
            modalStage.setScene(new Scene(root));

            modalStage.showAndWait();

            C activeController = (controller != null) ? controller : loader.getController();

            if (mapper != null && activeController != null) {
                return Optional.ofNullable(mapper.apply(activeController));
            }

            return Optional.empty();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load modal view: " + viewPath, e);
        }
    }

    public static final class ModalBuilder<C, R> {
        private final ViewManager.ViewPath viewPath;
        private String title = "Modal";
        private Modality modality = Modality.APPLICATION_MODAL;
        private C controller;
        private Function<C, R> mapper;

        public ModalBuilder(ViewManager.ViewPath viewPath) {
            this.viewPath = viewPath;
        }

        public ModalBuilder<C, R> title(String title) {
            this.title = title;
            return this;
        }

        public ModalBuilder<C, R> modality(Modality modality) {
            this.modality = modality;
            return this;
        }

        public ModalBuilder<C, R> controller(C controller) {
            this.controller = controller;
            return this;
        }

        public ModalBuilder<C, R> mapper(Function<C, R> mapper) {
            this.mapper = mapper;
            return this;
        }

        public Modal<C, R> build() {
            return new Modal<>(this);
        }
    }
}