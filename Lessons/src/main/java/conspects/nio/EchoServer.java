package conspects.nio;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Класс EchoServer
 */
public class EchoServer {
    private static final String POISON_PILL = "POISON_PILL";

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();  // открыть селектор
        ServerSocketChannel serverSocket = ServerSocketChannel.open();  // открыть соккет
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false);  // неблокирующий режим чтения/записи
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);  // селектор будет принимать подключения
        ByteBuffer buffer = ByteBuffer.allocate(256);  // размер буфера 256 байт

        // Основной цикл
        while (true) {
            selector.select();  // режим "выборки"
            Set<SelectionKey> selectedKeys = selector.selectedKeys();  // ключи
            Iterator<SelectionKey> iterator = selectedKeys.iterator();  // переборщик ключей
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next(); // взяли ключ
                if (key.isAcceptable())
                    register(selector, serverSocket);  // зарегистрировать, если канал принял соккет-подключение
                if (key.isReadable())
                    answerWithEcho(buffer, key);  // если канал доступен для чтения, отправить эхо-ответ
                iterator.remove();
            }
        }
    }

    /**
     * Эхо-ответ
     *
     * @param buffer ByteBuffer
     * @param key    SelectionKey
     * @throws IOException Exception
     */
    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();  // по ключу вернуть соккет-клиента
        client.read(buffer);  // прочитать данные от клиента в буфер
        if (new String(buffer.array()).trim().equals(POISON_PILL)) { // если пришло сообщение POISON_PILL, то закрыть соккет клиента
            client.close();
            System.out.println("Not accepting client messages anymore");
        }
        buffer.flip();  // теперь будем писать в буффер
        client.write(buffer); // отправим содержимое буфера клиенту как эхо-ответ
        buffer.clear();
    }

    /**
     * Регистрация подключения
     *
     * @param selector     Selector
     * @param serverSocket ServerSocketChannel
     * @throws IOException Exception
     */
    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();  // принять подключение и вернуть соккет-канал клиента
        client.configureBlocking(false);  // не блокировать канал
        client.register(selector, SelectionKey.OP_READ);  // зарегистрировать клиента и перейти в режим READ
    }

    /**
     * Запуск
     *
     * @return Process
     * @throws IOException          Exception
     * @throws InterruptedException Exception
     */
    public static Process start() throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = EchoServer.class.getCanonicalName();
        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
        return builder.start();
    }
}
