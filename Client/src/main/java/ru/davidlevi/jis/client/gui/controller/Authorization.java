package ru.davidlevi.jis.client.gui.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.davidlevi.jis.client.FxContext;
import ru.davidlevi.jis.client.core.Client;
import ru.davidlevi.jis.client.core.interfaces.ClientInterface;
import ru.davidlevi.jis.client.gui.Layout;

/**
 * Fxml-контроллер Authorization
 */
public class Authorization {
    @FXML
    private Label lblHeader;
    @FXML
    private Button btnSignIn;
    @FXML
    private Button btnCheckIn;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

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
        fxContext.getStage().setHeight(240);
    }

    /**
     * Run second
     */
    @FXML
    private void initialize() {
        btnSignIn.setOnAction(eventSignIn);
        btnCheckIn.setOnAction(eventCheckIn);

        /* CSS */
        lblHeader.getStyleClass().add("label-header");
    }

    /* Событие: Sign In */
    private EventHandler<ActionEvent> eventSignIn = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            clientInterface.authenticationRequest(loginField.getText(),
                    passwordField.getText());
        }
    };

    /* Событие: Check In */
    private EventHandler<ActionEvent> eventCheckIn = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            fxContext.setContentView(Layout.REGISTRATION);
        }
    };
}
