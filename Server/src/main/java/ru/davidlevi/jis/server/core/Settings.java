package ru.davidlevi.jis.server.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import ru.davidlevi.jis.common.utils.FOS;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Settings {
    private static final Path FILE_SETTINGS = Paths.get("server_settings.json");

    private static Logger logger;
    private StringBuilder builder;

    {
        logger = LogManager.getLogger(Settings.class);
        builder = new StringBuilder("\n");
        builder.append("  i i i i i i i\n");
        builder.append("  I I I I I I I\n");
        builder.append("  I  \\ `+' /  I\n");
        builder.append("   \\  `-+-'  /\n");
        builder.append("    `-__|__-'\n");
        builder.append("        |\n");
        builder.append("  ------+------");
    }

    private String hostname;
    private int port;
    private int backlog; // maximum length of the queue of incoming connections to this server
    private String storageFolder;

    Settings() {
        logger.info(builder.toString());
        if (!FOS.isExistFile(FILE_SETTINGS)) {
            JSONObject root = new JSONObject();
            JSONObject server = new JSONObject();
            server.put("hostname", "capricorn");
            server.put("port", 8189);
            server.put("backlog", 100);
            server.put("storage_folder", Paths.get("/store/david"));
            root.put("server", server);
            FOS.save(root.toString(), FILE_SETTINGS);
            logger.info("Произведена инициализация файла настроек сервера.");
        }
    }

    void loadServerSettings() {
        JSONObject root = new JSONObject(FOS.read(FILE_SETTINGS));
        hostname = root.getJSONObject("server").getString("hostname");
        port = root.getJSONObject("server").getInt("port");
        backlog = root.getJSONObject("server").getInt("backlog");
        storageFolder = root.getJSONObject("server").getString("storage_folder");
        logger.info("Настройки сервера загружены.");
    }

    public void saveServerSettings() {
        JSONObject root = new JSONObject();
        JSONObject server = new JSONObject();
        server.put("hostname", hostname);
        server.put("port", port);
        server.put("backlog", backlog);
        server.put("storage_folder", Paths.get(storageFolder));
        root.put("server", server);
        FOS.save(root.toString(), FILE_SETTINGS);
        logger.info("Настройки сервера сохранены.");
    }

    String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStorageFolder() {
        return storageFolder;
    }

    public void setStorageFolder(String storageFolder) {
        this.storageFolder = storageFolder;
    }

    int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }
}
