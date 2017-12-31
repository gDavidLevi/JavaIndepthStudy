package conspects.patterns.factory;

public class MainClass {
    public static void main(String[] args) {
        /* Создать объект по типу */
        Computer computer = ComputerFactory.getComputer(ComputerType.WORKSTATION, "16Gb", "1Tb", "i5 7500");
        System.out.println(computer);
    }
}
