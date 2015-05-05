package edu.umt.csci427.canary;

import rosetta.MDC_PULS_OXIM_PULS_RATE;
import rosetta.MDC_PULS_OXIM_SAT_O2;

import static edu.umt.csci427.canary.ThresholdData.PULSE_RATE_HIGH;
import static edu.umt.csci427.canary.ThresholdData.PULSE_RATE_LOW;
import static edu.umt.csci427.canary.ThresholdData.PULSE_RATE_MAX;
import static edu.umt.csci427.canary.ThresholdData.SPO2_LOW;
import static edu.umt.csci427.canary.ThresholdData.SPO2_MAX;

/**
 * Created by RYELAPTOP on 3/18/2015.
 */
public class PulseOximeterFactory extends OpenICEAbstractFactory {


    public static final String factoryName = "PULSE_OXIMETER_FACTORY";


    @Override
    Monitor PackageOpenICESimulatedData(String data) {

        Monitor myData = null;
        ThresholdFragment thresholdFragment;
        if(data != null && data.equals("Simulated Pulse Oximeter Pulse Rate")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data, "BPM", MDC_PULS_OXIM_PULS_RATE.VALUE);//TODO: true should match a unique device id.

            // set threshold fragment with appropriate defaults
            thresholdFragment = new twoThresholdFragment();

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(PULSE_RATE_HIGH, PULSE_RATE_LOW);
            thresholdFragment.setMax(PULSE_RATE_MAX);
        }
        else if(data != null && data.equals("Simulated Pulse Oximeter SpO2")){//comment out if you want all values to go through
            myData = Monitor.newInstance(data   , "%", MDC_PULS_OXIM_SAT_O2.VALUE);//TODO: true should match a unique device id.

            // set threshold fragment with appropriate defaults
            thresholdFragment = new oneThresholdFragment();

            // set thresholds to appropriate values
            thresholdFragment.setDefaultThresholds(Integer.MAX_VALUE, SPO2_LOW);
            thresholdFragment.setMax(SPO2_MAX);
        }
        else{

            System.out.println(factoryName + "Data is null or not simulated.");
            thresholdFragment = null;
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
