package ru.davidlevi.jis.server.network;

import ru.davidlevi.jis.server.network.interfaces.ServerThreadListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerThread extends Thread {
    private final ServerThreadListener listener;
    private final int port;
    private final int timeout;
    private final int backlog;
    private final String hostnameServer;

    public ServerThread(ServerThreadListener listener,
                        String name,
                        int port,
                        int backlog,
                        String hostname,
                        int timeout) {
        super(name);
        this.listener = listener;
        this.port = port;
        this.timeout = timeout;
        this.backlog = backlog;
        this.hostnameServer = hostname;
        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        listener.onStart_ServerThread(this);
        try (ServerSocket serverSocket = new ServerSocket(port, backlog, InetAddress.getByName(hostnameServer))) {
            serverSocket.setSoTimeout(timeout);
            listener.onReady_ServerThread(this, serverSocket);
            while (!isInterrupted()) {
                Socket acceptedClientSocket;
                try {
                    acceptedClientSocket = serverSocket.accept();
                } catch (SocketTimeoutException exception) {
                    listener.onTimeOutAccept_ServerThread(this, serverSocket);
                    continue;
                }
                listener.onAccepted_ServerThread(this, serverSocket, acceptedClientSocket);
            }
        } catch (IOException exception) {
            listener.onException_ServerThread(this, exception);
        } finally {
            listener.onStop_ServerThread(this);
        }
    }
}