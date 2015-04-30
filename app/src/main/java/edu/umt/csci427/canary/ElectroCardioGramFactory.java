package edu.umt.csci427.canary;

import ice.Numeric;
import rosetta.MDC_ECG_HEART_RATE;
import rosetta.MDC_TTHOR_RESP_RATE;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class ElectroCardioGramFactory extends OpenICEAbstractFactory {
    public static final String factoryName = "ECGFactory";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Simulated ElectroCardioGram HR")){//comment out if you want all values to go through
            //Title, units, metric id
            myData = Monitor.newInstance(MDC_ECG_HEART_RATE.VALUE, "HR", data);//TODO: true should match a unique device id.
        }
        else if(data != null && data.equals("Simulated ElectroCardioGram Resp Rate")){//comment out if you want all values to go through
            //Title, units, metric id
            myData = Monitor.newInstance(MDC_TTHOR_RESP_RATE.VALUE, "Respiratory Rate", data);//TODO: true should match a unique device id.
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
