package ru.davidlevi.jis.server.core;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.davidlevi.jis.common.network.Transmitter;
import ru.davidlevi.jis.common.network.Direction;
import ru.davidlevi.jis.common.network.interfaces.ByteStreamThreadListener;
import ru.davidlevi.jis.common.notuse.Dater;
import ru.davidlevi.jis.server.core.interfaces.InterfaceServer;
import ru.davidlevi.jis.common.network.ClientThread;
import ru.davidlevi.jis.server.gui.controller.Basic;
import ru.davidlevi.jis.server.network.interfaces.ServerThreadListener;
import ru.davidlevi.jis.server.database.Database;
import ru.davidlevi.jis.server.database.interfaces.InterfaceDatabase;
import ru.davidlevi.jis.server.network.ServerThread;
import ru.davidlevi.jis.common.network.interfaces.ClientThreadListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class Server extends Settings implements ServerThreadListener, ClientThreadListener, InterfaceServer, ByteStreamThreadListener {
    private Logger logger;
    private static InterfaceDatabase database;
    private ServerThread serverThread;
    private static Vector<ClientThread> listThreads;
    private Basic fxContext;
    private List<String> list;

    {
        logger = LogManager.getLogger(Server.class);
        listThreads = new Vector<>();
        database = new Database();
        list = new ArrayList<>();
    }

    public Server() {
        loadServerSettings();
    }

    /**
     * Метод собирает логи данного класса
     *
     * @param type    String
     * @param message String
     */
    private void log(String type, String message) {
        switch (type) {
            case "info":
                logger.info(message);
                break;
            case "error":
                logger.error(message);
                break;
            case "debug":
                logger.debug(message);
                break;
            case "warn":
                logger.warn(message);
                break;
            default:
                logger.info(message);
                break;
        }
        list.add(message);
        Platform.runLater(() -> {
            fxContext.update(list);
        });
    }

    /**
     * Метод получает и сохраняет ссылку на главный FX-контекст
     */
    @Override
    public void setFxContext(Basic fxContext) {
        this.fxContext = fxContext;
    }

    private void stopListening() {
        if (serverThread == null || !serverThread.isAlive()) {
            log("info", "Поток сервера не запущен.");
            return;
        }
        serverThread.interrupt();
        database.disconnect();
    }

    @Override
    public void start() {
        database.connect();
        if (serverThread != null && serverThread.isAlive()) {
            log("info", "Поток сервера уже запущен.");
            return;
        }
        serverThread = new ServerThread(this, "ServerThread", super.getPort(), super.getBacklog(), super.getHostname(), 2000);
    }

    @Override
    public synchronized void dropAll() {
        for (ClientThread listThread : listThreads)
            listThread.close();
    }


    @Override
    public void stop() {
        dropAll();
        stopListening();
    }

    // ============================================================================================

    @Override
    public void onStart_ServerThread(ServerThread serverThread) {
        log("info", "onStart");
    }

    @Override
    public void onStop_ServerThread(ServerThread serverThread) {
        log("info", "onStop");
    }

    @Override
    public void onReady_ServerThread(ServerThread serverThread, ServerSocket serverSocket) {
        log("info", "onReady");
    }

    @Override
    public void onTimeOutAccept_ServerThread(ServerThread serverThread, ServerSocket serverSocket) {
        // Не обрабатывать пока!
    }

    @Override
    public void onAccepted_ServerThread(ServerThread serverThread, ServerSocket serverSocket, Socket acceptedClientSocket) {
        log("info", "onAccepted : " + acceptedClientSocket);
        new User(this, acceptedClientSocket);
    }

    @Override
    public void onException_ServerThread(ServerThread serverThread, Exception e) {
        log("info", "onException : " + e);
    }

    // ============================================================================================

    @Override
    public synchronized void onStart_ClientThread(ClientThread clientThread) {
        log("info", "onStart : User " + ((User) clientThread).getName() + " on " + clientThread.getSocketClient());
    }

    @Override
    public synchronized void onReady_ClientThread(ClientThread clientThread, Socket socketClient) {
        log("info", "onReady : User " + ((User) clientThread).getName() + " on " + clientThread.getSocketClient());
        listThreads.add(clientThread);
    }

    @Override
    public synchronized void onGet_ClientThread(ClientThread clientThread, Socket socketClient, JSONObject jsonObject) {
        log("info", "onGet : User " + ((User) clientThread).getName() + " on " + clientThread.getSocketClient() + " : <-- \n" + jsonObject.toString(4));
        handlerJSONObjects(clientThread, jsonObject);
    }

    @Override
    public synchronized void onPost_ClientThread(ClientThread clientThread, Socket socketClient, JSONObject jsonObject) {
        log("info", "onPost : User " + ((User) clientThread).getName() + " on " + clientThread.getSocketClient() + " : --> \n" + jsonObject.toString(4));
    }

    @Override
    public synchronized void onException_ClientThread(ClientThread clientThread, Socket socketClient, Exception exception) {
        log("info", "onException : User " + ((User) clientThread).getName() + " on " + clientThread.getSocketClient() + " : " + exception);
    }

    @Override
    public synchronized void onStop_ClientThread(ClientThread clientThread) {
        log("info", "onStop : User " + ((User) clientThread).getName() + " on " + clientThread.getSocketClient());
        listThreads.remove(clientThread);
    }

    // ============================================================================================

    @Override
    public synchronized void onStart(Transmitter transmitter, Direction direction, String message) {
        log("info", message);
    }

    @Override
    public synchronized void onProgress(Transmitter transmitter, Direction direction, String treadName, long bytes) {

    }

    @Override
    public synchronized void onException(Transmitter transmitter, Exception exception) {

    }

    @Override
    public synchronized void onStop(Transmitter transmitter, Direction direction, String message, ClientThread clientThread, String userFolderUUID, String path) {
        if (direction == Direction.RECEIVE)
            sendFolderContent(clientThread, userFolderUUID, Paths.get(path, "..").normalize().toAbsolutePath().toString());
        log("info", message);
    }

    // ============================================================================================

    /**
     * Обработчик JSON объектов
     *
     * @param clientThread ClientThread
     * @param json         JSONObject
     */
    private void handlerJSONObjects(ClientThread clientThread, JSONObject json) {
        /* Общие */
        String userFolderUUID = database.getUuid(((User) clientThread).getNickname());

        /* JSON */
        //todo ОБРАБОТКА: "request": "authentication"
        if (json.getString("request").equals("authentication")) {
            JSONObject data = json.getJSONObject("data");
            String login = data.getString("login").toLowerCase();
            String password = data.getString("password");

            User user = (User) clientThread;
            String nicknameDatabase = database.getNickname(login, password);

            /* Если пользователь не найден в базе, то... */
            if (nicknameDatabase == null) {
                /*... отправить уведомление клиенту. */
                clientThread.post(getAlert("Database", "Authorization", "Пользователь не найден в БД."));
                return;
            }

            /* Отключить пользователя, если он переподключается из другого приложения */
            User alreadyConnected = getAuthorizedByNickname(nicknameDatabase);
            if (alreadyConnected != null) {
                alreadyConnected.inauthorize();
                user.authorize(nicknameDatabase);
            } else user.authorize(nicknameDatabase);
        }

        //todo ОБРАБОТКА: "request": "list"
        if (json.getString("request").equals("list")) {
            JSONObject data = json.getJSONObject("data");
            String path = data.getString("path");
            //
            sendFolderContent(clientThread, userFolderUUID, path);
        }

        //todo ОБРАБОТКА: "request": "registration"
        if (json.getString("request").equals("registration")) {
            JSONObject data = json.getJSONObject("data");
            String login = data.getString("login");
            String password = data.getString("password");
            String nickname = data.getString("nickname");
            String email = data.getString("email");
            //
            if (database.isExistLogin(login)) {
                clientThread.post(getAlert("Database", "Registration", "The user is already registered in the database."));
                log("info", "User '" + login + "' is already registered in the database.");
            } else {
                if (database.createUser(login, password, nickname, email)) {
                    Path newFolder = Paths.get(getStorageFolder(), database.getUuid(login)).normalize();
                    if (makeDirectory(newFolder))
                        log("info", "Личный каталог '" + newFolder + "' для пользователя '" + nickname + "' зарегистрирован.");
                    //
                    //todo ОТПРАВКА: "response": "registred"
                    JSONObject response = new JSONObject();
                    response.put("response", "registred");
                    JSONObject dataReg = new JSONObject();
                    dataReg.put("message", "Пользователь '" + nickname + "' зарегистрирован.");
                    response.put("data", dataReg);
                    clientThread.post(response);
                    //
                    log("info", "User '" + login + "' registred.");
                } else {
                    clientThread.post(getAlert("Database", "Registration", "Registration error.\nUser not found in database."));
                    log("info", "User '" + login + "' not registred.");
                }
            }
        }

        //todo ОБРАБОТКА: "request": "download"
        if (json.getString("request").equals("download")) {
            JSONObject data = json.getJSONObject("data");
            String filename = data.getString("filename");
            JSONObject socket = data.getJSONObject("socket");
            String hostname = socket.getString("hostname");
            int port = socket.getInt("port");

            /* Активация отправителя */
            Transmitter sender = new Transmitter(this);
            sender.initServer(clientThread, null, null);
            String filenameToSend = Paths.get(getStorageFolder(),
                    userFolderUUID,
                    filename).toString();
            sender.post(filenameToSend, hostname, port);
        }

        //todo ОБРАБОТКА: "request": "upload"
        if (json.getString("request").equals("upload")) {
            JSONObject data = json.getJSONObject("data");
            String uploadFilename = data.getString("filename");

            /* Активация получателя */
            Transmitter receiver = new Transmitter(this);
            receiver.initServer(clientThread, userFolderUUID, uploadFilename);
            receiver.get(Paths.get(getStorageFolder(), userFolderUUID, uploadFilename).toString());
            String storageName = receiver.getReceiverExternalIP();
            int port = receiver.getReceiverPort();

            /* Подготовка данных для ответа готовности к приему */
            //todo ОТПРАВКА: "response": "ready-upload"
            JSONObject response = new JSONObject();
            response.put("response", "ready-upload");
            JSONObject dataR = new JSONObject();
            JSONObject socket = new JSONObject();
            socket.put("key", uploadFilename);
            socket.put("hostname", storageName);
            socket.put("port", port);
            dataR.put("socket", socket);
            response.put("data", dataR);
            clientThread.post(response);
        }

        //todo ОБРАБОТКА: "request": "create-folder"
        if (json.getString("request").equals("create-folder")) {
            JSONObject data = json.getJSONObject("data");
            String newFolder = data.getString("new-folder");
            if (makeDirectory(Paths.get(getStorageFolder(), userFolderUUID, newFolder))) {
                log("info", "Новый каталог '" + newFolder + "' пользователя '" + ((User) clientThread).getNickname() + "' создан.");
                sendFolderContent(clientThread, userFolderUUID, Paths.get(newFolder, "..").normalize().toString());
            } else clientThread.post(getAlert("Create folder", "Не могу создать папку", newFolder));
        }

        //todo ОБРАБОТКА: "request": "delete"
        if (json.getString("request").equals("delete")) {
            JSONObject data = json.getJSONObject("data");
            String path = data.getString("path");
            if (delete(Paths.get(getStorageFolder(), userFolderUUID, path))) {
                log("info", "Удален путь '" + path + "' у пользователя '" + ((User) clientThread).getNickname() + "' в хранилище.");
                sendFolderContent(clientThread, userFolderUUID, Paths.get(path, "..").normalize().toString());
            } else clientThread.post(getAlert("Delete", "Не могу удалить", path));
        }

        //todo ОБРАБОТКА: "request": "rename"
        if (json.getString("request").equals("rename")) {
            JSONObject data = json.getJSONObject("data");
            String oldPath = data.getString("old-path");
            String newPath = data.getString("new-path");
            //
            Path oldAbsolutePath = Paths.get(getStorageFolder(), userFolderUUID, oldPath);
            Path newAbsolutePath = Paths.get(getStorageFolder(), userFolderUUID, newPath);
            if (rename(oldAbsolutePath, newAbsolutePath))
                sendFolderContent(clientThread, userFolderUUID, Paths.get(oldPath, "..").normalize().toString());
            else clientThread.post(getAlert("Rename", "Не могу переименовать", oldPath));
        }

        // todo end
    }

    /**
     * Отправляет содержимое папки
     *
     * @param thread         Thread
     * @param userFolderUUID String
     * @param path           String
     */
    private void sendFolderContent(Thread thread, String userFolderUUID, String path) {
        /* Подготовка данных */
        Path pathStorage = Paths.get(getStorageFolder(),
                userFolderUUID,
                path);
        JSONObject list = getListDir(pathStorage);

        //todo ОТПРАВКА: "response": "list"
        JSONObject response = new JSONObject();
        response.put("response", "list");
        response.put("data", list);
        ((ClientThread) thread).post(response);
    }

    /**
     * Возвращает JSON-объект содержащие allert-сообщение
     *
     * @param title   String
     * @param header  String
     * @param content String
     * @return JSONObject
     */
    private JSONObject getAlert(String title, String header, String content) {
        //todo ОТПРАВКА: "response": "alert"
        JSONObject response = new JSONObject();
        response.put("response", "alert");
        JSONObject data = new JSONObject();
        data.put("title", title);
        data.put("header", header);
        data.put("content", content);
        response.put("data", data);
        return response;
    }

    /**
     * @param nickname String
     * @return User
     */
    private User getAuthorizedByNickname(String nickname) {
        /* Пройтись по всем клиентским потокам и найти в них авторизованного пользователя */
        for (ClientThread thread : listThreads) {
            User user = (User) thread;
            if (!user.isAuthorized()) continue;
            if (user.getNickname().equalsIgnoreCase(nickname)) return user;
        }
        return null;
    }

    /* Содержимое каталога */
    private JSONObject getListDir(Path directory) {
        JSONObject data = new JSONObject();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directory)) {
            JSONArray name = new JSONArray();
            JSONArray folder = new JSONArray();
            JSONArray size = new JSONArray();
            JSONArray date = new JSONArray();
            for (Path entry : paths) {
                BasicFileAttributes attributes = Files.readAttributes(entry, BasicFileAttributes.class);
                String aName = entry.getFileName().toString();
                String aType = attributes.isDirectory() ? "dir" : "file";
                String aSize = aType.equals("dir") ? "" : String.valueOf(attributes.size());
                String aDate = Dater.longDateToString(entry.toFile().lastModified());
                /* flush */
                name.put(aName);
                folder.put(aType);
                size.put(aSize);
                date.put(aDate);
            }
            data.put("name", name);
            data.put("type", folder);
            data.put("size", size);
            data.put("date", date);
        } catch (NoSuchFileException e) {
            log("error", "На сервере не найдена папка пользователя " + directory);
            return null;
        } catch (IOException | DirectoryIteratorException e) {
            e.printStackTrace();
        }
        return data;
    }

    /* Создать каталог */
    private boolean makeDirectory(Path directory) {
        try {
            Files.createDirectories(directory);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Удалить каталог с содержимым или файл */
    private boolean delete(Path path) {
        try {
            Files.walk(path, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(fileOrDir -> {
                        boolean result = fileOrDir.delete();
                    });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Переименование */
    private boolean rename(Path oldAbsolutePath, Path newAbsolutePath) {
        try {
            Files.move(oldAbsolutePath, newAbsolutePath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log("error", "Не могу переименовать: " + oldAbsolutePath);
            e.printStackTrace();
        }
        return false;
    }
}