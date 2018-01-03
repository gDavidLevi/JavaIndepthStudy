package ru.davidlevi.jis.common.network;

import org.json.JSONObject;
import ru.davidlevi.jis.common.network.interfaces.ClientThreadListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {
    private final ClientThreadListener listener;
    private final Socket socketClient;
    private DataOutputStream out;

    public ClientThread(ClientThreadListener listener,
                        String name,
                        Socket socketClient) {
        super(name);
        this.listener = listener;
        this.socketClient = socketClient;
        start();
    }

    /* Runnable */
    @Override
    public void run() {
        listener.onStart_ClientThread(this);
        try {
            DataInputStream in = new DataInputStream(socketClient.getInputStream());
            out = new DataOutputStream(socketClient.getOutputStream());
            listener.onReady_ClientThread(this, socketClient);
            while (!isInterrupted()) {
                /* Получили объект */
                JSONObject get = new JSONObject(in.readUTF());
                listener.onGet_ClientThread(this, socketClient, get);
            }
        } catch (IOException exception) {
            listener.onException_ClientThread(this, socketClient, exception);
        } finally {
            try {
                socketClient.close();
            } catch (IOException exception) {
                listener.onException_ClientThread(this, socketClient, exception);
            }
            listener.onStop_ClientThread(this);
        }
    }

    /**
     * Метод отправляет объект
     * @param jsonObject JSONObject
     */
    public synchronized void post(JSONObject jsonObject) {
        try {
            /* Отправили объект */
            out.writeUTF(jsonObject.toString());
            out.flush();
            listener.onPost_ClientThread(this, socketClient, jsonObject);
        } catch (IOException exception) {
            listener.onException_ClientThread(this, socketClient, exception);
            close();
        }
    }

    public synchronized void close() {
        interrupt();
        try {
            socketClient.close();
        } catch (IOException exception) {
            listener.onException_ClientThread(this, socketClient, exception);
        }
    }

    public synchronized Socket getSocketClient() {
        return socketClient;
    }

}