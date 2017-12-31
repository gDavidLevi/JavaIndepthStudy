package ru.davidlevi.jis.client.gui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import ru.davidlevi.jis.client.FxContext;
import ru.davidlevi.jis.client.core.Client;
import ru.davidlevi.jis.client.core.interfaces.ClientInterface;
import ru.davidlevi.jis.client.gui.model.Record;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Fxml-контроллер Explorer
 */
public class Explorer {
    @FXML
    private ImageView imgBack;
    @FXML
    private ImageView imgHome;
    @FXML
    private Label lblCurrentRemoteFolder;
    @FXML
    private TableView<Record> listTable;
    @FXML
    private Label labelLineInfo;
    @FXML
    private TableColumn<Record, String> columnType;
    @FXML
    private TableColumn<Record, String> columnName;
    @FXML
    private TableColumn<Record, String> columnSize;
    @FXML
    private TableColumn<Record, String> columnDate;

    /* Runtime */
    private FxContext fxContext;
    private ClientInterface clientInterface;
    private HashMap<String, String> listSendFiles; // Буффер хранения списка отправляемых файлов
    private ObservableList<Record> data;
    private ContextMenu contextMenu;
    private MenuItem menuItemDownload;

    {
        clientInterface = new Client();
        listSendFiles = new HashMap<>();
        data = FXCollections.observableArrayList();
    }

    /**
     * Метод сохраняет ссылку на главный FX-контекст
     *
     * @param fxContext FxContext
     */
    public void setFxContext(FxContext fxContext) {
        this.fxContext = fxContext;
        clientInterface.setFxContext(fxContext);
        fxContext.getStage().setWidth(610);
        fxContext.getStage().setHeight(400);
    }

    /**
     * Run second
     */
    @FXML
    private void initialize() {
        clientInterface = new Client();
        clientInterface.setFxContext(fxContext);
        clientInterface.changeDir("/");
        lblCurrentRemoteFolder.setText("/");

        /* CSS */
        lblCurrentRemoteFolder.getStyleClass().add("label-path");

        /* Настройка таблицы */
        listTable.setEditable(false);

        /* Колонки таблицы */
        columnType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        columnDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        columnType.setCellFactory(cellFactoryColumnType);

        /* Контекстное меню */
        contextMenu = new ContextMenu();
        MenuItem menuItemCreateFolder = new MenuItem("New folder");
        menuItemCreateFolder.setOnAction(eventCreateFolder);
        contextMenu.getItems().add(menuItemCreateFolder);
        menuItemDownload = new MenuItem("Download");
        menuItemDownload.setOnAction(eventDownload);
        contextMenu.getItems().add(menuItemDownload);
        MenuItem menuItemUpload = new MenuItem("Upload");
        menuItemUpload.setOnAction(eventUpload);
        contextMenu.getItems().add(menuItemUpload);
        MenuItem menuItemDelete = new MenuItem("Delete");
        menuItemDelete.setOnAction(eventDelete);
        contextMenu.getItems().add(menuItemDelete);
        MenuItem menuItemRename = new MenuItem("Rename");
        menuItemRename.setOnAction(eventRename);
        contextMenu.getItems().add(menuItemRename);
        listTable.setContextMenu(contextMenu);

        /* Обработчики */
        imgHome.setOnMouseClicked(eventChangeDirRoot);
        imgBack.setOnMouseClicked(eventChangeDirBack);
        listTable.setOnMousePressed(eventMouseOnListTable);
    }

    /**
     * Фабрика отрисовки данных в ячееках таблицы в колонке 'columnType'.
     * Отвечает за установку картинок к ячейки колонки, если значение
     * в колонке равно "dir", то "images/places/folder-blue.png", если
     * в колонке значение "file", то "images/mimetypes/text-plain.png".
     */
    private Callback<TableColumn<Record, String>, TableCell<Record, String>> cellFactoryColumnType
            = new Callback<TableColumn<Record, String>, TableCell<Record, String>>() {
        @Override
        public TableCell<Record, String> call(TableColumn<Record, String> param) {
            final Image imageFolder = new Image("images/dir.png");
            final Image imageFile = new Image("images/file.png");
            return new TableCell<Record, String>() {
                private final ImageView imageView = new ImageView();

                {
                    imageView.setFitWidth(16);
                    imageView.setFitHeight(16);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setGraphic(imageView);
                    else if (item.equals("dir")) imageView.setImage(imageFolder);
                    else if (item.equals("file")) imageView.setImage(imageFile);
                    setGraphic(imageView);
                }
            };
        }
    };

    /**
     * Метод устанавливает данные из класса Client
     *
     * @param list List<Record>
     */
    public void update(List<Record> list) {
        data.setAll(list);
        listTable.setItems(data);
        listTable.refresh();
        listTable.getSelectionModel().select(0);
    }

    /**
     * Доступ к списку отправляемых файлов
     *
     * @return HashMap
     */
    public HashMap<String, String> getListSendFiles() {
        return listSendFiles;
    }

    /**
     * Доступ к информационной линии
     *
     * @param message String
     */
    public void setLineInfo(String message) {
        labelLineInfo.setText(message);
    }

    /**
     * Доступ из класса Client
     *
     * @return String
     */
    public String getCurrentRemoteFolder() {
        return lblCurrentRemoteFolder.getText();
    }

    /**
     * Событие: Home
     */
    private EventHandler<MouseEvent> eventChangeDirRoot = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            changeDir("/");
        }
    };

    /**
     * Событие: Back
     */
    private EventHandler<MouseEvent> eventChangeDirBack = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            changeDir(Paths.get(lblCurrentRemoteFolder.getText(), "..").normalize().toAbsolutePath()
                    .toString());
        }
    };

    /**
     * Событие: Download
     */
    private EventHandler<ActionEvent> eventDownload = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            /* Только для файлов */
            if (listTable.getSelectionModel().getSelectedItem().getType().equals("file")) {
                /* Выбрали: */
                String remoteFile = listTable.getSelectionModel().getSelectedItem().getName();

                /* Выбираем куда сохранять запрашиваемый файл */
                FileChooser fileSaveChooser = new FileChooser();
                fileSaveChooser.setTitle("Save file to");
                fileSaveChooser.setInitialFileName(remoteFile);
                String localFile = fileSaveChooser.showSaveDialog(fxContext.getStage()).toString();
                if (localFile != null)
                    clientInterface.receiveFile(remoteFile, localFile);
            }
        }
    };

    /**
     * Событие: Upload
     */
    private EventHandler<ActionEvent> eventUpload = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            /* Выбираем что будем загружать в хранилище */
            FileChooser fileOpenChooser = new FileChooser();
            fileOpenChooser.setTitle("Upload file");
            File localFilename = fileOpenChooser.showOpenDialog(fxContext.getStage());

            /* Формирование пути для сохранения на сервере */
            String remoteFilename = Paths.get(lblCurrentRemoteFolder.getText(),
                    localFilename.getName()).toString();

            /* Сохраняем в список отправки */
            listSendFiles.put(remoteFilename, localFilename.toString());

            /* Отправляем запрос на передачу */
            clientInterface.sendFileRequest(remoteFilename);
        }
    };

    /**
     * Событие: Клики в listTable
     */
    private EventHandler<MouseEvent> eventMouseOnListTable = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            String type = listTable.getSelectionModel().getSelectedItem().getType();
            String name = listTable.getSelectionModel().getSelectedItem().getName();

            /* Клик правой кнопкой мыши: */
            // Не показывать Download, если кликнули по папке
            if (e.isSecondaryButtonDown() && type.equals("dir"))
                menuItemDownload.setVisible(false);
            else
                menuItemDownload.setVisible(true);

            // Не показывать контекстное меню, если кликнули по ".." или "."
            if (e.isSecondaryButtonDown() && (name.equals("..") || name.equals(".")))
                listTable.setContextMenu(null);
            else
                listTable.setContextMenu(contextMenu);

            /* Двойной клик на строке: */
            /* Переход в каталог */
            if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                if (type.equals("dir"))
                    changeDir(Paths.get(lblCurrentRemoteFolder.getText(), name).toString());
            }
        }
    };

    /* Change directory */
    private void changeDir(String path) {
        clientInterface.changeDir(path);
        lblCurrentRemoteFolder.setText(path);
    }

    /**
     * Событие: Create Folder
     */
    private EventHandler<ActionEvent> eventCreateFolder = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            TextInputDialog dialog = new TextInputDialog("new folder");
            dialog.setTitle("Create");
            dialog.setHeaderText("What name should you assign to a new folder");
            dialog.setContentText("Please input name");
            //
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newFolderName -> clientInterface.createFolder(newFolderName));
        }
    };

    /**
     * Событие: Delete
     */
    private EventHandler<ActionEvent> eventDelete = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            Path path = Paths.get(lblCurrentRemoteFolder.getText(),
                    listTable.getSelectionModel().getSelectedItem().getName());
            String type = listTable.getSelectionModel().getSelectedItem().getType();
            //
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Delete");
            alert.setContentText("Действительно хотите удалить?");
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK && isFileOrDir(type))
                    clientInterface.delete(path);
            });
        }
    };

    /**
     * Событие: Rename
     */
    private EventHandler<ActionEvent> eventRename = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Rename");
            dialog.setHeaderText("What name should you assign to a new name");
            dialog.setContentText("Please input name");
            //
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                Path oldPathName = Paths.get(lblCurrentRemoteFolder.getText(),
                        listTable.getSelectionModel().getSelectedItem().getName());
                Path newPathName = Paths.get(lblCurrentRemoteFolder.getText(),
                        newName);
                String type = listTable.getSelectionModel().getSelectedItem().getType();
                if (isFileOrDir(type))
                    clientInterface.rename(oldPathName, newPathName);
            });
        }
    };

    /**
     * Это файл или каталог?
     *
     * @param type String
     * @return boolean
     */
    private boolean isFileOrDir(String type) {
        return type.equals("file") || type.equals("dir");
    }

    //--
}