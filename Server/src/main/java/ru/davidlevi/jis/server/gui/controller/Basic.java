package ru.davidlevi.jis.server.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import ru.davidlevi.jis.server.core.interfaces.InterfaceServer;
import ru.davidlevi.jis.server.core.Server;

import java.util.List;

public class Basic {
    @FXML
    private Button btnStart;
    @FXML
    private Button btnDrop;
    @FXML
    private Button btnStop;
    @FXML
    private ListView<String> listView;

    /* Runtime */
    private final InterfaceServer serverInterface;
    private final ObservableList<String> data;

    {
        serverInterface = new Server();
        serverInterface.setFxContext(this);
        data = FXCollections.observableArrayList();
    }

    /**
     * Run second
     */
    @FXML
    private void initialize() {
        /* Обработчики */
        btnStart.setOnAction(eventStart);
        btnDrop.setOnAction(eventDrop);
        btnStop.setOnAction(eventStop);
    }

    /**
     * Метод устанавливает данные из класса Client
     *
     * @param list List<String>
     */
    public void update(List<String> list) {
        data.setAll(list);
        listView.setItems(data);
    }

    /**
     * Событие: Start
     */
    private EventHandler<ActionEvent> eventStart = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            serverInterface.start();
        }
    };

    /**
     * Событие: Drop
     */
    private EventHandler<ActionEvent> eventDrop = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            serverInterface.dropAll();
        }
    };

    /**
     * Событие: Stop
     */
    private EventHandler<ActionEvent> eventStop = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            serverInterface.stop();
        }
    };
}
