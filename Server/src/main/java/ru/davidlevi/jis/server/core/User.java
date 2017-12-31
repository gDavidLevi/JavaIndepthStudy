package ru.davidlevi.jis.server.core;

import org.json.JSONObject;
import ru.davidlevi.jis.common.network.ClientThread;
import ru.davidlevi.jis.common.network.interfaces.ClientThreadListener;

import java.net.Socket;

public class User extends ClientThread {
    private boolean isAuthorized;
    private String nickname;

    {
        isAuthorized = false;
        nickname = "";
    }

    User(ClientThreadListener clientThreadListener, Socket socketClient) {
        super(clientThreadListener, "ClientThread", socketClient);
    }

    /* Авторизовать пользователя */
    public void authorize(String nickname) {
        this.isAuthorized = true;
        this.nickname = nickname;
        //
        //todo ОТПРАВКА: "response": "authorization"
        JSONObject response = new JSONObject();
        response.put("response", "authorization");
        JSONObject data = new JSONObject();
        data.put("nickname", nickname);
        response.put("data", data);
        post(response);
        //
        setName(nickname);
    }

    /* Деавторизовать пользователя */
    public void inauthorize() {
        //todo ОТПРАВКА: "response": "inauthorize"
        JSONObject response = new JSONObject();
        response.put("response", "inauthorize");
        post(response);
        close();
    }

    /**
     * Пользователь авторизован?
     *
     * @return boolean
     */
    public boolean isAuthorized() {
        return this.isAuthorized;
    }

    public String getNickname() {
        return this.nickname;
    }

}