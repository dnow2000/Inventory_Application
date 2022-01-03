package model;

public class InHouse extends Part{

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, max, min);
        this.machineId = machineId;
    }

    //getter
    public int getMachineId() {
        return machineId;
    }

    //setter
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }


}
