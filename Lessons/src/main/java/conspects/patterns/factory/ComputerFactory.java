package conspects.patterns.factory;

/**
 * Класс ComputerFactory (фабрика компьютеров)
 */
class ComputerFactory {
    /**
     * Статический метод Computer (создаёт компьютер на фабрике)
     *
     * @param type ComputerType
     * @param ram  String
     * @param hdd  String
     * @param cpu  String
     * @return Computer или null
     */
    static Computer getComputer(ComputerType type, String ram, String hdd, String cpu) {
        /* Что именно будем создавать? */
        if (type.equals(ComputerType.WORKSTATION)) {
            return new Workstation(ram, hdd, cpu);
        } else if (type.equals(ComputerType.SERVER))
            return new Server(ram, hdd, cpu);
        return null;
    }
}
