package conspects.patterns.observer;

/**
 * Интерфейс Observer (интерфейс слушателя)
 */
public interface Observer {
    /* Обновить */
    void update();

    /* Установить тему */
    void setSubject(Subject subject);
}
