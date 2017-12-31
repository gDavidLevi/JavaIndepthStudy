package ru.davidlevi.jis.common.network;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.davidlevi.jis.common.network.interfaces.ByteStreamThreadInterface;
import ru.davidlevi.jis.common.network.interfaces.ByteStreamThreadListener;

import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * Класс позволяет передавать большие потоки данных из-за применения Base64
 */
public class Transmitter implements ByteStreamThreadInterface {
    /* Константы */
    private static final int SOCKET_TIMEOUT_MS = 2000;
    private static final int MAXIMUM_NUMBER_OF_PENDING_CONNECTIONS = 1;
    private static final int DEFAULT_BUFFER_SIZE = 1024; // 1Mb
    private static final int PORT_MIN = 45535;
    private static final int PORT_MAX = 65535;

    /* Runtime */
    private Logger logger;
    private final ByteStreamThreadListener listener;
    private int receivePort;
    private ClientThread clientThread;
    private String userFolderUUID;
    private String path;

    {
        logger = LogManager.getLogger(Transmitter.class);
    }

    /**
     * Конструктор
     *
     * @param listener Получатель событий
     */
    public Transmitter(ByteStreamThreadListener listener) {
        this.listener = listener;
        generatePort();
    }

    /**
     * Инициализатор для клиента для получения и отправки.
     *
     * @param clientThread Поток-получателя на стороне клиента
     */
    public void initClient(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    /**
     * Инициализатор для сервера для получения и отправки.
     * <p>
     * Для отправки (post) параметры userFolderUUID и uploadFilename задать null.
     * Для получения (get) параметры userFolderUUID и uploadFilename обязательный к заполнению.
     *
     * @param clientThread   Поток-получателя на стороне сервера
     * @param userFolderUUID Каталог пользователя на сервере
     * @param uploadFilename Загружаемый файл ("полный" путь к файлу относительно userFolderUUID)
     */
    public void initServer(ClientThread clientThread, String userFolderUUID, String uploadFilename) {
        this.clientThread = clientThread;
        this.userFolderUUID = userFolderUUID;
        this.path = uploadFilename;
    }

    /**
     * Выбирает случайный порт из диапазона
     */
    private void generatePort() {
        Random random = new Random();
        this.receivePort = PORT_MIN + random.nextInt((PORT_MAX + 1) - PORT_MIN);
    }

    /* Копирование потока */
    private /*synchronized*/ void copyStream(InputStream in, OutputStream out, Direction direction) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        long counter = 0;
        while ((length = in.read(buffer)) != -1) {
            counter += length;
            listener.onProgress(this, direction, Thread.currentThread().getName(), counter);
            out.write(buffer, 0, length);
            out.flush();
        }
        Thread.currentThread().interrupt();
    }

    /**
     * Получение потока данных
     *
     * @param filename полное имя файла для записи на диск
     */
    public void get(String filename) {
        final File receiveFile = new File(filename);
        String message = String.format("Receive file '%s' from '%s'.", receiveFile.getName(), clientThread.getName());
        logger.info(message);
        //
        Runnable runnable = () -> {
            listener.onStart(Transmitter.this, Direction.RECEIVE, "onStart : " + message);
            try (ServerSocket serverSocket = new ServerSocket(receivePort, MAXIMUM_NUMBER_OF_PENDING_CONNECTIONS)) {
                serverSocket.setSoTimeout(SOCKET_TIMEOUT_MS);
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket;
                    try {
                        socket = serverSocket.accept();
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    try (Base64InputStream in = new Base64InputStream(socket.getInputStream(), false);
                         FileOutputStream out = new FileOutputStream(receiveFile)) {
                        Transmitter.this.copyStream(in, out, Direction.RECEIVE);
                    } catch (IOException exception) {
                        listener.onException(Transmitter.this, exception);
                    }
                }
            } catch (IOException exception) {
                listener.onException(Transmitter.this, exception);
            }
            listener.onStop(Transmitter.this, Direction.RECEIVE, "onStop : " + message, clientThread, userFolderUUID, path);
        };
        Thread receiver = new Thread(runnable);
        receiver.setName(receiveFile.getName());
        receiver.start();
    }

    /**
     * Отправка потока данных
     *
     * @param filename полное имя фала на диске для отправки
     * @param hostname удаленный хост
     * @param sendPort порт
     */
    public void post(String filename, String hostname, int sendPort) {
        final File sendFile = new File(filename);
        String message = String.format("Send file '%s' to '%s' on %s:%d.", sendFile.getName(), clientThread.getName(), hostname, sendPort);
        logger.info(message);
        if (sendPort < PORT_MIN) throw new RuntimeException("Invalid receivePort!");
        //
        Runnable runnable = () -> {
            listener.onStart(Transmitter.this, Direction.SEND, "onStart : " + message);
            try (Socket socket = new Socket(Inet4Address.getByName(hostname), sendPort)) {
                while (!Thread.currentThread().isInterrupted()) {
                    try (FileInputStream in = new FileInputStream(sendFile);
                         Base64OutputStream out = new Base64OutputStream(socket.getOutputStream(), true)) {
                        Transmitter.this.copyStream(in, out, Direction.SEND);
                    } catch (IOException exception) {
                        listener.onException(Transmitter.this, exception);
                    }
                }
            } catch (IOException exception) {
                listener.onException(Transmitter.this, exception);
            }
            listener.onStop(Transmitter.this, Direction.SEND, "onStop : " + message, clientThread, userFolderUUID, path);
        };
        Thread sender = new Thread(runnable);
        sender.setName(sendFile.getName());
        sender.start();
    }

    public int getReceiverPort() {
        return receivePort;
    }

    /**
     * Возвращает внешний IP-адрес клиента
     *
     * @return String
     */
    @Override
    public String getReceiverExternalIP() {
        String result = null;
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName(/*Google Public DNS*/"8.8.8.8"), 10002);
            result = datagramSocket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return result;
    }
}
