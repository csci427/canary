package edu.umt.csci427.canary;

import rosetta.MDC_ECG_HEART_RATE;
import rosetta.MDC_TTHOR_RESP_RATE;

import static edu.umt.csci427.canary.ThresholdData.*;

/**
 * Created by RYELAPTOP on 4/6/2015.
 */
public class ElectroCardioGramFactory extends OpenICEAbstractFactory {
    public static final String factoryName = "ECGFactory";

    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        // set threshold fragment with appropriate defaults
        ThresholdFragment thresholdFragment = new twoThresholdFragment();

        if(data != null && data.equals("Simulated ElectroCardioGram HR")){//comment out if you want all values to go through
            //Title, units, metric id
            myData = Monitor.newInstance(data, "HR", MDC_ECG_HEART_RATE.VALUE);//TODO: true should match a unique device id.

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(PULSE_RATE_HIGH, PULSE_RATE_LOW);
            thresholdFragment.setMax(PULSE_RATE_MAX);
        }
        else if(data != null && data.equals("Simulated ElectroCardioGram Resp Rate")){//comment out if you want all values to go through
            //Title, units, metric id
            myData = Monitor.newInstance(data, "Respiratory Rate", MDC_TTHOR_RESP_RATE.VALUE);//TODO: true should match a unique device id.

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(RESP_RATE_HIGH,RESP_RATE_LOW);
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
