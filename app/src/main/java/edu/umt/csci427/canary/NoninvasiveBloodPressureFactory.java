package edu.umt.csci427.canary;

import ice.Numeric;
import rosetta.MDC_ECG_HEART_RATE;
import rosetta.MDC_PRESS_CUFF_SYS;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class NoninvasiveBloodPressureFactory extends OpenICEAbstractFactory{

    public static final String factoryName = "NoninvasiveBloodPressureFactory";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Simulated Noninvasive BP")){//comment out if you want all values to go through
           myData = Monitor.newInstance(MDC_PRESS_CUFF_SYS.VALUE, "mmHg", data);//TODO: true should match a unique device id.
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
