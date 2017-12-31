package conspects.patterns.abstractFactory;

/**
 * Класс ServerFactory (создание именно Server)
 *
 * @see ComputersAbstractFactory интерфейс используется для реализации метода createComputer()
 */
public class ServerFactory implements ComputersAbstractFactory {
    private String ram;
    private String hdd;
    private String cpu;

    public ServerFactory(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    @Override
    public Computer createComputer() {
        return new Server(ram, hdd, cpu);
    }
}
