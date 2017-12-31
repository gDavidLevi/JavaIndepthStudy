package conspects.patterns.factory;

/**
 * Класс Computer (некий компьютер)
 */
public abstract class Computer {
    /* Методы */
    public abstract String getRam();
    public abstract String getHdd();
    public abstract String getCpu();

    @Override
    public String toString() {
        return "RAM: " + getRam() + " HDD: " + getHdd() + " CPU: " + getCpu();
    }
}
