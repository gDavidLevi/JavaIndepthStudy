package ru.davidlevi.jis.server.database.interfaces;

public interface InterfaceDatabase {
    void connect();

    boolean createUser(String login, String password, String nickname, String email);

    /* Именнованный запрос из файла UsersEntity.hbm.xml */
    String getNickname(String login, String password);

    /* Именнованный запрос из файла UsersEntity.hbm.xml */
    String getUuid(String nickname);

    /* Именнованный запрос из файла UsersEntity.hbm.xml */
    boolean isExistLogin(String login);

    void disconnect();
}