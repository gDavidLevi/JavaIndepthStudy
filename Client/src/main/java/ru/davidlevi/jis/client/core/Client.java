package ru.davidlevi.jis.client.core;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.davidlevi.jis.client.FxContext;
import ru.davidlevi.jis.client.core.interfaces.ClientInterface;
import ru.davidlevi.jis.client.gui.Layout;
import ru.davidlevi.jis.client.gui.model.Record;
import ru.davidlevi.jis.common.network.Transmitter;
import ru.davidlevi.jis.common.network.ClientThread;
import ru.davidlevi.jis.common.network.Direction;
import ru.davidlevi.jis.common.network.interfaces.ByteStreamThreadListener;
import ru.davidlevi.jis.common.network.interfaces.ClientThreadListener;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Client
 */
public class Client extends Settings implements ClientThreadListener, ByteStreamThreadListener, ClientInterface {
    /* Поля класса */
    private static ClientThread client;
    private static Socket socket;

    /* Runtime */
    private FxContext fxContext;

    /* Клиенская нить готова к приему/передачи? */
    private boolean isClientThreadReady = false;

    /* Конструктор */
    public Client() {
        loadClientSettings();
    }

    /**
     * Метод создает соккет и клиентскую нить
     */
    private void connect() {
        try {
            socket = new Socket(InetAddress.getByName(getHostname()).getHostAddress(), getPort());
            client = new ClientThread(this, "Client", socket);
        } catch (ConnectException e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Exception");
                alert.setContentText("Connection refused!");
                alert.showAndWait();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Ждем подключения к сокету сервера и запуска клиентского потока */
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод закрывает клиентскую нить и соккет
     */
    private void disconnect() {
        try {
            if (client != null) client.close();
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Отправка объекта
     *
     * @param jsonObject JSONObject
     */
    private void post(JSONObject jsonObject) {
        client.post(jsonObject);
    }

    /*
     * Методы слушателя ClientThreadListener:
     */

    @Override
    public void onStart_ClientThread(ClientThread clientThread) {
        System.out.println("Client.onStart");
    }

    @Override
    public void onReady_ClientThread(ClientThread clientThread, Socket socketClient) {
        System.out.println("Client.onReady");
        isClientThreadReady = true;
    }

    @Override
    public void onGet_ClientThread(ClientThread clientThread, Socket socketClient, JSONObject get) {
        System.out.println("Client.onGet : <-- \n" + get.toString(4));
        handlerJSONObjects(get);
    }

    @Override
    public void onPost_ClientThread(ClientThread clientThread, Socket socketClient, JSONObject jsonObject) {
        System.out.println("Client.onPost : --> \n" + jsonObject.toString(4));
    }

    @Override
    public void onException_ClientThread(ClientThread clientThread, Socket socketClient, Exception exception) {
        System.out.print("Client.onException : ");
        if (exception instanceof EOFException)
            System.out.println("The user has disconnected.");
        if (exception instanceof SocketException)
            System.out.println("Socket closed.");
    }

    @Override
    public void onStop_ClientThread(ClientThread clientThread) {
        System.out.println("Client.onStop");
        Platform.runLater(() -> {
            fxContext.setContentView(Layout.AUTHORIZATION);
        });
    }

    /*
     * Методы слушателя ByteStreamThreadListener:
     */

    @Override
    public void onStart(Transmitter transmitter, Direction direction, String message) {
        // не обрабатываю
    }

    @Override
    public void onProgress(Transmitter transmitter, Direction direction, String treadName, long bytes) {
        Platform.runLater(() -> {
            if (direction == Direction.RECEIVE)
                fxContext.getControllerExplorer().setLineInfo("Receive: " + treadName + " " + String.valueOf(bytes) + " bytes");
            if (direction == Direction.SEND)
                fxContext.getControllerExplorer().setLineInfo("Send: " + treadName + " " + String.valueOf(bytes) + " bytes");
        });
    }

    @Override
    public void onException(Transmitter transmitter, Exception exception) {
        // не обрабатываю
    }

    @Override
    public void onStop(Transmitter transmitter, Direction direction, String message, ClientThread clientThread, String userFolderUUID, String path) {
        Platform.runLater(() -> {
            fxContext.getControllerExplorer().setLineInfo("ok");
        });
    }

    /**
     * Обработчик JSON запросов:
     * - "response": "list"
     * - "response": "alert"
     * - "response": "exit"
     * - "response": "authorization"
     * - "response": "registred"
     * - "response": "ready-upload"
     *
     * @param json JSONObject
     */
    private void handlerJSONObjects(JSONObject json) {
        //todo ОБРАБОТКА: "response": "list"
        if (json.getString("response").equals("list")) {
            /* Получим массивы с данными */
            JSONObject object = json.getJSONObject("data");
            JSONArray typeItems = object.getJSONArray("type");
            JSONArray nameItems = object.getJSONArray("name");
            JSONArray sizeItems = object.getJSONArray("size");
            JSONArray dateItems = object.getJSONArray("date");
            Platform.runLater(() -> {
                List<Record> records = new ArrayList<>();
                /* Добавим в список List<Record> записи из массивов */
                for (int i = 0; i < nameItems.length(); i++) {
                    records.add(new Record((String) typeItems.get(i),
                            (String) nameItems.get(i),
                            (String) sizeItems.get(i),
                            (String) dateItems.get(i))
                    );
                }
                /* Передадим список контроллеру для обработки */
                fxContext.getControllerExplorer().update(records);
            });
        }

        //todo ОБРАБОТКА: "response": "alert"
        if (json.getString("response").equals("alert")) {
            JSONObject data = json.getJSONObject("data");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(data.getString("title"));
                alert.setHeaderText(data.getString("header"));
                alert.setContentText(data.getString("content"));
                alert.showAndWait();
            });
        }

        //todo ОБРАБОТКА: "response": "inauthorize"
        if (json.getString("response").equals("inauthorize")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Information!");
                alert.setHeaderText("The application will be closed");
                alert.setContentText("The user joined the account\nfrom another application.");
                alert.showAndWait();
                System.exit(0);
            });
        }

        //todo ОБРАБОТКА: "response": "authorization"
        if (json.getString("response").equals("authorization")) {
            JSONObject data = json.getJSONObject("data");
            final String nickname = data.getString("nickname");
            Platform.runLater(() -> {
                fxContext.getStage().setTitle("Client : " + nickname);
                fxContext.setContentView(Layout.EXPLORER);
            });
        }

        //todo ОБРАБОТКА: "response": "registred"
        if (json.getString("response").equals("registred")) {
            JSONObject data = json.getJSONObject("data");
            String message = data.getString("message");
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration");
                alert.setHeaderText("Successfully");
                alert.setContentText(message);
                alert.showAndWait();
                fxContext.setContentView(Layout.AUTHORIZATION);
            });
            disconnect();
        }

        //todo ОБРАБОТКА: "response": "ready-upload"
        if (json.getString("response").equals("ready-upload")) {
            JSONObject data = json.getJSONObject("data");
            JSONObject socket = data.getJSONObject("socket");
            String serverHostname = socket.getString("hostname");
            int serverPort = socket.getInt("port");
            String key = socket.getString("key");

            /* Активация отправителя */
            Transmitter upload = new Transmitter(this);
            upload.initClient(client);
            upload.post(fxContext.getControllerExplorer().getListSendFiles().get(key), serverHostname, serverPort);
        }
    }

    /*
     * Методы интерфейса ClientInterface:
     */

    /**
     * Метод получает и сохраняет ссылку на главный FX-контекст
     *
     * @param fxContext FxContext
     */
    @Override
    public void setFxContext(FxContext fxContext) {
        this.fxContext = fxContext;
    }

    /**
     * Отправка запроса на аутентификацию
     *
     * @param login    String
     * @param password String
     */
    @Override
    public void authenticationRequest(String login, String password) {
        connect();
        if (isClientThreadReady) {
            //todo ОТПРАВКА: "request": "authentication"
            JSONObject request = new JSONObject();
            request.put("request", "authentication");
            JSONObject data = new JSONObject();
            data.put("login", login);
            data.put("password", password);
            request.put("data", data);
            post(request);
        }
    }

    /**
     * Отправка запроса на регистрацию
     *
     * @param login    String
     * @param password String
     * @param nickname String
     * @param email    String
     */
    @Override
    public void registrationRequest(String login, String password, String nickname, String email) {
        connect();
        if (isClientThreadReady) {
            //todo ОТПРАВКА: "request": "alert"
            JSONObject request = new JSONObject();
            request.put("request", "registration");
            JSONObject data = new JSONObject();
            data.put("login", login);
            data.put("password", password);
            data.put("nickname", nickname);
            data.put("email", email);
            request.put("data", data);
            post(request);
        }
    }

    /**
     * Запросить получение содержимого каталога на сервере и войти в него
     *
     * @param cd String
     */
    @Override
    public void changeDir(String cd) {
        //todo ОТПРАВКА: "request": "list"
        JSONObject request = new JSONObject();
        request.put("request", "list");
        JSONObject data = new JSONObject();
        data.put("path", cd);
        request.put("data", data);
        post(request);
    }

    /**
     * Создание каталога на сервере
     *
     * @param newFolder String
     */
    @Override
    public void createFolder(String newFolder) {
        String currentRemoteFolder = fxContext.getControllerExplorer().getCurrentRemoteFolder();
        //todo ОТПРАВКА: "request": "create-folder"
        JSONObject request = new JSONObject();
        request.put("request", "create-folder");
        JSONObject data = new JSONObject();
        data.put("new-folder", Paths.get(currentRemoteFolder, newFolder).toString());
        request.put("data", data);
        post(request);
    }

    /**
     * Получение файла с сервера
     *
     * @param remoteFile с сервера
     * @param localFile  в локальный файл
     */
    @Override
    public void receiveFile(String remoteFile, String localFile) {
        /* Активация загрузчика */
        Transmitter download = new Transmitter(this);
        download.initClient(client);
        download.get(localFile);
        int requestedPort = download.getReceiverPort();
        String requestedHostname = download.getReceiverExternalIP();

        /* Подготовка данных для запроса */
        String currentRemoteFolder = fxContext.getControllerExplorer().getCurrentRemoteFolder();
        String requestedFilename = Paths.get(currentRemoteFolder, remoteFile).toString();

        //todo ОТПРАВКА: "request": "download"
        JSONObject request = new JSONObject();
        request.put("request", "download");
        JSONObject data = new JSONObject();
        data.put("filename", requestedFilename);
        JSONObject socket = new JSONObject();
        socket.put("hostname", requestedHostname);
        socket.put("port", requestedPort);
        data.put("socket", socket);
        request.put("data", data);
        post(request);
    }

    /**
     * Отправка файла на сервер
     *
     * @param remoteFilename String
     */
    @Override
    public void sendFileRequest(String remoteFilename) {
        //todo ОТПРАВКА: "request": "upload"
        JSONObject request = new JSONObject();
        request.put("request", "upload");
        JSONObject data = new JSONObject();
        data.put("filename", remoteFilename);
        request.put("data", data);
        post(request);
    }

    /**
     * Завершение работы клиента.
     * Закрытие соккета и завершение нити.
     */
    @Override
    public void quit() {
        disconnect();
    }

    /**
     * Удаляет папку с содержимым или файл
     *
     * @param path Path
     */
    @Override
    public void delete(Path path) {
        //todo ОТПРАВКА: "request": "delete"
        JSONObject request = new JSONObject();
        request.put("request", "delete");
        JSONObject data = new JSONObject();
        data.put("path", path.toString());
        request.put("data", data);
        post(request);
    }

    /**
     * Переименовывает путь (файла или папки; имя файла)
     *
     * @param oldName Path
     * @param newName Path
     */
    @Override
    public void rename(Path oldName, Path newName) {
        //todo ОТПРАВКА: "request": "rename"
        JSONObject request = new JSONObject();
        request.put("request", "rename");
        JSONObject data = new JSONObject();
        data.put("old-path", oldName.toString());
        data.put("new-path", newName.toString());
        request.put("data", data);
        post(request);
    }
}
