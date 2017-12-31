package conspects.patterns.abstractFactory;

/**
 * Класс ComputerFactory (фабрика компьютеров)
 *
 * @see ComputersAbstractFactory интерфейс с одним методом createComputer()
 */
class ComputerFactory {
    /**
     * Статический метод Computer (создаёт компьютер на фабрике)
     *
     * @param factory интерфейс ComputersAbstractFactory
     * @return Computer
     */
    static Computer getComputer(ComputersAbstractFactory factory) {
        return factory.createComputer();
    }
}
