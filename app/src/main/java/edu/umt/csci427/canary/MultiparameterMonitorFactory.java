package edu.umt.csci427.canary;

import ice.Numeric;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class MultiparameterMonitorFactory extends OpenICEAbstractFactory {

    public static final String factoryName = "MultiparameterMonitorFactory";

    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Simulated Multiparameter Monitor")){//comment out if you want all values to go through
            myData = Monitor.newInstance("", "BPM", data);//TODO: true should match a unique device id.
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }
        return myData;
    }

    @Override
    Monitor PackageOpenICERealTimeData(String data) {
        return null;
    }
}
