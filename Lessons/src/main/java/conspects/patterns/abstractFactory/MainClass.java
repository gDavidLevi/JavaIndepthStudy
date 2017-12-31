package conspects.patterns.abstractFactory;

public class MainClass {
    public static void main(String[] args){
        /**
         * Фабрика компьютеров ComputerFactory будет создавать компьютер методом getComputer().
         *
         * Метод getComputer() принимает объект класса ComputersAbstractFactory, а именно объекты классов
         * реализующие интерфейс ComputersAbstractFactory (WorkStationFactory и ServerFactory), затем создает
         * соответствующие объекты типов WorkStation или Server.
         */
        Computer pc = ComputerFactory.getComputer(new WorkStationFactory("16", "512Gb", "i5 7500"));
        System.out.println(pc);

        Computer server = ComputerFactory.getComputer(new ServerFactory("128", "2Tb", "Xeon"));
        System.out.println(server);
    }
}
