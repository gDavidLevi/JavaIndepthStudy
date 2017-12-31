package ru.davidlevi.jis.client.gui.controller;

import javafx.fxml.FXML;
import ru.davidlevi.jis.client.FxContext;
import ru.davidlevi.jis.client.core.Client;
import ru.davidlevi.jis.client.core.interfaces.ClientInterface;

/**
 * Fxml-контроллер Basic
 */
public class Basic {
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
    }

    /**
     * Run first
     */
    public Basic() {
    }

    /**
     * Run second
     */
    @FXML
    private void initialize() {
    }
}
