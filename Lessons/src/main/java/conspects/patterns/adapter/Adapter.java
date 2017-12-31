package conspects.patterns.adapter;

/**
 * Класс Adapter (адаптер)
 *
 * @see ru.davidlevi.conspect.patterns.adapter.ChinaConnector адаптация к китайской розетке
 */
public class Adapter implements ChinaConnector {
    /* Русская вилка */
    private RussianConnector russianConnector;

    /* Конструктор */
    Adapter(RussianConnector russianConnector) {
        this.russianConnector = russianConnector;
    }

    /**
     * Адаптация! "Втыкаем в китайскую розетку русскую вилку"
     */
    @Override
    public void provideElectricity() {
        russianConnector.giveElectricity();
    }
}
