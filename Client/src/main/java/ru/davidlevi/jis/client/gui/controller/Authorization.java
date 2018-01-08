package ru.davidlevi.jis.client.gui.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
        /* CSS */
        lblHeader.getStyleClass().add("label-header");

        /* Обработчики */
        btnSignIn.setOnAction(eventSignIn);
        btnCheckIn.setOnAction(eventCheckIn);
        passwordField.setOnKeyPressed(keyEventSignIn);
    }

    private void authentication() {
        clientInterface.authenticationRequest(loginField.getText(), passwordField.getText());
    }

    /* Событие: Sign In */
    private EventHandler<ActionEvent> eventSignIn = actionEvent -> authentication();

    /* Событие: Sign In от passwordField */
    private EventHandler<KeyEvent> keyEventSignIn = event -> {
        if (event.getCode().equals(KeyCode.ENTER))
            authentication();
    };

    /* Событие: Check In */
    private EventHandler<ActionEvent> eventCheckIn = new EventHandler<>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            fxContext.setContentView(Layout.REGISTRATION);
        }
    };
}
