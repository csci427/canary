package edu.umt.csci427.canary;

import ice.Numeric;
import rosetta.MDC_ECG_HEART_RATE;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class ElectroCardioGramFactory extends OpenICEAbstractFactory {
    public static final String factoryName = "ECGFactory";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Heart Rate (ECG)")){//comment out if you want all values to go through
            try{
                //Title, units, metric id
                myData = Monitor.newInstance(MDC_ECG_HEART_RATE.VALUE, "BPM", data);//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create monitor, message: " + ex.toString());
            }
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
