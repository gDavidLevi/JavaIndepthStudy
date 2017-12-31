package ru.davidlevi.jis.common.network.interfaces;

import org.json.JSONObject;
import ru.davidlevi.jis.common.network.ClientThread;

import java.net.Socket;

/**
 * Слушатель ClientThreadListener
 * Слушает события возникающие в классе ClientThread
 *
 * @see ClientThread
 */
public interface ClientThreadListener {
    /**
     * Событие "Нить стартовала"
     *
     * @param clientThread ClientThread
     */
    void onStart_ClientThread(ClientThread clientThread);

    /**
     * Событие "Нить готова к обмену сообщениями"
     *
     * @param clientThread ClientThread
     * @param socketClient Socket
     */
    void onReady_ClientThread(ClientThread clientThread, Socket socketClient);

    /**
     * Событие "Нить получила сообщение"
     *
     * @param clientThread ClientThread
     * @param socketClient Socket
     * @param jsonObject   JSONObject
     */
    void onGet_ClientThread(ClientThread clientThread, Socket socketClient, JSONObject jsonObject);

    /**
     * Событие "Нить отправила сообщение"
     *
     * @param clientThread ClientThread
     * @param socketClient Socket
     * @param jsonObject   JSONObject
     */
    void onPost_ClientThread(ClientThread clientThread, Socket socketClient, JSONObject jsonObject);

    /**
     * Событие "Возникла исключительная ситуация"
     *
     * @param clientThread ClientThread
     * @param socketClient Socket
     * @param exception    Exception
     */
    void onException_ClientThread(ClientThread clientThread, Socket socketClient, Exception exception);

    /**
     * Событие "Нить завершила работу"
     *
     * @param clientThread ClientThread
     */
    void onStop_ClientThread(ClientThread clientThread);
}