package ru.davidlevi.jis.client.core;

import org.json.JSONObject;
import ru.davidlevi.jis.common.utils.FOS;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс Settings
 * Доступ к настройкам для клиентского приложения
 */
class Settings {
    private static final Path FILE_SETTINGS = Paths.get("сlient_settings.json");

    /* Поля заполняемые из файла */
    private String hostname;
    private int port;

    Settings() {
        if (!FOS.isExistFile(FILE_SETTINGS)) {
            /* Создает файл с настройками, если он не найден */
            JSONObject root = new JSONObject();
            JSONObject client = new JSONObject();
            client.put("hostname", "capricorn");
            client.put("port", 8189);
            root.put("client", client);
            FOS.save(root.toString(), FILE_SETTINGS);
        }
    }

    /**
     * Метод загружает настроки из файла FILE_SETTINGS
     */
    void loadClientSettings() {
        JSONObject root = new JSONObject(FOS.read(FILE_SETTINGS));
        hostname = root.getJSONObject("client").getString("hostname");
        port = root.getJSONObject("client").getInt("port");
    }

    /**
     * Метод сохраняет настроки в файл FILE_SETTINGS
     */
    public void saveSettings() {
        JSONObject root = new JSONObject();
        JSONObject client = new JSONObject();
        client.put("hostname", hostname);
        client.put("port", port);
        root.put("client", client);
        FOS.save(root.toString(), FILE_SETTINGS);
    }

    String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
