package conspects.patterns.abstractFactory;

public class WorkStation extends Computer {
    private String ram;
    private String hdd;
    private String cpu;

    public WorkStation(String ram, String hdd, String cpu) {
        this.ram = ram;
        this.hdd = hdd;
        this.cpu = cpu;
    }

    @Override
    public String getRam() {
        return ram;
    }

    @Override
    public String getHdd() {
        return hdd;
    }

    @Override
    public String getCpu() {
        return cpu;
    }
}
