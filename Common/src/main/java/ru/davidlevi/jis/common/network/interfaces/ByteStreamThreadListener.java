package ru.davidlevi.jis.common.network.interfaces;

import ru.davidlevi.jis.common.network.Transmitter;
import ru.davidlevi.jis.common.network.ClientThread;
import ru.davidlevi.jis.common.network.Direction;

/**
 * Слушатель ByteStreamThreadListener
 * Слушает события класса Transmitter
 *
 * @see Transmitter
 */
public interface ByteStreamThreadListener {
    /**
     * Событие "Нить стартовала"
     *
     * @param transmitter Transmitter
     * @param direction        Direction
     * @param message          String
     */
    void onStart(Transmitter transmitter, Direction direction, String message);

    /**
     * Событие "Передано информации в байтах"
     *
     * @param transmitter Transmitter
     * @param direction        Direction
     * @param treadName        String
     * @param bytes            long
     */
    void onProgress(Transmitter transmitter, Direction direction, String treadName, long bytes);

    /**
     * Событие "Возникла исключительная ситуация"
     *
     * @param transmitter Transmitter
     * @param exception        Exception
     */
    void onException(Transmitter transmitter, Exception exception);

    /**
     * Событие "Нить завершила работу"
     *
     * @param transmitter Transmitter
     * @param direction        Direction
     * @param message          String
     * @param clientThread     ClientThread
     * @param userFolderUUID   String
     * @param path             String
     */
    void onStop(Transmitter transmitter, Direction direction, String message, ClientThread clientThread, String userFolderUUID, String path);
}
