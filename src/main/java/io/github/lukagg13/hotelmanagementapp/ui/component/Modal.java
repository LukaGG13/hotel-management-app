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

/**
 * Class used to display a modal view in the app.
 * @param <C> The type of the controller that will be passed in to the view.
 * @param <R> The return type of modal.
 */
public class Modal<C, R> {

    private final ViewManager.ViewPath viewPath;
    private final String title;
    private final Modality modality;
    private final C controller;
    private final Function<C, R> mapper;

    /**
     * Returns the new {@link Modal} from the builder.
     * @param builder The builder that will be used to create the object.
     */
    private Modal(ModalBuilder<C, R> builder) {
        this.viewPath = builder.viewPath;
        this.title = builder.title;
        this.modality = builder.modality;
        this.controller = builder.controller;
        this.mapper = builder.mapper;
    }

    /**
     * Method used to show the modal.
     * @return An {@link Optional} of the result type. It will be empty if a controller and a mapper function weren't passed.
     */
    public Optional<R> showAndWait() {
        try {
            FXMLLoader loader = new FXMLLoader(viewPath.path);

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
            throw new IllegalArgumentException("Failed to load modal view: " + viewPath, e);
        }
    }

    /**
     * Builder class used for creating {@link Modal} objects.
     * @param <C> The type of the controller that will be passed in to the view.
     * @param <R> The return type of modal.
     */
    public static final class ModalBuilder<C, R> {
        private final ViewManager.ViewPath viewPath;
        private String title = "Modal";
        private Modality modality = Modality.APPLICATION_MODAL;
        private C controller;
        private Function<C, R> mapper;

        /**
         * Return a new {@link ModalBuilder} object.
         * @param viewPath The {@link ViewManager.ViewPath} that will be displayed in the modal.
         */
        public ModalBuilder(ViewManager.ViewPath viewPath) {
            this.viewPath = viewPath;
        }

        /**
         * Sets the title of the {@link Modal}.
         * @param title The title that will be set.
         * @return {@link ModalBuilder} for builder pattern.
         */
        public ModalBuilder<C, R> title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the modality of the {@link Modal}.
         * @param modality The {@link Modality} that will be set.
         * @return {@link ModalBuilder} for builder pattern.
         */
        public ModalBuilder<C, R> modality(Modality modality) {
            this.modality = modality;
            return this;
        }

        /**
         * Sets the controller for the view.
         * @param controller The controller that will be used for the view.
         * @return {@link ModalBuilder} for builder pattern.
         */
        public ModalBuilder<C, R> controller(C controller) {
            this.controller = controller;
            return this;
        }

        /**
         * Sets the mapper function for getting the result from the controller.
         * @param mapper The {@link Function} that will be used to get results.
         * @return {@link ModalBuilder} for builder pattern.
         */
        public ModalBuilder<C, R> mapper(Function<C, R> mapper) {
            this.mapper = mapper;
            return this;
        }

        /**
         * Returns a new instance of the {@link Modal}.
         * @return The constructed modal.
         */
        public Modal<C, R> build() {
            return new Modal<>(this);
        }
    }
}