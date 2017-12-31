package ru.davidlevi.jis.client.gui.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ru.davidlevi.jis.client.FxContext;
import ru.davidlevi.jis.client.core.Client;
import ru.davidlevi.jis.client.core.interfaces.ClientInterface;
import ru.davidlevi.jis.client.gui.Layout;

/**
 * Fxml-контроллер Registration
 */
public class Registration {
    @FXML
    private Label lblHeader;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnCheckIn;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;

    /* Runtime */
    private FxContext fxContext;
    private ClientInterface clientInterface;

    {
        clientInterface = new Client();
    }

    /**
     * Метод сохраняет ссылку на главный FX-контекст
     *
     * @param fxContext FxContext
     */
    public void setFxContext(FxContext fxContext) {
        this.fxContext = fxContext;
        clientInterface.setFxContext(fxContext);
        fxContext.getStage().setWidth(380);
        fxContext.getStage().setHeight(300);
    }

    /**
     * Run second
     */
    @FXML
    private void initialize() {
        clientInterface = new Client();
        clientInterface.setFxContext(fxContext);
        btnCheckIn.setOnAction(eventCheckIn);
        btnBack.setOnAction(eventBack);

        /* CSS */
        lblHeader.getStyleClass().add("label-header");
    }

    /* Событие: Sign In */
    private EventHandler<ActionEvent> eventCheckIn = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            if (loginField == null &
                    passwordField == null &
                    nicknameField == null &
                    emailField == null) {
                clientInterface.registrationRequest(loginField.getText(),
                        passwordField.getText(),
                        nicknameField.getText(),
                        emailField.getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration");
                alert.setHeaderText("Important");
                alert.setContentText("All fields are required");
            }
        }
    };

    /* Событие: Back */
    private EventHandler<ActionEvent> eventBack = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            fxContext.setContentView(Layout.AUTHORIZATION);
        }
    };
}
