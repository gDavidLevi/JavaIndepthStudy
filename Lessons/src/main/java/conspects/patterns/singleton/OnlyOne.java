package conspects.patterns.singleton;

/**
 * Класс OnlyOne (для создание только одного объекта класса)
 * <p>
 * Всегда финальный!
 */
final class OnlyOne {
    /* Поле */
    private static OnlyOne instance;

    /**
     * Конструктор
     * <p>
     * Всегда приватный!
     */
    private OnlyOne() {
    }

    /**
     * Метод getInstance() создает только одного объекта класса
     * <p>
     * Всегда статический и синхронизированный!
     *
     * @return объект типа OnlyOne
     */
    static synchronized OnlyOne getInstance() {
        if (instance == null) instance = new OnlyOne();
        return instance;
    }
}
