package org.fog.custom;

import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.entities.AppModule;
//import org.fog.utils.Logger;

public class MyAppModule extends AppModule {

    public MyAppModule(String name, String appId, int userId, double mips, int ram, long bw, long size) {
        super(name, appId, userId, mips, ram, bw, size);
    }

    @Override
    public void processTupleArrival(SimEvent ev) {
        super.processTupleArrival(ev);
        System.out.println(getName() + " received tuple: " + ev.getTag());
    }
}
