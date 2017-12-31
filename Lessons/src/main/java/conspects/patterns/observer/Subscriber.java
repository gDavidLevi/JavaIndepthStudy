package conspects.patterns.observer;

/**
 * Класс Subscriber (подписчик)
 */
public class Subscriber implements Observer {
    /* Имя подписчика */
    private String name;

    /* Интерфейс Subject */
    private Subject topic; // тема для обсуждения в блоге

    /* Конструктор */
    Subscriber(String name) {
        this.name = name;
    }

    /**
     * Загрузить обновления из топика
     */
    @Override
    public void update() {
        /* Получить обновления из топика по установленной теме topic */
        String string = topic.getUpdate(this);
        /**/
        if (string == null) System.out.println(name + ": nothing changed");
        else System.out.println(name + ": new message: " + string);
    }

    /**
     * Установить тему топика
     *
     * @param subject тема
     */
    @Override
    public void setSubject(Subject subject) {
        this.topic = subject;
    }
}
