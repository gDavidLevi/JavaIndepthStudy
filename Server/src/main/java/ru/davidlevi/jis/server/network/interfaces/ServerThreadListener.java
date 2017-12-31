package ru.davidlevi.jis.server.network.interfaces;

import ru.davidlevi.jis.server.network.ServerThread;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @see ru.davidlevi.jis.server.core.Server имплементирует интерфейс для обработки событий
 * @see ServerThread отправляет события
 */
public interface ServerThreadListener {
    void onStart_ServerThread(ServerThread serverThread);

    void onReady_ServerThread(ServerThread serverThread, ServerSocket serverSocket);

    void onTimeOutAccept_ServerThread(ServerThread serverThread, ServerSocket serverSocket);

    void onAccepted_ServerThread(ServerThread serverThread, ServerSocket serverSocket, Socket acceptedClientSocket);

    void onException_ServerThread(ServerThread serverThread, Exception e);

    void onStop_ServerThread(ServerThread serverThread);
}