package edu.umt.csci427.canary;

import java.io.Console;

/**
 * Created by RYELAPTOP on 3/18/2015.
 */
public class PulseOximeterFactory extends OpenICEAbstractFactory {


    public static final String factoryName = "PULSE_OXIMETER_FACTORY";
    @Override
    OpenICEDataPackage PackageOpenICESimulatedData(ice.Numeric data) {

        OpenICEDataPackage myData = null;
        if(data != null && data.metric_id.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)){//comment out if you want all values to go through
            try{
                myData = new OpenICEDataPackage(data.metric_id, data.value, true);//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create OpenICEDataPackage, message: " + ex.toString());
            }
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }
        return myData;
    }

    @Override
    OpenICEDataPackage PackageOpenICERealTimeData(ice.Numeric data) {
        OpenICEDataPackage myData = null;
        if(data != null && data.metric_id.equals(rosetta.MDC_PULS_OXIM_PULS_RATE.VALUE)){//TODO: May need to be something else
            try{
                myData = new OpenICEDataPackage(data.metric_id, data.value, true);//TODO: true should match a unique device id.
            }
            catch(Exception ex){
                System.out.println("Could not create OpenICEDataPackage, message: " + ex.toString());
            }
        }
        else{
            System.out.println(factoryName + "Data is null or not real time.");
        }
        return myData;
    }
}
