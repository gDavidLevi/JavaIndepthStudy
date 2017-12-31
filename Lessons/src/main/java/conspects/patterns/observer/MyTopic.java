package conspects.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс MyTopic (моя лента блога)
 */
public class MyTopic implements Subject {
    /* Список наблюдателей */
    private List<Observer> observers;
    /* Новое сообщение */
    private String message;
    /* Произошли ли изменения? */
    private boolean changed;
    /* Монитор.  */
    private final Object monitor = new Object();

    /* Конструктор */
    MyTopic() {
        observers = new ArrayList<>();
    }

    @Override
    public void register(Observer obj) {
        /* Зарегистрировать слушателя, если его нет в списке */
        if (!observers.contains(obj)) observers.add(obj);
    }

    @Override
    public void unregister(Observer obj) {
        /* Удалить слушателя, если он в списке */
        if (observers.contains(obj)) observers.remove(obj);
    }

    @Override
    public void notifyObservers() {
        /* Запустить у всех слушателей метод update() для того, чтобы онги получили новое сообщение из топика */
        for (Observer o : observers) o.update();
        /* Изменений уже нет */
        this.changed = false;
    }

    /**
     * Возвращает объект (String) с сообщением данного топика
     *
     * @param obj слушатель топика, подписчик имплементирующий интерфейс Observer
     * @return String
     */
    @Override
    public String getUpdate(Observer obj) {
        return this.message;
    }

    /**
     * Отправить сообщение
     *
     * @param message текст сообщения
     */
    void postMessage(String message) {
        /* Сохранить сообщение в моем топике */
        this.message = message;
        /* Вывести егов консоль */
        System.out.println("A new message in blog: " + this.message);
        /* Появились изменения */
        this.changed = true;
        /* Известить всех слушателей */
        notifyObservers();
    }
}
