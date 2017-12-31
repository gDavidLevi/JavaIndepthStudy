package conspects.patterns.adapter;

/**
 * Класс RussianSocket (русская разетка)
 */
class RussianSocket {
    /**
     * Воткнуть!
     *
     * @param plug вилка
     */
    void plugin(RussianConnector plug) {
        plug.giveElectricity();
    }
}
