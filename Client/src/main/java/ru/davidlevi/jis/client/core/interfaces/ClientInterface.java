package ru.davidlevi.jis.client.core.interfaces;

import ru.davidlevi.jis.client.FxContext;

import java.nio.file.Path;

/**
 * Интерфейс ClientInterface
 * Осуществляет взаимодействие графической частью JavaFX-интерфейса с классом Client
 *
 * @see ru.davidlevi.jis.client.core.Client
 */
public interface ClientInterface {
    /**
     * Метод сохраняет ссылку на главный FX-контекст
     *
     * @param fxContext FxContext
     */
    void setFxContext(FxContext fxContext);

    /* Запрос на аутентификацию */
    void authenticationRequest(String login, String password);

    /**
     * Отправка запроса на регистрацию
     *
     * @param login    String
     * @param password String
     * @param nickname String
     * @param email    String
     */
    void registrationRequest(String login, String password, String nickname, String email);

    /**
     * Запросить получение содержимого каталога на сервере и войти в него
     *
     * @param cd String
     */
    void changeDir(String cd);

    /**
     * Создание каталога на сервере
     *
     * @param newFolder String
     */
    void createFolder(String newFolder);

    /**
     * Получение файла с сервера
     *
     * @param remoteFile с сервера
     * @param localFile  в локальный файл
     */
    void receiveFile(String remoteFile, String localFile);

    /**
     * Отправка файла на сервер
     *
     * @param remoteFilename String
     */
    void sendFileRequest(String remoteFilename);

    /**
     * Завершение работы клиента.
     * Закрытие соккета и завершение нити.
     */
    void quit();

    /**
     * Удаляет папку с сдержимым или файл
     *
     * @param path Path
     */
    void delete(Path path);

    /**
     * Переименовывает путь (файла или папки; имя файла)
     *
     * @param oldName Path
     * @param newName Path
     */
    void rename(Path oldName, Path newName);
}
