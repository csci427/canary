package edu.umt.csci427.canary;

import rosetta.MDC_ECG_HEART_RATE;
import rosetta.MDC_PRESS_CUFF_SYS;
import rosetta.MDC_PULS_OXIM_PULS_RATE;
import rosetta.MDC_PULS_OXIM_SAT_O2;

/**
 * Abstract Factory for simulated and real data from OpenICE
 */
public abstract class OpenICEAbstractFactory {


    ///Creates data package for Adapter pattern for simulated data
    //TODO: Unimplemented. We were unable to test with real data any implementation would have been
    //TODO: error prone.
    abstract Monitor PackageOpenICESimulatedData(String data);

    ///Creates data package for Adapter pattern for real time data
    abstract Monitor PackageOpenICERealTimeData(String data);

    //TODO: add each type value
    public static OpenICEAbstractFactory GetSimulatedFactory(String factoryType){
        if(factoryType.equalsIgnoreCase("Simulated ElectroCardioGram HR") ||factoryType.equalsIgnoreCase("Simulated ElectroCardioGram Resp Rate")){
            return new ElectroCardioGramFactory();
        }
        else if(factoryType.equalsIgnoreCase("Simulated Capnometer etCO2") ||factoryType.equalsIgnoreCase("Simulated Capnometer Resp Rate")){
            return new CapnometerFactory();
        }
        else if(factoryType.equalsIgnoreCase("Simulated Noninvasive BP SYS") ||factoryType.equalsIgnoreCase("Simulated Noninvasive BP DIA")||
                factoryType.equalsIgnoreCase("Simulated Noninvasive BP NonInvasive")){
            return new NoninvasiveBloodPressureFactory();
        }
        else if(factoryType.equalsIgnoreCase("Simulated Pulse Oximeter SpO2") ||factoryType.equalsIgnoreCase("Simulated Pulse Oximeter Pulse Rate")){
            return new PulseOximeterFactory();
        }
        else{
            return null;
        }
    }

    ///Default constructor.
    public OpenICEAbstractFactory(){};
}
