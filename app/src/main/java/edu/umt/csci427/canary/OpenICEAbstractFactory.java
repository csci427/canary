package edu.umt.csci427.canary;

/**
 * Abstract Factory for simulated and real data from OpenICE
 */
public abstract class OpenICEAbstractFactory {

    private PulseOximeterFactory pulseOximeterFactory = null;

    ///Creates data package for Adapter pattern for simulated data
    abstract OpenICEDataPackage PackageOpenICESimulatedData(ice.Numeric data);

    ///Creates data package for Adapter pattern for real time data
    abstract OpenICEDataPackage PackageOpenICERealTimeData(ice.Numeric data);

    public OpenICEAbstractFactory GetSimulatedFactory(String factoryType){
        if(factoryType != null){
            switch(factoryType){
                case "PULSE_OX":
                    if(pulseOximeterFactory == null) {
                        pulseOximeterFactory = new PulseOximeterFactory();
                    }
                    return pulseOximeterFactory;
                default:
                    return null;
                }
        }
        else{
            return null;
        }
    }

    public OpenICEAbstractFactory GetRealTimeDataFactory(String factoryType){
        if(factoryType != null){
            switch(factoryType){
                case "PULSE_OX":
                    if(pulseOximeterFactory == null) {//TODO: turn into real device
                        pulseOximeterFactory = new PulseOximeterFactory();
                    }
                    return pulseOximeterFactory;
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
