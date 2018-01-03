package ru.davidlevi.jis.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

/**
 * File Operating System
 * Общие процедуры
 */
public class FOS {
    /* Чтение файла в строку */
    public static String read(Path fileName) {
        BufferedReader reader;
        StringBuilder builder = null;
        try {
            reader = newBufferedReader(fileName, StandardCharsets.UTF_8);
            builder = new StringBuilder();
            String string;
            while ((string = reader.readLine()) != null) builder.append(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert builder != null;
        return builder.toString();
    }

    /* Запись строки в файл */
    public static void save(String content, Path fileName) {
        BufferedWriter writer;
        try {
            writer = newBufferedWriter(fileName, StandardCharsets.UTF_8);
            writer.write(content, 0, content.length());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isExistFile(Path fileSettings) {
        try (InputStream stream = Files.newInputStream(fileSettings, StandardOpenOption.READ)) {
            stream.read();
            return true;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return false;
    }
}
