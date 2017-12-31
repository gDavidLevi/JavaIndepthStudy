package conspects.patterns.abstractFactory;

/**
 * Класс WorkStationFactory (создание именно WorkStation)
 *
 * @see ComputersAbstractFactory интерфейс используется для реализации метода createComputer()
 */
public class WorkStationFactory implements ComputersAbstractFactory {
    private String ram;
    private String hdd;
    private String cpu;

    public WorkStationFactory(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    @Override
    public Computer createComputer() {
        return new WorkStation(ram, hdd, cpu);
    }
}
