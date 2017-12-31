package conspects.patterns.observer;

/**
 * Интерфейс Subject (тема для обсуждения в блоге)
 */
public interface Subject {
    /* Зарегистрировать слушателя */
    void register(Observer obj);

    /* Удалить слушателя */
    void unregister(Observer obj);

    /* Уведомить слушателей */
    void notifyObservers();

    /* Получить обновления */
    String getUpdate(Observer obj);
}
