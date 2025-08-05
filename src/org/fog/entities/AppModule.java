
package org.fog.entities;

import java.util.LinkedList;
import java.util.List;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;

public class AppModule extends Vm {

    private String name;
    private String appId;
    private List<String> inputTupleIds;
    private List<String> outputTupleIds;
    private double energyConsumption;
    private double costPerMi;
    private double lastUtilization;
    private double totalUtilization;

    public AppModule(String name, String appId, int userId, double mips,
            int ram, long bw, long size) {
        super(0, userId, mips, 1, ram, bw, size, "Xen",
                new CloudletSchedulerTimeShared());
        this.name = name;
        this.appId = appId;
        this.inputTupleIds = new LinkedList<>();
        this.outputTupleIds = new LinkedList<>();
        this.energyConsumption = 0.0;
        this.costPerMi = 0.0;
        this.lastUtilization = 0.0;
        this.totalUtilization = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getAppId() {
        return appId;
    }

    public List<String> getInputTupleIds() {
        return inputTupleIds;
    }

    public List<String> getOutputTupleIds() {
        return outputTupleIds;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public double getCostPerMi() {
        return costPerMi;
    }

    public void setCostPerMi(double costPerMi) {
        this.costPerMi = costPerMi;
    }

    public double getLastUtilization() {
        return lastUtilization;
    }

    public void setLastUtilization(double lastUtilization) {
        this.lastUtilization = lastUtilization;
    }

    public double getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(double totalUtilization) {
        this.totalUtilization = totalUtilization;
    }

    public void processTupleArrival(org.cloudbus.cloudsim.core.SimEvent ev) {
        System.out.println("AppModule " + getName() + " received event: " + ev.getTag());
    }

}
