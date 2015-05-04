package edu.umt.csci427.canary;

import rosetta.MDC_PRESS_CUFF_DIA;
import rosetta.MDC_PRESS_CUFF_SYS;
import rosetta.MDC_PULS_RATE_NON_INV;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class NoninvasiveBloodPressureFactory extends OpenICEAbstractFactory{

    public static final String factoryName = "NoninvasiveBloodPressureFactory";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Simulated Noninvasive BP SYS")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "mmHg", MDC_PRESS_CUFF_SYS.VALUE);//TODO: true should match a unique device id.
        }
        else if(data != null && data.equals("Simulated Noninvasive BP DIA")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "mmHg", MDC_PRESS_CUFF_DIA.VALUE);//TODO: true should match a unique device id.
        }
        else if(data != null && data.equals("Simulated Noninvasive BP NonInvasive")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "BPM", MDC_PULS_RATE_NON_INV.VALUE);//TODO: true should match a unique device id.
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
