package ru.davidlevi.jis.common.notuse;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Класс Dater обрабатывает дату
 */
public class Dater {
    private static final String DD_MM_YYYY_H_M_S = "dd.MM.yyyy H:m:s";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DD_MM_YYYY_H_M_S);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DD_MM_YYYY_H_M_S);

    /* Возвращает строку из LocalDate */
    public static String localDateToString(LocalDate date) {
        if (date == null) return null;
        return FORMATTER.format(date);
    }

    /* Возвращает строку из LocalDate */
    public static String longDateToString(Long date) {
        if (date == null) return null;
        return DATE_FORMAT.format(date);
    }

    /* Возвращает LocalDate из строки */
    public static LocalDate stringToLocalDate(String date) {
        try {
            return FORMATTER.parse(date, string -> LocalDate.from(string));
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /* Получился ли преобразовать строку в LocalDate */
    public static boolean testDate(String date) {
        return Dater.stringToLocalDate(date) != null;
    }
}
