package conspects.patterns.adapter;

/**
 * Класс RussianSocket (русская разетка)
 */
class ChinaSocket {
    /**
     * Воткнуть!
     *
     * @param plug вилка
     */
    void plugin(ChinaConnector plug) {
        plug.provideElectricity();
    }
}
