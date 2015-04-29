package edu.umt.csci427.canary;

import java.io.Console;

/**
 * Created by RYELAPTOP on 3/18/2015.
 */
public class PulseOximeterFactory extends OpenICEAbstractFactory {


    public static final String factoryName = "PULSE_OXIMETER_FACTORY";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)){//comment out if you want all values to go through
            try{
                myData = Monitor.newInstance(data, "", "");//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create Monitor, message: " + ex.toString());
            }
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }
        return myData;
    }

    @Override
    Monitor PackageOpenICERealTimeData(String data) {
        Monitor myData = null;
        if(data != null && data.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)){//comment out if you want all values to go through
            try{
                myData = Monitor.newInstance(data, "", "");//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create Monitor, message: " + ex.toString());
            }
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }
        return myData;
    }
}
