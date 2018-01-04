package conspects.nio.help;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

public class BytesStream {
    private static final String PATH = "src/main/java/conspects/nio/";

    public static void main(String[] args) throws Exception {
        /**
         * ПЛОХО!
         * Так делать нельзя потому, что создается новый экземплар строки String с каждой иттерацией
         */
        {
            long startTime = System.currentTimeMillis();
            FileInputStream fileInputStream = new FileInputStream(PATH + "file.txt");
            String s = "";
            int x;
            while ((x = fileInputStream.read()) != -1) {
                s += (char) x; // z, zz, zzz, zzzz...
            }
            fileInputStream.close();
            System.out.println(String.valueOf(System.currentTimeMillis() - startTime) + " ms."); // Файл размером 91140 байт читается за 5654 ms.
        }

        /**
         * ХОРОШО!
         * Читаем файл кусочками
         */
        {
            long startTime = System.currentTimeMillis();
            // BufferedInputStream читает по DEFAULT_BUFFER_SIZE = 8192;
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(PATH + "file.txt"));
            StringBuilder stringBuilder = new StringBuilder();
            int xx;
            while ((xx = inputStream.read()) != -1) {
                stringBuilder.append((char) xx); // Правильное решение!
            }
            inputStream.close();
            System.out.println(String.valueOf(System.currentTimeMillis() - startTime) + " ms."); // Файл размером 91140 байт читается за 10 ms.
        }

        /**
         * ХОРОШО ОЧЕНЬ!
         * Используем байтовый буфер размером .available()
         */
        {
            long startTime = System.currentTimeMillis();
            FileInputStream fileInputStream = new FileInputStream(PATH + "file.txt");
            byte[] bytes = new byte[fileInputStream.available()]; // Создаем буфер из массива байт азмером .available()
            fileInputStream.read(bytes); // Очень быстрое чтение!
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                stringBuilder.append((char) b);
            }
            fileInputStream.close();
            System.out.println(String.valueOf(System.currentTimeMillis() - startTime) + " ms."); // Файл размером 91140 байт читается за 5 ms.
        }

        /**
         * Буферезованный исходящйи поток
         * Размер буфера по умолчанию = 8192 байт.
         */
        {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(PATH + "file_out.txt"));
            bufferedOutputStream.write(65);
            bufferedOutputStream.flush(); // слить в файл до переполнения буфера
            bufferedOutputStream.close();
        }

        /**
         * Байтовый поток. Массив.
         * Пишем/читаем из/в массив(а)
         */
        {
            byte[] bytes = {1, 2, 3, 4, 5, 6, 7};
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            int x;
            while ((x = byteArrayInputStream.read()) != -1) {
                System.out.print(x + " ");
            }
            System.out.println();
            byteArrayInputStream.close();
            //
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i = 0; i < 100; i++) {
                byteArrayOutputStream.write(i);
            }
            byteArrayOutputStream.close();
            System.out.println(Arrays.toString(byteArrayOutputStream.toByteArray()));
        }

        /**
         * SequenceInputStream "сшивает" несколько потоков в один, и мы работаем в итоге с одним потоком.
         */
        {
            ArrayList<FileInputStream> list = new ArrayList<>();
            list.add(new FileInputStream(PATH + "1.txt"));
            list.add(new FileInputStream(PATH + "2.txt"));
            Enumeration<FileInputStream> fileInputStreamEnumeration = Collections.enumeration(list);
            SequenceInputStream sequenceInputStream = new SequenceInputStream(fileInputStreamEnumeration);
            int x;
            while ((x = sequenceInputStream.read()) != -1) {
                System.out.print((char) x);
            }
        }

        /**
         * Данные Piped-потоки создаются парами!
         * Pipes - это каналы между потоками (Thread). Используются для взаимодействия потоков.
         * Например: запущены 2 потока A и B. Поток A создает PipedWriter, отправляет туда какие-либо данные (например char'ы).
         * Поток B считывает PipedReader'ом char'ы переданные потоком A.
         */
        {
            PipedInputStream pipedInputStream = new PipedInputStream();
            PipedOutputStream pipedOutputStream = new PipedOutputStream();
            pipedInputStream.connect(pipedOutputStream);
            pipedOutputStream.write(10);
            pipedOutputStream.write(20);
            System.out.println(pipedInputStream.read());
            System.out.println(pipedInputStream.read());
        }

        { // RandomAccessFile - читает байты; получение доступа к определенному месту в файле
            // "r" Открывает файл только по чтению. Запуск любых методов записи данных приведет к выбросу исключения IOException.
            // "rw" Открывает файл по чтению и записи. Если файл еще не создан, то осуществляется попытка создать его.
            // "rws" Открывает файл по чтению и записи подобно "rw", и также требует системе при каждом изменении содержимого файла или метаданных синхронно записывать эти изменения на основной носитель.
            // "rwd" Открывает файл по чтению и записи подобно "rws", но принуждает систему синхронно записывать изменения на основной носитель только при каждом изменении содержимого файла. Если изменяются метаданные, синхронная запись не осуществляется.
            RandomAccessFile randomAccessFile = new RandomAccessFile(PATH + "file.txt", "rw"); //ABCDEFG
            int position = 0; // позиця м=символа
            randomAccessFile.seek(position);
            System.out.println(randomAccessFile.read()); // A
            //
            randomAccessFile.write('Z');
            System.out.println(randomAccessFile.read()); // Z
            randomAccessFile.close();
        }

        /**
         * DataOutputStream - запись примитивных типов.
         * Типы: bool, int, float, long...
         * Поддерживает кодировку: UTF-8 в методах .writeUTF() и .readUTF()
         * Используется в сетевом обмене, см. сетевой чат
         */
        {
            long value = 99999990L;
            // DataOutputStream не буферезирован по умолчанию поэтому выходной поток оборачивают в буферизированный поток BufferedOutputStream!
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(PATH + "1.txt")));
            dataOutputStream.writeLong(value);
            //dataOutputStream.writeUTF("@");
            //dataOutputStream.writeBoolean(false);
            //dataOutputStream.writeFloat(0.2563f);
            dataOutputStream.flush();
            dataOutputStream.close();
            //
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(PATH + "1.txt"));
            System.out.println("long: " + dataInputStream.readLong()); // 99999990
            //System.out.println(dataInputStream.readUTF()); // см. сетевой чат
        }

        /**
         * Файловый исходящий поток байт
         */
        {
            FileOutputStream outputStream = new FileOutputStream(PATH + "file.txt");
            for (int i = 0; i < 5; i++) {
                outputStream.write(65 + i);  // пишем байты
            }
            outputStream.flush();
            outputStream.close();
        }

        /**
         * Файловый входящий поток байт
         */
        {
            FileInputStream inputStream = new FileInputStream(PATH + "file.txt");
            int x;
            while ((x = inputStream.read()) != -1) {
                System.out.print((char) x);  // читаем байты
            }
            inputStream.close();
        }

        /**
         * FilterInputStream.available() возвращает оценку количества байтов, которые могут быть прочитаны из этого
         * входного потока, без блокировки следующим вызовом метода для этого входного потока.
         */
        {
            FilterInputStream filterInputStream = new BufferedInputStream(new FileInputStream(PATH + "test.txt"));
            int one = 0;
            char oneChar;
            int availableBytesNow = 0;
            while ((one = filterInputStream.read()) != -1) {
                oneChar = (char) one;
                System.out.print("Прочитано: " + oneChar);
                /* Доступно число байт на данный момент */
                availableBytesNow = filterInputStream.available();
                System.out.println("; Доступно байт сейчас: " + availableBytesNow);
            }
        }
    }
}