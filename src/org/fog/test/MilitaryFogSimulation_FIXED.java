package org.fog.test;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.*;
import org.fog.application.*;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.entities.*;
import org.fog.placement.*;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.distribution.DeterministicDistribution;
import org.fog.custom.MyAppModule;
import java.util.*;

public class MilitaryFogSimulation_FIXED {

    public static void main(String[] args) {
        System.out.println("Starting MilitaryFogSimulation...");

        try {
            int numUser = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;
            CloudSim.init(numUser, calendar, traceFlag);

            String appId = "militaryApp";
            FogBroker broker = new FogBroker("broker");

            FogDevice cloud = createFogDevice("cloud", 44800, 40000, 100, 10000, 0.01, 16.0);
            FogDevice fog1 = createFogDevice("fog1", 5600, 4000, 100, 1000, 0.0, 2.0);
            FogDevice fog2 = createFogDevice("fog2", 5600, 4000, 100, 1000, 0.0, 2.0);

            fog1.setParentId(cloud.getId());
            fog2.setParentId(cloud.getId());

            List<FogDevice> fogDevices = Arrays.asList(cloud, fog1, fog2);

            Sensor sensor1 = new Sensor("sensor1", "SENSOR", broker.getId(), appId, new DeterministicDistribution(5));
            Sensor sensor2 = new Sensor("sensor2", "SENSOR", broker.getId(), appId, new DeterministicDistribution(5));
            Sensor sensor3 = new Sensor("sensor3", "SENSOR", broker.getId(), appId, new DeterministicDistribution(5));

            Actuator display = new Actuator("display", broker.getId(), appId, "DISPLAY");

            sensor1.setGatewayDeviceId(fog1.getId());
            sensor1.setLatency(1.0);
            sensor2.setGatewayDeviceId(fog2.getId());
            sensor2.setLatency(1.0);
            sensor3.setGatewayDeviceId(fog2.getId());
            sensor3.setLatency(1.0);
            display.setGatewayDeviceId(fog1.getId());
            display.setLatency(1.0);

            Application application = createApplication(appId, broker.getId());
            application.setUserId(broker.getId());

            ModuleMapping moduleMapping = ModuleMapping.createModuleMapping();
            moduleMapping.addModuleToDevice("processing", fog1.getName());
            moduleMapping.addModuleToDevice("decision", cloud.getName());
            List<Sensor> sensors = Arrays.asList(sensor1, sensor2, sensor3);
            List<Actuator> actuators = Arrays.asList(display);
            Controller controller = new Controller("master-controller", fogDevices, sensors, actuators);
            controller.submitApplication(application,
                    new ModulePlacementMapping(fogDevices, application, moduleMapping));

            CloudSim.startSimulation();
            CloudSim.stopSimulation();

            System.out.println("MilitaryFogSimulation finished!");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred.");
        }
    }

    private static FogDevice createFogDevice(String nodeName, long mips, int ram, long upBw, long downBw,
            double ratePerMips, double busyPower) throws Exception {

        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(mips)));

        int hostId = FogUtils.generateEntityId();
        long storage = 1000000; // 1 GB
        int bw = 10000;

        PowerHost host = new PowerHost(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList,
                new StreamOperatorScheduler(peList),
                new FogLinearPowerModel(busyPower, 100.0));

        // Use host directly here — NOT a list
        FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
                "x86", "Linux", "Xen", host,
                10.0, 3.0, 0.05, 0.001, 0.0);

        List<Storage> storageList = new ArrayList<>();

        FogDevice fogDevice = new FogDevice(
                nodeName,
                characteristics,
                new AppModuleAllocationPolicy(Collections.singletonList(host)),
                storageList,
                10, // scheduling interval
                upBw, downBw,
                2.0, // uplink latency
                ratePerMips);
        fogDevice.setUplinkLatency(2.0);

        return fogDevice;
    }

    private static Application createApplication(String appId, int userId) {
        Application application = Application.createApplication(appId, userId);
        MyAppModule processing = new MyAppModule("processing", appId, userId, 1000, 1024, 1000, 10000);
        MyAppModule decision = new MyAppModule("decision", appId, userId, 1000, 1024, 1000, 10000);

        application.addAppModule("processing", 1000);
        application.addAppModule("decision", 1000);

        application.addAppEdge("SENSOR", "processing", 3000, 500, "SENSOR", Tuple.UP, AppEdge.SENSOR);
        application.addAppEdge("processing", "decision", 1000, 1000, "PROCESSED", Tuple.UP, AppEdge.MODULE);
        application.addAppEdge("decision", "DISPLAY", 500, 100, "ACTION", Tuple.DOWN, AppEdge.ACTUATOR);

        // tuple.setCpuLength(1000); // in million instructions
        // tuple.setNwLength(1000); // in bytes

        application.addTupleMapping("processing", "SENSOR", "PROCESSED", new FractionalSelectivity(1.0));
        application.addTupleMapping("decision", "PROCESSED", "ACTION", new FractionalSelectivity(1.0));

        application.setLoops(Arrays.asList(new AppLoop(Arrays.asList("SENSOR", "processing", "decision", "DISPLAY"))));

        return application;
    }
}
