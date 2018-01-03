package ru.davidlevi.jis.common.network.interfaces;

import ru.davidlevi.jis.common.network.ClientThread;
import ru.davidlevi.jis.common.network.Direction;
import ru.davidlevi.jis.common.network.FileTransferThread;

/**
 * Слушатель ByteStreamThreadListener
 * Слушает события класса FileTransferThread
 *
 * @see FileTransferThread
 */
public interface ByteStreamThreadListener {
    /**
     * Событие "Нить стартовала"
     *
     * @param fileTransferThread FileTransferThread
     * @param direction        Direction
     * @param message          String
     */
    void onStart(FileTransferThread fileTransferThread, Direction direction, String message);

    /**
     * Событие "Передано информации в байтах"
     *
     * @param fileTransferThread FileTransferThread
     * @param direction        Direction
     * @param treadName        String
     * @param bytes            long
     */
    void onProgress(FileTransferThread fileTransferThread, Direction direction, String treadName, long bytes);

    /**
     * Событие "Возникла исключительная ситуация"
     *
     * @param fileTransferThread FileTransferThread
     * @param exception        Exception
     */
    void onException(FileTransferThread fileTransferThread, Exception exception);

    /**
     * Событие "Нить завершила работу"
     *
     * @param fileTransferThread FileTransferThread
     * @param direction        Direction
     * @param message          String
     * @param clientThread     ClientThread
     * @param userFolderUUID   String
     * @param path             String
     */
    void onStop(FileTransferThread fileTransferThread, Direction direction, String message, ClientThread clientThread, String userFolderUUID, String path);
}
