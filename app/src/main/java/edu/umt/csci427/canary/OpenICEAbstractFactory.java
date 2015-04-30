package edu.umt.csci427.canary;

import rosetta.MDC_ECG_HEART_RATE;
import rosetta.MDC_PRESS_CUFF_SYS;
import rosetta.MDC_PULS_OXIM_PULS_RATE;
import rosetta.MDC_PULS_OXIM_SAT_O2;

/**
 * Abstract Factory for simulated and real data from OpenICE
 */
public abstract class OpenICEAbstractFactory {

    private static PulseOximeterFactory pulseOximeterFactory = null;
    private static ElectroCardioGramFactory ECGFactory = null;
    private static NoninvasiveBloodPressureFactory BPFactory = null;
    private static CapnometerFactory capnometerFactory = null;
    private static MultiparameterMonitorFactory multiParameterMonitorFactory = null;
    private static TempuratureProbeFactory tempProbeFactory = null;
    private static InfusionPumpFactory infusionPumpFactory = null;

    ///Creates data package for Adapter pattern for simulated data
    abstract Monitor PackageOpenICESimulatedData(String data);

    ///Creates data package for Adapter pattern for real time data
    abstract Monitor PackageOpenICERealTimeData(String data);

    //TODO: add each type value
    public static OpenICEAbstractFactory GetSimulatedFactory(String factoryType){
        if(factoryType != null && !factoryType.isEmpty()){
            switch(factoryType){
                //PULSE OX PULSE RATE
                case "Pulse Rate (PulseOX)":
                    if(pulseOximeterFactory == null) {
                        pulseOximeterFactory = new PulseOximeterFactory();
                    }
                    return pulseOximeterFactory;
                case "SpO2 (PulseOX)":
                    if(pulseOximeterFactory == null) {
                        pulseOximeterFactory = new PulseOximeterFactory();
                    }
                    return pulseOximeterFactory;
                case "Heart Rate (ECG)":
                    if(ECGFactory == null) {
                        ECGFactory = new ElectroCardioGramFactory();
                    }
                    return ECGFactory;
                case "Systolic BP (CUFF)":
                    if(BPFactory == null) {
                        BPFactory = new NoninvasiveBloodPressureFactory();
                    }
                    return BPFactory;
                case "Simulated Capnometer":
                    if(capnometerFactory == null) {
                        capnometerFactory = new CapnometerFactory();
                    }
                    return capnometerFactory;
                case "Simulated Infusion Pump":
                    if(infusionPumpFactory == null) {
                        infusionPumpFactory = new InfusionPumpFactory();
                    }
                    return infusionPumpFactory;
                case "Simulated Multiparameter Monitor":
                    if(multiParameterMonitorFactory == null) {
                        multiParameterMonitorFactory = new MultiparameterMonitorFactory();
                    }
                    return multiParameterMonitorFactory;
                case "Simulated Temp Probe":
                    if(tempProbeFactory == null) {
                        tempProbeFactory = new TempuratureProbeFactory();
                    }
                    return tempProbeFactory;
                default:
                    return null;
                }
        }
        else{
            return null;
        }
    }


    ///Default constructor.
    public OpenICEAbstractFactory(){};
}
