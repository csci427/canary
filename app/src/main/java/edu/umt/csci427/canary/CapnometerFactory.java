package edu.umt.csci427.canary;


import rosetta.MDC_AWAY_CO2_ET;
import rosetta.MDC_CO2_RESP_RATE;

import static edu.umt.csci427.canary.ThresholdData.*;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class CapnometerFactory extends OpenICEAbstractFactory {

    public static final String factoryName = "CAPNOMETER_FACTORY";


    // set threshold fragment with appropriate defaults
    ThresholdFragment thresholdFragment = new twoThresholdFragment();

    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        if(data != null && data.equals("Simulated Capnometer etCO2")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "etCO2", MDC_AWAY_CO2_ET.VALUE);//TODO: true should match a unique device id.

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(ETCO2_HIGH, ETCO2_LOW);
            thresholdFragment.setMax(ETCO2_MAX);
        }
        else if(data != null && data.equals("Simulated Capnometer Resp Rate")){
            myData = Monitor.newInstance(data, "Respiratory Rate", MDC_CO2_RESP_RATE.VALUE);

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(RESP_RATE_HIGH, RESP_RATE_LOW);
            thresholdFragment.setMax(RESP_RATE_MAX);
        }
        else{
            System.out.println(factoryName + "Data is null or not simulated.");
        }


        thresholdFragment.setMonitor(myData);
        myData.setThresholdFragment(thresholdFragment);

        return myData;
    }

    @Override
    Monitor PackageOpenICERealTimeData(String data) {
        return null;
    }
}
