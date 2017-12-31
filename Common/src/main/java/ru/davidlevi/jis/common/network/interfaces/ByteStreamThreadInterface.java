package ru.davidlevi.jis.common.network.interfaces;

public interface ByteStreamThreadInterface {
    void get(String filename);
    void post(String filename, String hostname, int sendPort);
    int getReceiverPort();
    String getReceiverExternalIP();
}
