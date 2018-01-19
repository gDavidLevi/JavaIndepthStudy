package ru.davidlevi.jis.server.core.interfaces;

import ru.davidlevi.jis.server.gui.controller.Basic;

/**
 * Интерсейс управления сервером через GUI
 *
 * @see ru.davidlevi.jis.server.core.Server имплементирует интерфейс для обработки событий
 * @see Basic управляет интерфейсом
 */
public interface ServerInterface {
    /**
     * Запускаем сервер
     */
    void start();

    /**
     * Останавливаем сервер
     */
    void stop();

    /**
     * Отключаем всех клиентов
     */
    void dropAll();

    /**
     * Метод получает и сохраняет ссылку на главный FX-контекст
     */
    void setFxContext(Basic fxContext);
}
