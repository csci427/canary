package edu.umt.csci427.canary;

import java.io.Console;

import rosetta.MDC_PULS_OXIM_PULS_RATE;
import rosetta.MDC_PULS_OXIM_SAT_O2;

/**
 * Created by RYELAPTOP on 3/18/2015.
 */
public class PulseOximeterFactory extends OpenICEAbstractFactory {


    public static final String factoryName = "PULSE_OXIMETER_FACTORY";
    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Pulse Rate (PulseOX)")){//comment out if you want all values to go through
            try{
                myData = Monitor.newInstance(MDC_PULS_OXIM_PULS_RATE.VALUE, "BPM", data);//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create Monitor, message: " + ex.toString());
            }
        }
        else if(data != null && data.equals("SpO2 (PulseOX)")){//comment out if you want all values to go through
            try{
                myData = Monitor.newInstance(MDC_PULS_OXIM_SAT_O2.VALUE, "%", data);//TODO: true should match a unique device id.
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
    //TODO: Clean try catch
    @Override
    Monitor PackageOpenICERealTimeData(String data) {
        Monitor myData = null;
        if(data != null && data.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)){//comment out if you want all values to go through
            try{
                myData = Monitor.newInstance("Pulse Rate (OX)", "", data);//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create Monitor, message: " + ex.toString());
            }
        }
        else if(data != null && data.equals(MDC_PULS_OXIM_SAT_O2.VALUE)){//comment out if you want all values to go through
            try{
                myData = Monitor.newInstance("SpO2 (PulseOX)", "", data);//TODO: true should match a unique device id.
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
