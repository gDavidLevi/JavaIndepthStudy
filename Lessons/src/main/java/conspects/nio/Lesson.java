package conspects.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Set;

public class Lesson {
    public static void main(String[] args) throws IOException {
        final String rootPath = "./Lessons/src/main/java/conspects/nio/folder/";

        // Пример 1
        System.out.println("1. Пути");
        Path path0 = Paths.get("/home/david/folder/file.txt");
        System.out.println(path0.getName(0));  // home
        System.out.println(path0.getName(1));  // david
        System.out.println(path0.getName(path0.getNameCount() - 1));   // file.txt
        System.out.println(path0.relativize(Paths.get("/home")));  // ../../..

        // Пример 2
        System.out.println("2. Содержимое каталога");
        Path path1 = Paths.get(rootPath);
        Files.list(path1)
                .forEach(System.out::println);

        // Пример 3
        System.out.println("3. Пройтись по дереву");
        Files.walkFileTree(Paths.get(rootPath), new FileVisitor<>() {
            @Override
            // Перед посещением дирректории
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            // Посетили файл, так что с ним сделать
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                System.out.println(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            // Не смогли найти файл/папку
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            // После посещения каталога
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });

        // Пример 4
        System.out.println("4. Копирование");
        Files.copy(Paths.get(rootPath, "1.txt"),
                Paths.get(rootPath, "4.txt"),
                StandardCopyOption.REPLACE_EXISTING);

        // Пример 5
        System.out.println("5. Читаем файл в консоль:");
        Path file = Paths.get(rootPath, "1.txt");
        Files.readAllLines(file).forEach(System.out::println);

        // Пример 6
        System.out.println("6. Копирование файла используя RandomAccessFile и FileChannel:");
        RandomAccessFile fromFile = new RandomAccessFile(rootPath + "from.txt", "rw");
        RandomAccessFile toFile = new RandomAccessFile(rootPath + "to.txt", "rw");
        FileChannel fromChannel = fromFile.getChannel();
        FileChannel toChannel = toFile.getChannel();
        long fileSize = fromChannel.size();
        fromChannel.transferTo(0, fileSize, toChannel);

        // Пример 7
        System.out.println("7. Чтение файла используя ByteBuffer, RandomAccessFile и FileChannel:");
        RandomAccessFile randomAccessFile = new RandomAccessFile(rootPath + "from.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4);  // 4 байта
        int bytesCount = channel.read(buffer);  // сколько информации в буфере
        while (bytesCount != -1) { // пока не достигнут конец файла
            buffer.flip();  // изменяет состояние чтения/записи (переместить указать в начало)
            while (buffer.hasRemaining()) // пока в буфере имеется информацци
                System.out.print((char) buffer.get()); // выводить содержимое буфера
            buffer.clear();
            bytesCount = channel.read(buffer);
        }
        randomAccessFile.close();

        // Пример 8
        System.out.println("7. flip и ByteBuffer:");
        ByteBuffer byteBuffer = ByteBuffer.allocate(4); // выделить место под буфер размером 4 байта
        byteBuffer.put((byte) 10);
        byteBuffer.put((byte) 11);
        byteBuffer.put((byte) 12);
        byteBuffer.put((byte) 13);
        byteBuffer.flip(); // меняем состоние на чтение (переместить указать в начало)
        System.out.println(byteBuffer.get());  // 10
        System.out.println(byteBuffer.get());  // 11
        System.out.println(byteBuffer.get());  // 12
        System.out.println(byteBuffer.get());  // 13

        ByteBuffer byteBuffer1 = ByteBuffer.allocate(4);
        byteBuffer1.put((byte) 10);
        System.out.println(byteBuffer1.get());  // 0

        ByteBuffer byteBuffer2 = ByteBuffer.allocate(4);
        byteBuffer2.put((byte) 10);
        byteBuffer2.put((byte) 11);
        byteBuffer2.put((byte) 12);
        byteBuffer2.put((byte) 13);
        //System.out.println(byteBuffer2.get());  // Exception in thread "main" java.nio.BufferUnderflowException
        byteBuffer2.flip();
        System.out.println(byteBuffer2.get());

        ByteBuffer byteBuffer3 = ByteBuffer.allocate(4);
        byteBuffer3.put((byte) 1);
        byteBuffer3.put((byte) 2);
        byteBuffer3.put((byte) 3);
        byteBuffer3.put((byte) 4);
        byteBuffer3.flip();
        byteBuffer3.put((byte) 7);
        byteBuffer3.put((byte) 8);
        byteBuffer3.flip();
        System.out.println();
        System.out.println(byteBuffer3.get());  // 7
        System.out.println(byteBuffer3.get());  // 8
        //System.out.println(byteBuffer3.get());  // 0  - защита старых данных: Exception in thread "main" java.nio.BufferUnderflowException
        //System.out.println(byteBuffer3.get());  // 0  - защита старых данных: Exception in thread "main" java.nio.BufferUnderflowException
        System.out.println();

        // Чтобы получить к доступ к "защита старых данных" используют метод compact()
        byteBuffer3.compact();
        System.out.println(byteBuffer3.get());  // 7
        System.out.println(byteBuffer3.get());  // 8
        System.out.println(byteBuffer3.get());  // 3
        System.out.println(byteBuffer3.get());  // 4
        System.out.println();

        //
        byteBuffer3.rewind();
        System.out.println(byteBuffer3.get());  // 7
        System.out.println(byteBuffer3.get());  // 8
        System.out.println(byteBuffer3.get());  // 3
        System.out.println(byteBuffer3.get());  // 4
    }
}

/**
 * Класс EchoServer
 */
class EchoServer {
    private static final String POISON_PILL = "POISON_PILL";

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5454));
        serverSocket.configureBlocking(false); //неблокирующий режим чтения/записи
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(256);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                    answerWithEcho(buffer, key);
                }
                iter.remove();
            }
        }
    }

    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key)
            throws IOException {

        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        if (new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }

        buffer.flip();
        client.write(buffer);
        buffer.clear();
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket)
            throws IOException {

        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    public static Process start() throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = EchoServer.class.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);

        return builder.start();
    }
}