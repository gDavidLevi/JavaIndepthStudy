package conspects.patterns.observer;

public class MainClass {
    public static void main(String[] args) {
        /* Создаем тему */
        MyTopic topic = new MyTopic();

        /* Создаем подписчиков */
        Observer observer1 = new Subscriber("Rick 1");
        Observer observer2 = new Subscriber("Davi 2");
        Observer observer3 = new Subscriber("Moto 3");

        /* Регистрируем в блоге слушателей */
        topic.register(observer1);
        topic.register(observer2);
        topic.register(observer3);

        /* Подписываем слушателей на тему */
        observer1.setSubject(topic);
        observer2.setSubject(topic);
        observer3.setSubject(topic);

        /* Постим в тему сообщение */
        topic.postMessage("Wow! Incredible");

        /* Отпишем слушателя 2 */
        observer2.setSubject(null);

        /* Удалим слушателя 2 из темы */
        topic.unregister(observer2);

        /* Еще раз постим в тему сообщение */
        topic.postMessage("Important news!");
    }
}
